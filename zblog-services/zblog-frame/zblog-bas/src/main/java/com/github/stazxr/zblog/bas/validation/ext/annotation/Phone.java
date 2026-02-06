package com.github.stazxr.zblog.bas.validation.ext.annotation;

import com.github.stazxr.zblog.bas.validation.ext.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机号校验注解
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
