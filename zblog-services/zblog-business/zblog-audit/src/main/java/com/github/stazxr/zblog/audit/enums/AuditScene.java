package com.github.stazxr.zblog.audit.enums;

/**
 * 内容审核场景
 *
 * 不同场景可以配置不同的审核策略，例如：
 *
 * @author SunTao
 * @since 2026-06-29
 */
public enum AuditScene {
    /**
     * 默认
     */
    DEFAULT,
    /**
     * 弹幕
     */
    BARRAGE,
    /**
     * 评论
     */
    COMMENT,
    /**
     * 留言
     */
    MESSAGE,
    /**
     * 文章
     */
    ARTICLE
}
