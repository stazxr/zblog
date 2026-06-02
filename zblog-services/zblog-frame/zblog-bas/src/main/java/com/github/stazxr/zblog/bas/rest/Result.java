package com.github.stazxr.zblog.bas.rest;

import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import lombok.Getter;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 框架级统一响应体
 *
 * @param <T> 返回数据类型
 * @author SunTao
 * @since 2026-01-25
 */
@Getter
public final class Result<T> implements Serializable {
    private static final long serialVersionUID = -7847907472897585204L;

    /**
     * 成功响应码
     */
    public static final String SUCCESS_CODE = "000000000";

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应类型
     */
    private String type;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 链路追踪ID
     */
    private final String traceId;

    /**
     * 当前处理请求的服务实例
     */
    private final String instanceId;

    /**
     * 响应时间戳（毫秒）
     */
    private final long timestamp;

    private Result() {
        this.timestamp = System.currentTimeMillis();
        this.traceId = MDC.get("traceId");
        this.instanceId = MDC.get("deployCode");
    }

    private Result(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public Result<T> code(String code) {
        this.code = code;
        return this;
    }

    public Result<T> type(String type) {
        this.type = type;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> Result<T> success() {
        String successMessage = I18nUtils.getMessage("result.success");
        return new Result<T>(SUCCESS_CODE, successMessage).type(ResultType.SUCCESS);
    }

    public static <T> Result<T> success(String message) {
        return new Result<T>(SUCCESS_CODE, message).type(ResultType.SUCCESS);
    }

    public static <T> Result<T> failure() {
        String failureMessage = I18nUtils.getMessage("result.failure");
        return new Result<>(CommonErrorCode.SBASEA000.getCode(), failureMessage);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(CommonErrorCode.SBASEA000.getCode(), message);
    }

    public static <T> Result<T> failure(String code, String message) {
        return new Result<>(code, message);
    }
}
