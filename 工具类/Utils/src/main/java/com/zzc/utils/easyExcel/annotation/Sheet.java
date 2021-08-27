package com.zzc.utils.easyExcel.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {
	String name() default "";
}
