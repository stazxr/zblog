package com.github.stazxr.zblog.bas.exception;

import org.springframework.lang.Nullable;

/**
 * 异常错误码
 *
 * @author SunTao
 * @since 2024-08-14
 */
public class ExpMessageCode {
    public static final ExpMessageCode PARAM_NULL = new ExpMessageCode("valid.comm.param.NotNull");
    public static final ExpMessageCode PARAM_BLANK = new ExpMessageCode("valid.comm.param.NotBlank");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 参数
     */
    private Object[] args = null;

    /**
     * 构造方法，创建指定错误码实例
     *
     * @param code 错误码字符串
     */
    public ExpMessageCode(String code) {
        this.code = code;
    }

    /**
     * 构造方法，创建指定错误码实例
     *
     * @param code 错误码字符串
     * @param args 错误码参数列表
     */
    public ExpMessageCode(String code, @Nullable Object[] args) {
        this.code = code;
        this.args = args;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取参数清单
     *
     * @return 参数清单
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * 静态工厂方法，创建错误码实例
     *
     * @param code 错误码字符串
     * @return ExpMessageCode 实例
     */
    public static ExpMessageCode of(String code) {
        return new ExpMessageCode(code);
    }

    /**
     * 静态工厂方法，创建错误码实例
     *
     * @param code 错误码字符串
     * @param args 错误码参数列表
     * @return ExpMessageCode 实例
     */
    public static ExpMessageCode of(String code, Object... args) {
        return new ExpMessageCode(code, args);
    }
}
