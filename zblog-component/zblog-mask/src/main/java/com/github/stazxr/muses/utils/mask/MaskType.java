package com.github.stazxr.muses.utils.mask;

import com.github.stazxr.muses.utils.mask.constants.MaskConstants;
import com.github.stazxr.muses.utils.mask.core.MaskStrategy;
import com.github.stazxr.zblog.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enumeration defining common data masking types and their rules for sensitive data handling.
 *
 * @author SunTao
 * @since 2024-05-15
 */
public enum MaskType implements MaskStrategy {
    /**
     * Masking the first character. Rule: If the string length is >= 1, mask the first character.
     * Example: ABC => *BC
     */
    FIRST_MASK(StringUtils::hideFirstChar),

    /**
     * Password masking. Rule: 123456 => ******; 123456789 => ******
     */
    PASSWORD(data -> MaskConstants.PASSWORD_MASK),

    /**
     * Mobile number masking. Rule: Hide the middle four digits of the mobile number.
     */
    MOBILE_NUMBER(data -> {
        if (data.length() == MaskConstants.MOBILE_NUMBER_LEN) {
            // Standard mobile number
            return data.substring(0, 3) + MaskConstants.OTHER_MASK + data.substring(7);
        } else {
            // Non-standard mobile number
            return StringUtils.hideFirstChar(data);
        }
    }),

    /**
     * Landline number masking. Rule: Hide middle part or suffix.
     */
    LANDLINE_NUMBER(data -> {
        int landlineNumPart = 3;
        int minLandlineNumLen = 6;
        if (data.length() > minLandlineNumLen) {
            // Standard landline number format like 000-000-0000 or 0000000000
            String[] landParts = data.split("-");
            if (landParts.length == landlineNumPart) {
                return landParts[0] + "-" + MaskConstants.OTHER_MASK + "-" + landParts[2];
            } else {
                return data.substring(0, data.length() - 4) + MaskConstants.OTHER_MASK;
            }
        } else {
            // Non-standard landline number
            return StringUtils.hideFirstChar(data);
        }
    }),

    /**
     * Username masking. Rule: Hide middle part.
     */
    USERNAME(data -> {
        int minUsernameLen = 3;
        if (data.length() > minUsernameLen) {
            return data.substring(0, 2) + MaskConstants.OTHER_MASK + data.substring(data.length() - 1);
        } else {
            return StringUtils.hideFirstChar(data);
        }
    }),

    /**
     * Email masking (strong). Rule: Hide username part.
     */
    EMAIL(data -> {
        int atIndex = data.indexOf("@");
        if (atIndex != -1) {
            // Standard email format
            return MaskConstants.OTHER_MASK + data.substring(atIndex);
        } else {
            // Invalid email address, no need to mask
            return data;
        }
    }),

    /**
     * Email masking (weak). Rule: Secondary masking of username part.
     */
    EMAIL_WEAK(data -> {
        int atIndex = data.indexOf("@");
        if (atIndex != -1) {
            // Standard email format
            String maskUsername = USERNAME.applyMask(data.substring(0, atIndex));
            return maskUsername + data.substring(atIndex);
        } else {
            // Invalid email address, no need to mask
            return data;
        }
    }),

    /**
     * Address masking. Rule: Not yet implemented.
     */
    ADDRESS(data -> {
        // TODO: Not implemented yet
        return data;
    }),

    /**
     * ID card number masking. Rule: Mask personal information in standard ID card numbers.
     */
    ID_CARD(data -> {
        if (data.length() == MaskConstants.ID_CARD_LEN) {
            return data.substring(0, 6) + MaskConstants.ID_CARD_MASK + data.substring(14);
        }
        return data;
    }),

    /**
     * QQ number masking. Rule: Mask middle part or last few digits.
     */
    QQ_NUMBER(data -> {
        if (data.length() >= MaskConstants.MIN_QQ_LEN) {
            return data.substring(0, 2) + MaskConstants.OTHER_MASK + data.substring(data.length() - 2);
        }
        return StringUtils.hideFirstChar(data);
    });

    private static final Logger log = LoggerFactory.getLogger(MaskType.class);

    private final MaskStrategy strategy;

    /**
     * Constructs a masking type enum object.
     *
     * @param strategy Masking strategy
     */
    MaskType(MaskStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Applies masking operation to given data.
     *
     * @param data Data to be masked
     * @return Masked value
     */
    @Override
    public String applyMask(String data) {
        try {
            return strategy.applyMask(data);
        } catch (Exception e) {
            log.error("Data mask error", e);
            return data;
        }
    }
}
