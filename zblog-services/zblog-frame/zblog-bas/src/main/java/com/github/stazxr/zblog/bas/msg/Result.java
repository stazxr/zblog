package com.github.stazxr.zblog.bas.msg;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通用响应类，用于返回 API 调用结果。
 * <p>包含标准的响应状态码、消息、数据和时间戳。</p>
 *
 * @author SunTao
 * @since 2024-11-12
 */
@Getter
public final class Result implements Serializable {
    private static final long serialVersionUID = -7847907472897585204L;

    /**
     * 默认的成功消息
     */
    private static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";

    /**
     * 默认的失败消息
     */
    private static final String DEFAULT_FAILED_MESSAGE = "操作失败";

    /**
     * 成功标识符
     */
    private static final int SUCCESS_IDENTIFIER = 0;

    /**
     * 失败标识符
     */
    private static final int FAILED_IDENTIFIER = 1;

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应消息
     */
    private final String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 业务码，默认情况下，0成功，1失败
     */
    private final int identifier;

    /**
     * 响应时间戳
     */
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private Result(int identifier, String message) {
        this.identifier = identifier;
        this.message = message;
    }

    /**
     * 成功响应，使用默认成功消息。
     *
     * @param data 返回数据
     * @return Result 实例，包含成功标识和消息
     */
    public static Result s(Object data) {
        return success().data(data);
    }

    /**
     * 成功响应，使用默认成功消息。
     *
     * @return Result 实例，包含成功标识和消息
     */
    public static Result success() {
        return success(SUCCESS_IDENTIFIER, DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 成功响应，指定成功消息。
     *
     * @param message 成功消息
     * @return Result 实例，包含成功标识和消息
     */
    public static Result success(String message) {
        return success(SUCCESS_IDENTIFIER, message);
    }

    /**
     * 成功响应，指定标识符和消息。
     *
     * @param identifier 标识符
     * @param message 成功消息
     * @return Result 实例，包含成功标识和消息
     */
    public static Result success(int identifier, String message) {
        return new Result(identifier, message).code(HttpStatus.OK);
    }

    /**
     * 失败响应，使用默认失败消息。
     *
     * @return Result 实例，包含失败标识和消息
     */
    public static Result failure() {
        return failure(FAILED_IDENTIFIER, DEFAULT_FAILED_MESSAGE);
    }

    /**
     * 失败响应，指定失败消息。
     *
     * @param message 失败消息
     * @return Result 实例，包含失败标识和消息
     */
    public static Result failure(String message) {
        return failure(FAILED_IDENTIFIER, message);
    }

    /**
     * 失败响应，指定标识符和消息。
     *
     * @param identifier 标识符
     * @param message 失败消息
     * @return Result 实例，包含失败标识和消息
     */
    public static Result failure(int identifier, String message) {
        return new Result(identifier, message).code(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 设置 HTTP 状态码。
     *
     * @param status HTTP 状态
     * @return 当前 Result 实例
     */
    public Result code(HttpStatus status) {
        this.code = status.value();
        return this;
    }

    /**
     * 设置返回数据。
     *
     * @param data 返回的数据
     * @return 当前 Result 实例
     */
    public Result data(Object data) {
        this.data = data;
        return this;
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
