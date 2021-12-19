package com.github.stazxr.zblog.core.model;

import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.core.enums.ResultCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义Response返回体
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Getter
@NoArgsConstructor
public class Result implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7847907472897585204L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    private Result(ResultCode resultCode) {
        code = resultCode.code();
        message = resultCode.message();
    }

    private Result(ResultCode resultCode, String message) {
        code = resultCode.code();
        this.message = message;
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result success(String message) {
        return new Result(ResultCode.SUCCESS, message);
    }

    public static Result failure() {
        return new Result(ResultCode.FAILED);
    }

    public static Result failure(ResultCode resultCode) {
        return new Result(resultCode);
    }

    public static Result failure(String message) {
        return new Result(ResultCode.FAILED, message);
    }

    public static Result failure(ResultCode resultCode, String message) {
        return new Result(resultCode, message);
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
