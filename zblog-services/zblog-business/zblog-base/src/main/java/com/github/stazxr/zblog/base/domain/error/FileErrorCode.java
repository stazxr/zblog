package com.github.stazxr.zblog.base.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 文件错误码定义。
 *
 * @author SunTao
 * @since 2026-02-08
 */
public enum FileErrorCode implements ErrorCode {
    /** 文件不存在或已被删除 */
    EFILEB000("FILE_NOT_EXISTS"),
    /** 待上传文件列表为空 */
    EFILEB001("FILE_UPLOAD_WITHOUT_FILE"),
    /** 文件上传未启用 */
    EFILEB002("FILE_UPLOAD_SWITCH_OFF"),
    /** 不支持的文件类型 */
    EFILEB003("FILE_UPLOAD_TYPE_NOT_SUPPORT"),
    /** 图片内容校验失败 */
    EFILEB004("FILE_UPLOAD_IMAGE_INVALID"),
    /** 文件分辨率过大 */
    EFILEB005("FILE_UPLOAD_IMAGE_RADIO_OVER_MAX"),
    /** 文件过大 */
    EFILEB006("FILE_UPLOAD_IMAGE_SIZE_OVER_MAX"),
    /** 文件已关联业务数据 */
    EFILEB007("FILE_DELETE_WITH_BUSINESS"),

    /** 上传失败，数据库异常 */
    SFILEB001("FILE_UPLOAD_WITH_DB_FAILED");

    private final String i18nKey;

    FileErrorCode(String i18nKey) {
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
