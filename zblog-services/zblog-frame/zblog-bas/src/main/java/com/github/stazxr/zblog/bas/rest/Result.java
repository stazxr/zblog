package com.github.stazxr.zblog.bas.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import lombok.Getter;
import org.slf4j.MDC;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * 响应结果
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 链路追踪ID
     */
    private final String traceId;

    /**
     * 当前处理请求的服务实例
     */
    private final String instanceId;

    /**
     * 响应时间戳
     */
    private final String timestamp;

    private Result() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.traceId = MDC.get("traceId");
        this.instanceId = MDC.get("deployCode");
    }

    private Result(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    public Result<T> success(boolean success) {
        this.success = success;
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

    public Result<T> errorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public static <T> Result<T> success() {
        String successMessage = I18nUtils.getMessage("result.success");
        return new Result<>(true, successMessage);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(true, message);
    }

    public static <T> Result<T> failure() {
        String failureMessage = I18nUtils.getMessage("result.failure");
        return new Result<>(false, failureMessage);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(false, message);
    }

    public static <T> Result<T> failure(String errorCode, String message) {
        return new Result<T>(false, message).errorCode(errorCode);
    }

    /**
     * 重写 toString 方法，将 Result 转换为 JSON 格式字符串。
     *
     * @return JSON 格式的 Result 字符串表示
     */
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
