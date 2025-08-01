package com.github.stazxr.zblog.bas.validate.annotation;

import com.github.stazxr.zblog.bas.validate.ext.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *
 *
 * @author SunTao
 * @since 2025-08-01
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "{phone.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
