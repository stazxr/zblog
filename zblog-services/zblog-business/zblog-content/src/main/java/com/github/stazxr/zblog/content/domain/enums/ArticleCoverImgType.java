package com.github.stazxr.zblog.content.domain.enums;

/**
 * 文章封面类型
 *
 * @author SunTao
 * @since 2022-11-24
 */
public enum ArticleCoverImgType {
    /**
     * 单封面
     */
    SINGLE(1),

    /**
     * 多封面（轮询）
     */
    MULTI(2),

    /**
     * 默认封面
     */
    DEFAULT(3),

    /**
     * 文章标题
     */
    TITLE(4),

    /**
     * 无封面
     */
    NONE(5),

    /**
     * 多封面（随机）
     */
    MULTI2(5);

    private final Integer type;

    ArticleCoverImgType(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
