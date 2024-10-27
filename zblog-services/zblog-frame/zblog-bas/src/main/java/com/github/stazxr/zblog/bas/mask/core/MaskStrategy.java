package com.github.stazxr.zblog.bas.mask.core;

import com.github.stazxr.zblog.util.StringUtils;

/**
 * Masking Strategy Interface.
 *
 * <p>This interface defines methods for data masking, allowing for custom masking logic.
 *
 * @author SunTao
 * @since 2024-05-15
 */
@FunctionalInterface
public interface MaskStrategy {
    /**
     * Default method for data masking.
     *
     * @param data The data to be masked.
     * @return The masked data.
     */
    default String mask(String data) {
        if (StringUtils.isBlank(data)) {
            return data;
        }
        return applyMask(data);
    }

    /**
     * Abstract method for applying the masking logic.
     *
     * @param data The data to be masked.
     * @return The masked data.
     */
    String applyMask(String data);
}
