package com.github.stazxr.zblog.bas.mask.core;

import com.github.stazxr.zblog.bas.mask.MaskType;

import java.lang.annotation.*;

/**
 * Annotation for field masking.

 * <p>This annotation marks fields that need to be masked.
 *
 * @author SunTao
 * @since 2024-05-15
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMask {
    /**
     * Masking type.
     * Defaults to first letter mask.
     *
     * @return MaskType enum representing the type of masking
     */
    MaskType type() default MaskType.FIRST_MASK;
}
