package com.github.stazxr.zblog.bas.exception.code;

/**
 * 公共错误码定义。
 * <p>
 * 适用于全局通用的技术异常（不可预期、非业务语义错误），通常由系统运行时、基础设施或第三方依赖引起。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public enum CommonErrorCode implements ErrorCode {
    /**
     * 系统未知错误
     * <p>
     * 兜底错误码，适用于无法明确分类的系统异常。
     */
    SBASEA000("error.system.unknown"),

    /**
     * 系统繁忙
     * <p>
     * 常见于并发过高、线程池耗尽、限流触发等场景。
     */
    SBASEA001("error.system.busy"),

    /**
     * 服务暂不可用
     * <p>
     * 通常表示下游服务不可用或系统处于维护状态。
     */
    SBASEA002("error.system.unavailable"),

    /**
     * 系统内部错误
     * <p>
     * 用于明确识别的系统异常，如 NPE、数据库异常等。
     */
    SBASEA003("error.system.internal"),

    /**
     * 请求超时
     * <p>
     * 包括接口调用超时、RPC 超时、数据库查询超时等。
     */
    SBASEA004("error.system.timeout"),
    ;

    private final String i18nKey;

    CommonErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码，唯一且符合规范
     *
     * @return 错误码字符串
     */
    @Override
    public String getCode() {
        return name();
    }

    /**
     * 国际化 key，用于获取对应消息
     *
     * @return i18n key
     */
    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}
