package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 说说状态
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Getter
public enum TalkStatus {
    /**
     * 公开
     */
    PUBLIC(1),

    /**
     * 私密
     */
    PRIVATE(2);

    private final Integer status;

    TalkStatus(Integer status) {
        this.status = status;
    }

    public static TalkStatus of(Integer talkStatus) {
        for (TalkStatus item : values()) {
            if (item.status.equals(talkStatus)) {
                return item;
            }
        }

        return null;
    }
}
