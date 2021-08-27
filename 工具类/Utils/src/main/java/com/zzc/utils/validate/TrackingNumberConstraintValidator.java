package com.zzc.utils.validate;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Administrator
 */
public class TrackingNumberConstraintValidator implements ConstraintValidator<TrackingNumber, CharSequence> {

    @Override
    public void initialize(TrackingNumber annotation) {
    }

    @Override
    public boolean isValid(@Nullable CharSequence value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value)) {
            final String trackingNumber = StringUtils.trim(value.toString());
            if (StringUtils.length(trackingNumber) < 6) {
                return false;
            }
            return StringUtils.isAlphanumeric(trackingNumber);
        }
        return true;
    }
}