package com.github.stazxr.zblog.core.model;

import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.core.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 自定义Response返回体
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Getter
@ApiModel("统一返回")
public final class Result implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7847907472897585204L;

    /**
     * Http 响应码
     */
    @ApiModelProperty(name = "code", value = "响应码", example = "200")
    private Integer code;

    /**
     * 响应信息
     */
    @ApiModelProperty(name = "message", value = "响应信息", example = "操作成功")
    private final String message;

    /**
     * 响应数据
     */
    @ApiModelProperty(name = "data", value = "响应数据")
    private Object data;

    /**
     * 业务响应码（在某些特殊场景，做更细致的业务划分）
     */
    @ApiModelProperty(name = "identifier", value = "业务码", example = "1")
    private final Integer identifier;

    /**
     * 时间戳
     */
    @ApiModelProperty(name = "identifier", value = "响应时间", example = "1996-03-01 00:04:10")
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private Result(ResultCode resultCode) {
        this(resultCode.code(), resultCode.message());
    }

    private Result(ResultCode resultCode, String message) {
        this(resultCode.code(), message);
    }

    private Result(Integer identifier, String message) {
        this.identifier = identifier;
        this.message = message;
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS).code(HttpStatus.OK);
    }

    public static Result success(ResultCode resultCode) {
        return new Result(resultCode).code(HttpStatus.OK);
    }

    public static Result success(String message) {
        return new Result(ResultCode.SUCCESS, message).code(HttpStatus.OK);
    }

    public static Result failure() {
        return new Result(ResultCode.FAILED).code(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result failure(ResultCode resultCode) {
        return new Result(resultCode).code(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result failure(String message) {
        return new Result(ResultCode.FAILED, message).code(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result failure(ResultCode resultCode, String message) {
        return new Result(resultCode, message).code(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result failure(Integer identifier, String message) {
        return new Result(identifier, message).code(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 设置响应码
     *
     * @param status HttpStatus
     * @return Result
     */
    public Result code(HttpStatus status) {
        this.code = status.value();
        return this;
    }

    /**
     * 设置返回数据
     *
     * @param data 返回数据
     * @return this + data
     */
    public Result data(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
