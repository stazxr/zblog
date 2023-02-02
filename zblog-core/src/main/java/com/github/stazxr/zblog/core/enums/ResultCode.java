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
     * 操作成功
     */
    SUCCESS(1, "操作成功"),

    /**
     * 注销成功
     */
    LOGOUT_SUCCESS(2, "注销成功"),

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
    ID_EXCEPTION(10003, "ID生成异常，请联系网站管理员处理！"),

    /**
     * 新增或修改的数据已经存在，序列，用户名，邮箱等业务数据
     */
    DATA_EXIST(10004, "数据已存在"),

    /**
     * 空指针异常
     */
    NULL_POINT(10005, "发生了空指针异常"),

    /**
     * 访问资源不存在
     */
    NOT_FOUND(10006, "请求资源不存在"),

    /**
     * 数据不允许做删除操作
     */
    CANNOT_DELETE(10007, "数据被使用，无法删除"),

    /**
     * 登录认证失败
     */
    LOGIN_FAILED(10008, "登录失败"),

    /**
     * 密码已过期，请修改密码
     */
    PASSWORD_EXPIRED(10009, "密码已过期，请修改密码"),

    /**
     * 没有权限操作
     */
    NO_PERMISSION(10010, "没有权限"),

    /**
     * 新密码不能与前X次使用过的密码相同
     */
    @Deprecated
    PASSWORD_IS_OLD_SAME(10011, "新密码不能与前两次使用过的密码相同"),

    /**
     * 新旧密码不能一致
     */
    @Deprecated
    PASSWORD_IS_SAME(10012, "新密码与旧密码不能相同"),

    /**
     * 两次新密码设置不相同
     */
    @Deprecated
    PASSWORD_IS_DIFFERENT(10013, "两次新密码设置不相同"),

    /**
     * 密码复杂度太低
     */
    PASSWORD_IS_SIMPLE(10014, "密码复杂度太低"),

    /**
     * 密码不能包含用户名，姓名，昵称，手机号，邮箱等个人信息
     */
    @Deprecated
    PASSWORD_IS_VALID(10015, "密码不能包含用户名，姓名，手机号，邮箱等个人信息"),

    /**
     * 旧密码错误
     */
    @Deprecated
    OLD_PASSWORD_IS_ERROR(100016, "旧密码错误"),

    /**
     * 参数错误，缺失必要的参数，use 10018
     */
    @Deprecated
    PARAM_EMPTY(10017, "参数错误，缺失必要的参数"),

    /**
     * 参数错误
     */
    PARAM_VALID(10018, "参数错误"),

    /**
     * 错误的配置信息
     */
    BAD_CONFIGURATION(10020, "错误的配置信息"),

    /**
     * 机器数超过了最大值
     */
    ID_OVER_MAX(10021, "机器数超过了最大值"),

    /**
     * 业务所需的关键性数据不存在
     */
    DATA_NOT_EXIST(10022, "数据不存在"),

    /**
     * 接口必须使用@Router注解
     *
     * @Tips 取消强制要求
     */
    @Deprecated
    INTERFACE_DEFINE_ERROR(10023, "接口必须使用@Router注解"),

    /**
     * 请求数据格式不正确，MissingServletRequestParameterException
     */
    BAD_REQUEST(10024, "请求数据格式不正确"),

    /**
     * 续签失败
     */
    RENEW_TOKEN_FAILED(10025, "续签失败"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAILED(10026, "文件上传失败"),

    /**
     * 文件上传失败，大小超过限制
     */
    FILE_SIZE_OVER_LIMIT(10027, "上传文件大小超出限制"),

    /**
     * 令牌认证失败：请登录
     */
    TOKEN_FAILED_001(900001, "请登录"),

    /**
     * 令牌认证失败：登录状态过期
     */
    TOKEN_FAILED_002(900002, "当前登录状态已过期，请重新登录"),

    /**
     * 令牌认证失败：系统繁忙
     */
    TOKEN_FAILED_003(900003, "系统繁忙，请稍后再试"),

    /**
     * 令牌认证失败：系统错误
     */
    TOKEN_FAILED_004(900004, "权限校验异常，请联系网站管理员"),

    /**
     * 新增失败
     */
    ADD_FAILED(10028, "新增失败"),

    /**
     * 编辑失败
     */
    EDIT_FAILED(10029, "编辑失败"),

    /**
     * 删除失败
     */
    DELETE_FAILED(10030, "删除失败"),

    /**
     * 请求方式不正确
     */
    REQUEST_METHOD_NOT_SUPPORT(10033, "请求方式不正确"),

    /**
     * 文件下载失败
     */
    FILE_DOWNLOAD_FAILED(10034, "文件下载失败"),

    /**
     * 文件删除失败
     */
    FILE_DELETE_FAILED(10035, "文件删除失败");

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
