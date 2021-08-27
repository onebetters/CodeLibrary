package com.zzc.utils.easyExcel.handler;

import com.alibaba.excel.converters.bigdecimal.BigDecimalNumberConverter;
import com.alibaba.excel.converters.string.StringNumberConverter;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.google.common.base.Objects;
import com.zzc.utils.easyExcel.AnnotationType;
import com.zzc.utils.easyExcel.CustomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 功能：导出Excel按字段求和汇总
 * 描述：EasyExcel是一行一行每一列进行写入excel的，所以提供类似RowWriteHandler、CellWriteHandler供用户自定义操作
 * 使用注意事项：
 *    1.第一列不支持汇总，需汇总字段的index应>=1，且只有cell的cellType是NUMERIC类型才支持SUM汇总；
 *    2.导出的实体类字段需支持转换为Number类型的，如果字段是String类型，可以加上Converter将类型转化为Number类型
 *      例如：{@link BigDecimalNumberConverter}、{@link StringNumberConverter}
 *    3.数据写入时，记得注册FieldSumWriteHandler。
 *      例如：EasyExcel.writerSheet().registerWriteHandler(new FieldSumWriteHandler(xxx.class)).build();
 * 缺陷Bug（在本项目中已修复）：
 *    1.测试发现，如果与@ExcelFieldMerge一起使用，即sheet中ExcelFieldMerge作用的列（存在合并单元格）求和时，会导致重复计算，
 *      使SUM汇总不准确 （其实是EasyExcel的合并单元格的缺陷Bug）
 *
 * @author Administrator
 */
@Slf4j
public class FieldSumWriteHandler implements RowWriteHandler {

    /**
     * 需求和的字段index
     */
    private final Map<Integer, Field> columnIndexMap;

    public FieldSumWriteHandler(@Nonnull final Class<?> clazz) {
        this.columnIndexMap = CustomUtils.getNeedColumnIndex(clazz, AnnotationType.SUM);
    }

    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex,
                                Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                               Row row, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                Row row, Integer relativeRowIndex, Boolean isHead) {
        // 这里可以对row进行任何操作
        if (!isHead) {
            final CellStyle cellStyle = this.buildContentCellStyle(writeSheetHolder.getSheet().getWorkbook());

            final Row nextRow = writeSheetHolder.getSheet().createRow(row.getRowNum() + 1);
            nextRow.createCell(0).setCellValue("总计");
            nextRow.getCell(0).setCellStyle(cellStyle);

            //只有一列时不建议支持汇总，需汇总字段的index应>=1，且只有字段是NUMERIC类型才会SUM汇总（排除日期类型）
            if (row.getLastCellNum() > 1) {
                for (int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++) {
                    Cell cell    = row.getCell(cellNum);
                    Cell sumCell = nextRow.createCell(cellNum);
                    if (columnIndexMap.containsKey(cell.getColumnIndex()) && Objects.equal(cell.getCellTypeEnum(), CellType.NUMERIC)
                            && !DateUtil.isCellDateFormatted(cell)) {
                        //长度转成ABC列
                        String colString = CellReference.convertNumToColString(cellNum);
                        //求和公式 （第2行开始）
                        String sumFormula = "SUM(" + colString + "2:" + colString + nextRow.getRowNum() + ")";
                        sumCell.setCellFormula(sumFormula);
                    }
                    sumCell.setCellStyle(cellStyle);
                }
            }
        }
    }

    /**
     * 单元格样式
     */
    private CellStyle buildContentCellStyle(@Nonnull Workbook workbook) {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景灰色，与默认表头一置
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        //底端对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);

        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short)12);
        contentWriteFont.setFontName("黑体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        return StyleUtil.buildContentCellStyle(workbook, contentWriteCellStyle);
    }

}
