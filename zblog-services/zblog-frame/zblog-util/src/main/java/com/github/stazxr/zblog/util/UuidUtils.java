package com.github.stazxr.zblog.util;

import java.util.UUID;

/**
 * 生成UUID工具类
 *
 * @author SunTao
 * @since 2021-05-16
 */
public class UuidUtils {
    private static final int SHORT_UUID_LENGTH = 8;

    private static final int MIDDLE_UUID_LENGTH = 16;

    private static final String[] CHARS = {"a", "b", "c", "d", "e", "f",
            "g", "h", "h", "j", "k", "k", "m", "n", "n", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "z", "z", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "H",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"
    };

    /**
     * Generates the UUID version number.
     *
     * @return UUID version
     */
    public static int genUuidVer() {
        return UUID.randomUUID().version();
    }

    /**
     * Generates a standard UUID string (32 characters without dashes).
     *
     * @return UUID string
     */
    public static String genUuidStr() {
        String str = UUID.randomUUID().toString();
        return str.replace("-", "");
    }

    /**
     * Generates an 8-character UUID string using a predefined character set.
     *
     * @return 8-character UUID string
     */
    public static String gen8BitUuidStr() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < SHORT_UUID_LENGTH; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            shortBuffer.append(CHARS[Integer.parseInt(str, 16) % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * Generates a 16-character UUID string using a predefined character set.
     *
     * @return 16-character UUID string
     */
    public static String gen16BitUuidStr() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < MIDDLE_UUID_LENGTH; i++) {
            String str = uuid.substring(i * 2, i * 2 + 2);
            shortBuffer.append(CHARS[Integer.parseInt(str, 16) % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
