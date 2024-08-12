package com.github.stazxr.zblog.mask.constants;

/**
 * Constants for data masking.
 *
 * @author SunTao
 * @since 2024-05-15
 */
public class MaskConstants {
    /**
     * Mask for passwords.
     */
    public static final String PASSWORD_MASK = "******";

    /**
     * Mask for ID cards.
     */
    public static final String ID_CARD_MASK = "********";

    /**
     * Default mask for other sensitive data.
     */
    public static final String OTHER_MASK = "****";

    /**
     * Length of mobile phone numbers.
     */
    public static final int MOBILE_NUMBER_LEN = 11;

    /**
     * Length of ID card numbers.
     */
    public static final int ID_CARD_LEN = 18;

    /**
     * Minimum length of QQ numbers.
     */
    public static final int MIN_QQ_LEN = 5;
}
