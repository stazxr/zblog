package com.github.stazxr.zblog.bas.exception;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;

/**
 * 异常基类
 *
 * @author SunTao
 * @since 2022-03-09
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 8442431512167536240L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 使用自定义消息构造异常
     *
     * @param message 异常消息
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * 使用自定义消息及根因构造异常
     *
     * @param message 异常消息
     * @param cause 根因异常
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用国际化消息码构造异常
     *
     * @param expMessageCode 异常消息码
     */
    public BaseException(ExpMessageCode expMessageCode) {
        super(resolveMessageCode(expMessageCode));
        code = expMessageCode.getCode();
    }

    /**
     * 使用国际化消息码及根因构造异常
     *
     * @param expMessageCode 异常消息码
     * @param cause 根因异常
     */
    public BaseException(ExpMessageCode expMessageCode, Throwable cause) {
        super(resolveMessageCode(expMessageCode), cause);
        code = expMessageCode.getCode();
    }

    /**
     * 获取原始消息
     *
     * @return 原始异常消息
     */
    public String getRawMessage() {
        return super.getMessage();
    }

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    public String getMessage() {
        return buildMessage(super.getMessage(), this.getCause());
    }

    /**
     * 获取异常码
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取异常链中最底层的根因异常
     *
     * @return 根因异常，如果不存在返回 null
     */
    public Throwable getRootCause() {
        return getRootCause(this);
    }

    /**
     * 判断异常链中是否包含指定类型的异常
     *
     * @param exType 异常类型
     * @return 如果异常链中包含指定类型返回 true，否则返回 false
     */
    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        }

        Throwable cause = this;
        while (cause != null) {
            if (exType.isInstance(cause)) {
                return true;
            }
            if (cause.getCause() == cause) {
                break;
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 安全解析国际化消息码，保证构造异常时不会抛出异常
     */
    private static String resolveMessageCode(ExpMessageCode expMessageCode) {
        try {
            return I18nUtils.getMessage(expMessageCode.getCode(), expMessageCode.getArgs());
        } catch (Exception e) {
            // 如果国际化失败，则使用 code 本身
            return expMessageCode.getCode();
        }
    }

    /**
     * 构建消息，包含嵌套异常信息
     */
    private static String buildMessage(String message, Throwable cause) {
        if (message == null && cause == null) {
            return null;
        }

        if (message == null) {
            return cause.getMessage();
        }

        if (cause == null) {
            return message;
        }

        return message + "; nested exception is " + cause;
    }

    /**
     * 获取异常链中的根因异常
     */
    private static Throwable getRootCause(Throwable original) {
        if (original == null) {
            return null;
        }
        Throwable rootCause = original;
        Throwable cause = original.getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }
}
