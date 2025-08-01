package com.github.stazxr.zblog.bas.msg;

/**
 * 自定义返回码，只有 { code == 1 } 代表是成功的返回
 *
 * @author SunTao
 * @since 2025-08-01
 */
public enum ResultCode {
    /**
     * 操作失败
     */
    FAILED(0, "操作失败"),

    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),

    /**
     * 数据校验失败
     */
    VALIDATE_FAILED(0, "数据校验失败，请检查上送参数");

    private final Integer code;

    private final String defaultMsg;

    ResultCode(Integer code, String defaultMsg) {
        this.code = code;
        this.defaultMsg = defaultMsg;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.defaultMsg;
    }
}
