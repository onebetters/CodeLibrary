package com.zzc.utils.validate;

import javax.annotation.Nullable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Administrator
 */
public class NotEqualsConstraintValidator implements ConstraintValidator<NotEquals, CharSequence> {

    private CharSequence rejectValue;

    @Override
    public void initialize(NotEquals annotation) {
        this.rejectValue = annotation.value();
    }

    @Override
    public boolean isValid(@Nullable CharSequence value, ConstraintValidatorContext context) {
        return Objects.isNull(value) || !Objects.equals(value.toString(), rejectValue.toString());
    }
}
