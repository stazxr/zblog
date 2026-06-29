package com.github.stazxr.zblog.audit.model;

/**
 * 内容审核场景。
 *
 * 不同场景可以配置不同的审核策略，例如：
 *
 * <ul>
 *     <li>COMMENT：评论审核</li>
 *     <li>MESSAGE：留言审核</li>
 *     <li>BARRAGE：弹幕审核</li>
 *     <li>ARTICLE：文章审核</li>
 * </ul>
 *
 * @author SunTao
 * @since 2026-06-29
 */
public enum AuditScene {
    /**
     * 评论
     */
    COMMENT,

    /**
     * 留言
     */
    MESSAGE,

    /**
     * 弹幕
     */
    BARRAGE,

    /**
     * 文章
     */
    ARTICLE
}
