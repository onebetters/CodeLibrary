package com.zzc.easyExcel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.zzc.easyExcel.AnnotationType;
import com.zzc.easyExcel.CustomUtils;
import com.zzc.easyExcel.annotation.ExcelFieldMerge;
import com.zzc.easyExcel.enums.CellMergeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 自定义单元格合并策略
 *
 * 存在的问题 fixme: poi的addMergedRegion很耗时，随着mergeCell的次数逐渐增大（数万~数十万）时候，
 *                  对cpu和内存消耗不仅大大增加，而且耗时也非常大，本地测试的时候，循环次数6W次，大约需要20-30分钟。
 *
 * <p>Filename: com.qianmi.gcs.wx.purchase.infrastructure.support.utils.easyExcel.handler.CellMergeWriterHandler.java</p>
 * <p>Date: 2021-11-04 19:08.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class CellMergeWriterHandler extends AbstractMergeStrategy{
    /**
     * 需要合并的列数
     */
    private final List<Integer> columnsOfRow    = new ArrayList<>();
    private final List<Integer> columnsOfColumn = new ArrayList<>();
    private final boolean       alignLeft;

    public CellMergeWriterHandler(@Nonnull final Class<?> clazz) {
        Map<Integer, Field> columnMergeMap = CustomUtils.getNeedColumnIndex(clazz, AnnotationType.MERGER);
        this.alignLeft = columnMergeMap.values().stream().anyMatch(field -> field.getAnnotation(ExcelFieldMerge.class).alignLeft());
        columnMergeMap.forEach((index, field) -> {
            CellMergeEnum mergeType = field.getAnnotation(ExcelFieldMerge.class).mergerType();
            if(CellMergeEnum.ROW.equals(mergeType)) {
                columnsOfRow.add(index);
            }
            if(CellMergeEnum.COLUMN.equals(mergeType)) {
                // 列数从1开始，默认都加1
                columnsOfColumn.add(index + 1);
            }
        });
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if(CollectionUtils.isNotEmpty(this.columnsOfRow) && this.columnsOfRow.contains(cell.getColumnIndex())) {
            mergeCell(sheet, cell, CellMergeEnum.ROW);
        }

        if(CollectionUtils.isNotEmpty(this.columnsOfColumn) && this.columnsOfColumn.contains(cell.getColumnIndex())) {
            mergeCell(sheet, cell, CellMergeEnum.COLUMN);
        }
    }

    private void mergeCell(Sheet sheet, Cell curCell, CellMergeEnum cellMergeEnum) {
        if (Objects.isNull(curCell)) {
            return;
        }

        int  rowIndex = curCell.getRowIndex();
        int  colIndex = curCell.getColumnIndex();
        Row  preRow   = null;
        Cell preCell  = null;

        if(CellMergeEnum.ROW.equals(cellMergeEnum)) {
            preRow = sheet.getRow(rowIndex - 1);
            preCell = preRow.getCell(colIndex);
        }

        if(CellMergeEnum.COLUMN.equals(cellMergeEnum)) {
            if(colIndex == 0) {
                return;
            }
            preRow = curCell.getRow();
            preCell = preRow.getCell(colIndex - 1);
        }

        if (Objects.isNull(preRow) || Objects.isNull(preCell)) {
            return;
        }

        if (alignLeft && CellMergeEnum.ROW.equals(cellMergeEnum)) {
            if (curCell.getColumnIndex() == 0) {
                mergeRows(sheet, preCell, curCell);
                return;
            }
            mergeRowsAlignLeft(sheet, curCell);
        } else {
            mergeRows(sheet, preCell, curCell);
        }
    }

    /**
     * 只在 CellMergeEnum.ROW 模式下生效
     * 左对齐 行单元格合并
     */
    private void mergeRowsAlignLeft(Sheet sheet, Cell curCell) {
        List<CellRangeAddress> list = sheet.getMergedRegions();
        CellRangeAddress firstColumnRangeAddress = list.stream().filter(e -> compareFirstColAndRow(e, curCell)).findFirst().orElse(null);
        if(Objects.isNull(firstColumnRangeAddress)) {
            return;
        }

        int firstRow = firstColumnRangeAddress.getFirstRow();
        Optional<CellRangeAddress> perColumnRange = list.stream().filter(e -> compareColAndRowRemove(e, curCell, firstRow)).findFirst();
        perColumnRange.ifPresent(cellRangeAddress -> sheet.removeMergedRegion(list.indexOf(cellRangeAddress)));
        curCell.setCellType(CellType.BLANK);
        this.addMergedRegion(sheet, new CellRangeAddress(firstRow, curCell.getRowIndex(), curCell.getColumnIndex(), curCell.getColumnIndex()));
    }

    /**
     * 行单元格合并
     */
    private void mergeRows(Sheet sheet, Cell preCell, Cell curCell) {
        Object preCellValue = getCellValue(preCell);
        Object curCellValue = getCellValue(curCell);
        if(Objects.isNull(curCellValue)) {
            return;
        }

        // 1.前一个单元格不是空，并且等于当前单元格值，创建一个合并单元格
        if(!StringUtils.EMPTY.equals(preCellValue)) {
            if(!preCellValue.equals(curCellValue)) {
                return;
            }
            curCell.setCellType(CellType.BLANK);
            this.addMergedRegion(sheet, new CellRangeAddress(preCell.getRowIndex(), curCell.getRowIndex(), preCell.getColumnIndex(), curCell.getColumnIndex()));
            return;
        }

        // 2.前一个单元格是空的，找到前一个单元格是否存在合并单元格
        List<CellRangeAddress> list = sheet.getMergedRegions();
        CellRangeAddress rangeAddress = list.stream().filter(e -> compareColAndRow(e, preCell)).findFirst().orElse(null);
        // 2.1 不存在，则判断当前单元格是不是也是空值，是就合并为一个单元格，不是不处理
        if(Objects.isNull(rangeAddress)) {
            if(StringUtils.EMPTY.equals(curCellValue)) {
                curCell.setCellType(CellType.BLANK);
                this.addMergedRegion(sheet, new CellRangeAddress(preCell.getRowIndex(), curCell.getRowIndex(), preCell.getColumnIndex(), curCell.getColumnIndex()));
            }
            return;
        }

        // 2.2 存在，则获取前一个单元格合并单元格的值（都是第一行第一列那个值，其他位置都是填充空值）判断是否相等，不相等直接返回。
        int firstRow    = rangeAddress.getFirstRow();
        int firstColumn = rangeAddress.getFirstColumn();
        String value = String.valueOf(getCellValue(sheet.getRow(firstRow).getCell(firstColumn)));
        if(!value.equals(curCellValue)) {
            return;
        }

        // 3.前一个单元格合并单元格的值与当前单元格相等，删除之前定义的合并单元格，重新定义合并单元格范围
        int lastRow    = curCell.getRowIndex();
        int lastColumn = curCell.getColumnIndex();
        for (int i = 0; i < list.size(); i++) {
            if(rangeAddress.equals(list.get(i))) {
                sheet.removeMergedRegion(i);
                curCell.setCellType(CellType.BLANK);
                this.addMergedRegion(sheet, new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));
                return;
            }
        }
    }

    /**
     * 自定义合并单元格，代替官方的
     * 官方的接口{@link Sheet#addMergedRegion(CellRangeAddress)}好多校验，性能很慢
     */
    private void addMergedRegion(Sheet sheet, CellRangeAddress cellRangeAddress) {
        CTMergeCells ctMergeCells;
        SXSSFWorkbook workbook = (SXSSFWorkbook) sheet.getWorkbook();
        CTWorksheet   sheetX   = workbook.getXSSFWorkbook().getSheet(sheet.getSheetName()).getCTWorksheet();
        if (sheetX.isSetMergeCells()) {
            ctMergeCells = sheetX.getMergeCells();
        } else {
            ctMergeCells = sheetX.addNewMergeCells();
        }

        CTMergeCell ctMergeCell = ctMergeCells.addNewMergeCell();
        ctMergeCell.setRef(cellRangeAddress.formatAsString());
    }

    private static boolean compareColAndRow(CellRangeAddress cellRangeAddress, Cell cell) {
        return cellRangeAddress.getFirstColumn() <= cell.getColumnIndex() && cellRangeAddress.getLastColumn() >= cell.getColumnIndex()
                && cellRangeAddress.getFirstRow() <= cell.getRowIndex() && cellRangeAddress.getLastRow() >= cell.getRowIndex();
    }

    private static boolean compareFirstColAndRow(CellRangeAddress cellRangeAddress, Cell cell) {
        return cellRangeAddress.getFirstColumn() == 0
                && cellRangeAddress.getFirstRow() <= cell.getRowIndex() && cellRangeAddress.getLastRow() >= cell.getRowIndex();
    }

    private static boolean compareColAndRowRemove(CellRangeAddress cellRangeAddress, Cell cell, int firstRow) {
        return cellRangeAddress.getFirstColumn() == cell.getColumnIndex()
                && cellRangeAddress.getFirstRow() <= cell.getRowIndex() && cellRangeAddress.getLastRow() >= firstRow;
    }

    /**
     * 获取单元格的值
     */
    protected static Object getCellValue(Cell cell) {
        if (Objects.isNull(cell)) {
            return StringUtils.EMPTY;
        }

        CellType cellTypeEnum = cell.getCellTypeEnum();
        switch (cellTypeEnum) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            default:
                return StringUtils.EMPTY;
        }
    }

}
