package com.github.stazxr.zblog.bas.validate;

import javax.validation.*;
import java.util.Set;

/**
 *
 *
 * @author SunTao
 * @since 2025-08-01
 */
public class ValidatorUtil {
    public static <T> void validate(T obj, Class<?>... groups) {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();) {
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(obj, groups);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<T> violation : violations) {
                    sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
                }
                throw new ConstraintViolationException("参数校验失败: " + sb.toString(), violations);
            }

        }
    }
}