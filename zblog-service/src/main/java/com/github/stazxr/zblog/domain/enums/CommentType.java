package com.github.stazxr.zblog.domain.enums;

import lombok.Getter;

/**
 * 评论类型
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
public enum CommentType {
    /**
     * 文章
     */
    ARTICLE(1),

    /**
     * 友链
     */
    FRIEND_LINK(2),

    /**
     * 说说
     */
    TALK(3);

    private final Integer type;

    CommentType(Integer type) {
        this.type = type;
    }

    public static CommentType of(Integer articleType) {
        for (CommentType item : values()) {
            if (item.type.equals(articleType)) {
                return item;
            }
        }

        return null;
    }
}
