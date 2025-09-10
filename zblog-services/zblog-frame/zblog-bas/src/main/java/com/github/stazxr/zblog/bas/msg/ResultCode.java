package com.github.stazxr.zblog.bas.msg;

import com.github.stazxr.zblog.bas.common.CommonUtil;
import com.github.stazxr.zblog.bas.i18n.I18nUtils;

/**
 * 自定义返回码，只有 { code == 1 } 代表是成功的返回
 *
 * @author SunTao
 * @since 2025-08-01
 */
public enum ResultCode {
    /**
     * 操作失败
     */
    FAILED(0, "{result.failed}"),

    /**
     * 操作成功
     */
    SUCCESS(1, "{result.success}"),

    /**
     * 参数校验失败
     */
    PARAM_VALIDATE_FAILED(40001, "{result.param.valid.failed}"),

    /**
     * 数据校验失败
     */
    DATA_VALIDATE_FAILED(40002, "{result.data.valid.failed}"),

    /**
     * 请求参数绑定失败
     */
    REQUEST_PARAM_BIND_FAILED(40003, "{result.request.param.bind.failed}");

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
        if (CommonUtil.isResultTemplateMessage(defaultMsg)) {
            String code = defaultMsg.substring(1, defaultMsg.length() - 1);
            return I18nUtils.getMessage(code);
        }
        return defaultMsg;
    }
}
