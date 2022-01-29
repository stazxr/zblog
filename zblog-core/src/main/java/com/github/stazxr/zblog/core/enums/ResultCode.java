package com.github.stazxr.zblog.core.enums;

/**
 * 自定义返回码
 *
 * @author SunTao
 * @since 2020-11-16
 */
public enum ResultCode {
    FAILED(0, "操作失败"),
    SUCCESS(1, "操作成功"),
    LOGIN_SUCCESS(2, "登录成功"),
    LOGOUT_SUCCESS(3, "登出成功"),
    SERVER_ERROR(10000, "系统错误"),
    SERVER_EXP(10001, "系统异常"),
    SERVER_BUSY(10002, "系统繁忙，请稍后重试"),
    ID_EXCEPTION(10003, "ID服务异常"),
    DATA_EXIST(10004, "数据已存在"),
    NULL_POINT(10005, "数据不存在"),
    NOT_FOUND(10006, "资源不存在"),
    CANNOT_DELETE(10007, "数据被使用，无法删除"),
    AUTH_FAILED(10008, "认证失败"),
    NO_PERMISSION(10009, "没有权限"),
    PASSWORD_IS_SAME(10012, "新密码与旧密码不能相同"),
    PASSWORD_IS_DIFFERENT(10013, "两次新密码设置不相同"),
    PASSWORD_IS_SIMPLE(10014, "密码复杂度太低"),
    PASSWORD_IS_VALID(10015, "密码不能包含用户名，姓名，手机号，邮箱等个人信息"),
    OLD_PASSWORD_IS_ERROR(100016, "旧密码错误"),
    PARAM_INVALID(10017, "参数错误");

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
