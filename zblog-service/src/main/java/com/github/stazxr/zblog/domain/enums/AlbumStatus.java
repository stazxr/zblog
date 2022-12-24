package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 相册状态
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Getter
public enum AlbumStatus {
    /**
     * 公开
     */
    PUBLIC(1),

    /**
     * 私密
     */
    PRIVATE(2);

    private final Integer status;

    AlbumStatus(Integer status) {
        this.status = status;
    }

    public static AlbumStatus of(Integer albumStatus) {
        for (AlbumStatus item : values()) {
            if (item.status.equals(albumStatus)) {
                return item;
            }
        }

        return null;
    }
}
