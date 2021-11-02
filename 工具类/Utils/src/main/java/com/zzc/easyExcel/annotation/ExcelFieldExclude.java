package com.zzc.easyExcel.annotation;


import com.zzc.easyExcel.handler.FieldExcludeWriteHandler;

import java.lang.annotation.*;

/**
 * 根据业务需求，有的时候不需要导出一些字段，可以使用此注解
 *
 * 使用事项：
 *      使用者需自己根据业务判断，然后注入{@link FieldExcludeWriteHandler}即可，不注入不生效。
 *      例如：
 *          WriteSheet writeSheet = EasyExcel.writerSheet().build();
 *          //判断满足自己条件后，注册FieldExcludeWriteHandler
 *          writeSheet.getCustomWriteHandlerList().add(new FieldExcludeWriteHandler(xxx.class));
 *
 * @author Administrator
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelFieldExclude {
}
