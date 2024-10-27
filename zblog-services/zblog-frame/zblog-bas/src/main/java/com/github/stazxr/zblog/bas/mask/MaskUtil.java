package com.github.stazxr.zblog.bas.mask;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.bas.mask.core.MaskStrategy;
import com.github.stazxr.zblog.bas.mask.filter.MaskFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for data masking.
 *
 * @author SunTao
 * @since 2024-05-15
 */
@Slf4j
public class MaskUtil {
    private static final MaskFilter MASK_FILTER = new MaskFilter();

    private static final String MASK_LOG_LABEL = "[ZMAS]:mask data error";

    /**
     * Masking data.
     *
     * @param data Data to be masked
     * @return Masked string
     */
    public static String toMaskString(Object data) {
        try {
            return JSON.toJSONString(data, MASK_FILTER);
        } catch (Exception e) {
            log.error(MASK_LOG_LABEL, e);
            return JSON.toJSONString(data);
        }
    }

    /**
     * String masking.
     *
     * @param cha  String to be masked
     * @param type Mask type
     * @return Masked string
     */
    public static String desensitized(CharSequence cha, MaskType type) {
        return type.mask(String.valueOf(cha));
    }

    /**
     * String masking using custom strategy.
     *
     * @param cha      String to be masked
     * @param strategy Masking strategy
     * @return Masked string
     */
    public static String desensitized(CharSequence cha, MaskStrategy strategy) {
        return strategy.mask(String.valueOf(cha));
    }
}
