package com.github.stazxr.zblog.util.math;

import java.math.BigInteger;

/**
 * BigIntegerLongConversion is a utility class for converting between BigInteger and long.
 *
 * @author SunTao
 * @since 2024-05-26
 */
public class BigIntegerLongConversion {
    /**
     * Converts a BigInteger to a long.
     * Throws ArithmeticException if the BigInteger value is out of the range of long.
     *
     * @param bigInteger BigInteger to convert, must not be null
     * @return long value after conversion
     * @throws ArithmeticException if the BigInteger value is out of the range of long
     * @throws IllegalArgumentException if bigInteger is null
     */
    public static long bigIntegerToLong(BigInteger bigInteger) {
        if (bigInteger == null) {
            throw new IllegalArgumentException("BigInteger must not be null");
        }
        if (bigInteger.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0 ||
                bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            throw new ArithmeticException("BigInteger out of long range");
        }
        return bigInteger.longValue();
    }

    /**
     * Converts a long to a BigInteger.
     *
     * @param value long value to convert
     * @return BigInteger after conversion
     */
    public static BigInteger longToBigInteger(long value) {
        return BigInteger.valueOf(value);
    }
}
