package com.zzc.easyExcel.annotation;


import com.zzc.easyExcel.strategy.MarketTradeMergeStrategy;

import java.lang.annotation.*;

/**
 * 合并单元格
 *
 *  使用该注解需配套写自己的merge策略，参考{@link MarketTradeMergeStrategy}
 *
 * @author Administrator
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelFieldMerge {
}
