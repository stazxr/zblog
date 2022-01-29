package com.github.stazxr.zblog.util;

import java.util.UUID;

/**
 * 生成UUID工具类
 *
 * @author SunTao
 * @since 2021-05-16
 */
public class UuidUtils {
    private static final int SHORT_UUID_LENGTH = 16;

    private static final String[] CHARS = {"a", "b", "c", "d", "e", "f",
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
        "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z"
    };

    public static String uuid() {
        String str = UUID.randomUUID().toString();
        return str.replace("-", "");
    }

    public static String generateShortUuid() {
        StringBuilder shortBuffer = new StringBuilder();

        // uuid的长度为32
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < SHORT_UUID_LENGTH; i++) {
            // uuid.length / 16 = 2
            String str = uuid.substring(i * 2, i * 2 + 2);

            // 0x3E为CHARS的长度
            shortBuffer.append(CHARS[Integer.parseInt(str, 16) % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
