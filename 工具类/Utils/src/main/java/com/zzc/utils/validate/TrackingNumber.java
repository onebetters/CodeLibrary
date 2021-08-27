package com.zzc.utils.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 是否合法的快递单号
 * @author Administrator
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Constraint(validatedBy = {TrackingNumberConstraintValidator.class})
public @interface TrackingNumber {

    String message() default "快递单号不合法，必须为长度不少于6位的数字或字母组合";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
