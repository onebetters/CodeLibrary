package com.zzc.easyExcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzc.easyExcel.annotation.ExcelFieldExclude;
import com.zzc.easyExcel.annotation.ExcelFieldMerge;
import com.zzc.easyExcel.annotation.ExcelFieldSum;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

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
        Map<Integer, Field> columnIndexMap = new HashMap<>(16);
        List<Field>         fields         = new ArrayList<>();

        for(Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }

        //过滤出EasyExcel需要导出的字段，否则index不准
        fields = fields.stream().filter(field -> field.isAnnotationPresent(ExcelProperty.class)).collect(Collectors.toList());
        //字段的反射顺序和编写时的顺序是一样的
        for(int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (!field.isAnnotationPresent(ExcelIgnore.class)) {
                switch (type) {
                    case MERGER:
                        if (field.isAnnotationPresent(ExcelFieldMerge.class)) {
                            columnIndexMap.put(i, field);
                        }
                        break;
                    case SUM:
                        if (field.isAnnotationPresent(ExcelFieldSum.class)) {
                            columnIndexMap.put(i, field);
                        }
                        break;
                    case EXCLUDE:
                        if (field.isAnnotationPresent(ExcelFieldExclude.class)) {
                            columnIndexMap.put(i, field);
                        }
                        break;
                    default: break;
                }
            }
        }
        return columnIndexMap;
    }

    /**
     * 每个自定义merge策略必实现的方法
     * @param sheet sheet
     * @param cell cell
     * @param columnMergeMap 需要合并的列数
     * @param mergeInfoMap Map<行index，往下merge（合并）的行数>
     * @param blankRowList 需要清除内容的行index
     */
    public void merge(@Nonnull Sheet sheet, @Nonnull Cell cell,
                      @Nonnull final Map<Integer, Field> columnMergeMap,
                      @Nonnull final Map<Integer, Long> mergeInfoMap,
                      @Nonnull final List<Integer> blankRowList) {
        if (columnMergeMap.containsKey(cell.getColumnIndex())) {
            if (mergeInfoMap.containsKey(cell.getRowIndex())) {
                Long mergeCount     = mergeInfoMap.get(cell.getRowIndex());
                int  nowRowIndex    = cell.getRowIndex();
                int  nowColumnIndex = cell.getColumnIndex();
                CellRangeAddress cellRangeAddress = new CellRangeAddress(nowRowIndex, nowRowIndex + mergeCount.intValue(), nowColumnIndex, nowColumnIndex);
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            } else {
                if (blankRowList.contains(cell.getRowIndex())) {
                    cell.setCellType(CellType.BLANK);
                }
            }
        }
    }
}
