package com.zzc.easyExcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.exception.ExcelCommonException;
import com.google.common.collect.Lists;
import com.zzc.easyExcel.annotation.ExcelFieldExclude;
import com.zzc.easyExcel.annotation.ExcelFieldMerge;
import com.zzc.easyExcel.annotation.ExcelFieldSum;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@UtilityClass
public class CustomUtils {

    /**
     * 得到需要过滤的列（字段）索引index  即@ExcelProperty 中的index
     */
    public Map<Integer, Field> getNeedColumnIndex(@Nonnull final Class<?> clz, @Nonnull final AnnotationType type) {
        Map<Integer, Field>       result        = new HashMap<>(80);
        Map<Integer, Field>       indexFiledMap = new TreeMap<>();
        Map<Integer, List<Field>> orderFiledMap = new TreeMap<>();
        List<Field>               fields        = new ArrayList<>();

        for(Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }

        Map<Integer, Field> fieldMapSort = buildFieldIndex(fields, indexFiledMap, orderFiledMap);

        fieldMapSort.forEach((index, field) -> {
            switch (type) {
                case MERGER:
                    if (field.isAnnotationPresent(ExcelFieldMerge.class)) {
                        result.put(index, field);
                    }
                    break;
                case SUM:
                    if (field.isAnnotationPresent(ExcelFieldSum.class)) {
                        result.put(index, field);
                    }
                    break;
                case EXCLUDE:
                    if (field.isAnnotationPresent(ExcelFieldExclude.class)) {
                        result.put(index, field);
                    }
                    break;
                default: break;
            }
        });
        return result;
    }

    private static Map<Integer, Field> buildFieldIndex(final List<Field> fieldList,
                                                       final Map<Integer, Field> indexFiledMap,
                                                       final Map<Integer, List<Field>> orderFiledMap) {
        List<Field> fields = fieldList.stream().filter(field -> field.isAnnotationPresent(ExcelProperty.class)).collect(Collectors.toList());
        for(int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (!field.isAnnotationPresent(ExcelIgnore.class)) {
                // 如果设置了顺序，以设置的顺序为主 index > order > default sort
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                if (excelProperty != null && excelProperty.index() >= 0) {
                    if (indexFiledMap.containsKey(excelProperty.index())) {
                        throw new ExcelCommonException("The index of '" + indexFiledMap.get(excelProperty.index()).getName()
                                                               + "' and '" + field.getName() + "' must be inconsistent");
                    }
                    indexFiledMap.put(excelProperty.index(), field);
                    continue;
                }

                int order = Integer.MAX_VALUE;
                if (excelProperty != null) {
                    order = excelProperty.order();
                }
                List<Field> orderFiledList = orderFiledMap.computeIfAbsent(order, key -> Lists.newArrayList());
                orderFiledList.add(field);
            }
        }
        return buildSortedAllFiledMap(orderFiledMap, indexFiledMap);
    }

    private static Map<Integer, Field> buildSortedAllFiledMap(final Map<Integer, List<Field>> orderFiledMap,
                                                              final Map<Integer, Field> indexFiledMap) {
        Map<Integer, Field> sortedAllFiledMap = new HashMap<>(
                (orderFiledMap.size() + indexFiledMap.size()) * 4 / 3 + 1);

        Map<Integer, Field> tempIndexFiledMap = new HashMap<>(indexFiledMap);
        int index = 0;
        for (List<Field> fieldList : orderFiledMap.values()) {
            for (Field field : fieldList) {
                while (tempIndexFiledMap.containsKey(index)) {
                    sortedAllFiledMap.put(index, tempIndexFiledMap.get(index));
                    tempIndexFiledMap.remove(index);
                    index++;
                }
                sortedAllFiledMap.put(index, field);
                index++;
            }
        }
        sortedAllFiledMap.putAll(tempIndexFiledMap);
        return sortedAllFiledMap;
    }

    /**
     * 每个自定义merge策略必实现的方法
     * @param sheet sheet
     * @param cell cell
     * @param columnMergeMap 需要合并的列
     * @param mergeInfoMap Map<尾行index，往上merge（合并）的行数>
     */
    public void merge(@Nonnull Sheet sheet, @Nonnull Cell cell,
                      @Nonnull final Map<Integer, Field> columnMergeMap,
                      @Nonnull final Map<Integer, Long> mergeInfoMap) {
        if (columnMergeMap.containsKey(cell.getColumnIndex()) && mergeInfoMap.containsKey(cell.getRowIndex())) {
            int  lastRowIndex    = cell.getRowIndex();
            int  lastColumnIndex = cell.getColumnIndex();
            int  firstRowIndex   = lastRowIndex - mergeInfoMap.get(lastRowIndex).intValue();
            // 清除之前cell里的值
            for (int rowIndex = lastRowIndex - 1; rowIndex > firstRowIndex; rowIndex--) {
                sheet.getRow(rowIndex).getCell(lastColumnIndex).setCellType(CellType.BLANK);
            }
            cell.setCellType(CellType.BLANK);

            CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRowIndex, lastRowIndex, lastColumnIndex, lastColumnIndex);
            addMergedRegion(sheet, cellRangeAddress);
        }
    }

    /**
     * 自定义合并单元格，代替官方的
     * 官方的接口{@link Sheet#addMergedRegion(org.apache.poi.ss.util.CellRangeAddress)}好多校验，性能很慢
     */
    private void addMergedRegion(Sheet sheet, CellRangeAddress cellRangeAddress) {
        SXSSFWorkbook workbook = (SXSSFWorkbook) sheet.getWorkbook();
        CTWorksheet  sheetX   = workbook.getXSSFWorkbook().getSheet(sheet.getSheetName()).getCTWorksheet();
        CTMergeCells ctMergeCells;

        if (sheetX.isSetMergeCells()) {
            ctMergeCells = sheetX.getMergeCells();
        } else {
            ctMergeCells = sheetX.addNewMergeCells();
        }

        CTMergeCell ctMergeCell = ctMergeCells.addNewMergeCell();
        ctMergeCell.setRef(cellRangeAddress.formatAsString());
    }
}
