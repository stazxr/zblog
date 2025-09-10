package com.github.stazxr.zblog.bas.validation.ext;

import com.github.stazxr.zblog.bas.validation.ext.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 *
 * @author SunTao
 * @since 2025-08-01
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    /**
     * 手机号正则
     */
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.matches(PHONE_REGEX);
    }
}