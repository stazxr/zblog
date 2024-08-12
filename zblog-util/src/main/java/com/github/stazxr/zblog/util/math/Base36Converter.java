package com.github.stazxr.zblog.util.math;

import java.math.BigInteger;

/**
 * Base36Converter is a utility class for converting between Base36 strings and decimal numbers.
 * Base36 consists of digits 0-9 and letters A-Z, totaling 36 characters.
 *
 * @author SunTao
 * @since 2024-05-26
 */
public class Base36Converter {
    /**
     * Converts a Base36 string to a decimal BigInteger.
     *
     * @param base36 Base36 string, must not be null or empty
     * @return corresponding decimal BigInteger
     * @throws NumberFormatException if base36 is not a valid Base36 string
     */
    public static BigInteger base36ToDecimal(String base36) {
        if (base36 == null || base36.isEmpty()) {
            throw new IllegalArgumentException("Input string must not be null or empty");
        }
        return new BigInteger(base36, 36);
    }

    /**
     * Converts a decimal BigInteger to a Base36 string.
     *
     * @param decimal decimal BigInteger, must not be null
     * @return corresponding Base36 string, using uppercase letters
     * @throws IllegalArgumentException if decimal is null
     */
    public static String decimalToBase36(BigInteger decimal) {
        if (decimal == null) {
            throw new IllegalArgumentException("Input decimal must not be null");
        }
        return decimal.toString(36).toUpperCase();
    }
}
