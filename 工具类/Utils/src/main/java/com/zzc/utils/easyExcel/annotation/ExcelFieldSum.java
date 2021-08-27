package com.zzc.utils.easyExcel.annotation;


import com.zzc.utils.easyExcel.handler.FieldSumWriteHandler;

import java.lang.annotation.*;

/**
 * 指定字段求和
 *
 * 使用事项：
 *     1.求和字段不要放在第一列
 *     2.使用时需注入{@link FieldSumWriteHandler}即可，不注入不生效。
 *     例如：EasyExcel.writerSheet().registerWriteHandler(new FieldSumWriteHandler(xxx.class)).build();
 *
 * @author Administrator
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelFieldSum {
}
