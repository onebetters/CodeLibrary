package com.zzc.easyExcel.annotation;

import com.zzc.easyExcel.enums.CellMergeEnum;
import com.zzc.easyExcel.handler.CellMergeWriterHandler;
import com.zzc.easyExcel.strategy.MarketTradeMergeStrategy;

import java.lang.annotation.*;

/**
 * 合并单元格
 *
 * 使用事项：
 *
 * 方式一：
 *      1.可以同时支持行合并和列合并
 *      2.如果导出内容是BigDecimal类型，要转换成String，不然会当作不一样的内容不进行合并
 *        例如：@ExcelProperty(value = "销售额", converter = BigDecimalStringConverter.class)
 *      3.使用时需注入{@link CellMergeWriterHandler}即可，不注入不生效。
 *        例如：EasyExcel.writerSheet().registerWriteHandler(new CellMergeWriterHandler(xxx.class)).build();
 *
 * 方式二（推荐）： 这个要比方式一快一点，少做了很多merge操作，因为最耗时的还是POI的addMergedRegion
 *       自己写merge策略，参考{@link MarketTradeMergeStrategy}
 *
 * <p>Filename: com.qianmi.gcs.wx.purchase.infrastructure.support.utils.easyExcel.annotation.ExcelFieldMerge.java</p>
 * <p>Date: 2020-11-20 22:40.</p>
 *
 * @author <a href="mailto:zhangzhichuan@qianmi.com">OF3430-张志川</a>
 * @version 0.1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelFieldMerge {

    /**
     * 行合并 or 列合并
     */
    CellMergeEnum mergerType() default CellMergeEnum.ROW;

    /**
     * 是否左对齐 （参照第一列合并的行数，后面所有列都合并相应行）
     * 注意：只在mergerType=CellMergeEnum.ROW下才生效
     */
    boolean alignLeft() default false;
}
