package com.github.stazxr.zblog.base.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 字典错误码定义。
 *
 * @author SunTao
 * @since 2026-02-06
 */
public enum DictErrorCode implements ErrorCode {
    /** 字典KEY不能为空 */
    EDICTA000("DICT_DICTKEY_REQUIRED"),
    /** 字典状态不能为空 */
    EDICTA001("DICT_ENABLED_REQUIRED"),
    /** 上级字典不存在 */
    EDICTA002("DICT_PARENT_NOT_EXISTS"),
    /** 上级字典类型不正确 */
    EDICTA003("DICT_PARENT_TYPE_INVALID"),
    /** 存在子节点，无法被删除 */
    EDICTA004("DICT_DELETE_WITH_CHILDREN");

    private final String i18nKey;

    DictErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码，唯一且符合规范
     *
     * @return 错误码字符串
     */
    @Override
    public String getCode() {
        return name();
    }

    /**
     * 国际化 key，用于获取对应消息
     *
     * @return i18n key
     */
    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}
