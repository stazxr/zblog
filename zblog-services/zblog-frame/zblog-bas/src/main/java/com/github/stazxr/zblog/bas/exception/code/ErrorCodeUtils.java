package com.github.stazxr.zblog.bas.exception.code;

/**
 * 错误码工具类
 *
 * @author SunTao
 * @since 2026-02-02
 */
public class ErrorCodeUtils {
    public static ErrorCode of(String message) {
        return new ErrorCode() {
            @Override
            public String getCode() {
                return message;
            }
            @Override
            public String getI18nKey() {
                return message;
            }
        };
    }

    public static ErrorCode of(String code, String message) {
        return new ErrorCode() {
            @Override
            public String getCode() {
                return code;
            }
            @Override
            public String getI18nKey() {
                return message;
            }
        };
    }
}
