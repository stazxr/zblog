package com.github.stazxr.zblog.bas.sequence;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 序号生成错误码定义。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public enum SequenceErrorCode implements ErrorCode {
    /**
     * 时钟被回拨
     */
    SSEQGA000("error.system.seqg.clock_rollback"),

    /**
     * 序号生成器未配置
     */
    SSEQGA001("error.system.seqg.not_configured"),
    ;

    private final String i18nKey;

    SequenceErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }


    /**
     * 错误码
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