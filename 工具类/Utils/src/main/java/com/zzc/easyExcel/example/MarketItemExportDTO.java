package com.zzc.easyExcel.example;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.zzc.easyExcel.annotation.Sheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExcelIgnoreUnannotated
@Sheet(name = "商家商品")
// 头背景设置成灰色  IndexedColors.GREY_25_PERCENT.getIndex()
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 22)
@HeadRowHeight(40)
// 头字体设置
@HeadFontStyle(fontName = "微软雅黑", fontHeightInPoints = 11)
@ContentFontStyle(fontName = "微软雅黑", fontHeightInPoints = 9)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)
@ColumnWidth(12)
public class MarketItemExportDTO implements Serializable {

	@ColumnWidth(30)
	@ContentStyle(wrapped = true)
	@ExcelProperty(value = "商品名称")
	private String skuName;

	@ExcelProperty(value = "规格")
	private String specs;

	@ColumnWidth(18)
	@ExcelProperty(value = "SKU编码")
	private String bn;

	@ExcelProperty(value = "市场分类")
	private String category;

	@ExcelProperty(value = "市场品牌")
	private String band;

	@ExcelProperty(value = "计量单位")
	private String unit;

	@ExcelProperty(value = "销售原价")
	private BigDecimal salePrice;

	@ExcelProperty(value = "建议零售价")
	private BigDecimal mktPrice;

	@ExcelProperty(value = "生效价格")
	private String priceMode;

	@ExcelProperty(value = "阶梯价混批")
	private String mixedBatch;

	@ExcelProperty(value = "级别价")
	private String levelPrice;

	@ContentStyle(wrapped = true)
	@ExcelProperty(value = "阶梯价")
	private String stepPrice;

	@ExcelProperty(value = "销量")
	private Long saleCount;

	@ExcelProperty(value = "商品佣金")
	private String commission;

	@ExcelProperty(value = "库存")
	private BigDecimal saleStock;

	@ExcelProperty(value = "所属商家")
	private String shopName;

	@ColumnWidth(18)
	@ExcelProperty(value = "发布时间")
	private Date publishTime;

	@ExcelProperty(value = "商品状态")
	private String marketItemStatus;
}
