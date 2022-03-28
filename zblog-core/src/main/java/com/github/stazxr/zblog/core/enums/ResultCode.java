package com.github.stazxr.zblog.core.enums;

/**
 * 自定义返回码，只有 {code == 1} 代表是成功的返回
 *
 * @author SunTao
 * @since 2020-11-16
 */
public enum ResultCode {
    /**
     * 系统发生了业务操作失败
     */
    FAILED(0, "操作失败"),

    /**
     * 唯一的一个成功的编码
     */
    SUCCESS(1, "操作成功"),

    /**
     * 系统发生了不可预料的未知的错误，空指针，参数解析错误，JSON解析错误等
     */
    SERVER_ERROR(10000, "系统发生了未知错误"),

    /**
     * 系统发生了业务操作异常
     */
    SERVER_EXP(10001, "系统异常"),

    /**
     * 系统发生了临时性的异常，可后续自动恢复的异常
     */
    SERVER_BUSY(10002, "系统繁忙，请稍后重试"),

    /**
     * 系统ID获取接口发生异常
     */
    ID_EXCEPTION(10003, "ID服务异常，请联系管理员"),

    /**
     * 新增或修改的数据已经存在，用户名，邮箱等业务数据
     */
    DATA_EXIST(10004, "数据已存在"),

    /**
     * 空指针异常，或者业务所需的关键性数据不存在
     */
    NULL_POINT(10005, "数据不存在"),

    /**
     * 访问资源不存在
     */
    NOT_FOUND(10006, "资源不存在"),

    /**
     * 数据不允许做删除操作
     */
    CANNOT_DELETE(10007, "数据被使用，无法删除"),

    /**
     * 登录认证失败
     */
    LOGIN_FAILED(10008, "登录失败"),

    /**
     * 令牌认证失败
     */
    VALID_TOKEN(10009, "令牌认证失败"),

    /**
     * 没有权限操作
     */
    NO_PERMISSION(10010, "没有权限"),

    /**
     * 新密码不能与前两次使用过的密码相同
     */
    PASSWORD_IS_OLD_SAME(10011, "新密码不能与前两次使用过的密码相同"),

    /**
     * 新旧密码不能一致
     */
    PASSWORD_IS_SAME(10012, "新密码与旧密码不能相同"),

    /**
     * 两次新密码设置不相同
     */
    PASSWORD_IS_DIFFERENT(10013, "两次新密码设置不相同"),

    /**
     * 密码复杂度太低
     */
    PASSWORD_IS_SIMPLE(10014, "密码复杂度太低"),

    /**
     * 密码不能包含用户名，姓名，昵称，手机号，邮箱等个人信息
     */
    PASSWORD_IS_VALID(10015, "密码不能包含用户名，姓名，手机号，邮箱等个人信息"),

    /**
     * 旧密码错误
     */
    OLD_PASSWORD_IS_ERROR(100016, "旧密码错误"),

    /**
     * 非法的参数
     */
    PARAM_INVALID(10017, "非法的参数"),

    /**
     * 密码已过期，请修改密码
     */
    PASSWORD_EXPIRED(10018, "密码已过期，请修改密码");

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
