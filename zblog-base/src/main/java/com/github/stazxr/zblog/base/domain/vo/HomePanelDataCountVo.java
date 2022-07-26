package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 首页面板数量统计（访问量，评论数等）
 *
 * @author SunTao
 * @since 2022-07-19
 */
@Data
@ToString
public class HomePanelDataCountVo {
    /**
     * 访问量
     */
    private final int visits = 0;

    /**
     * 评论数
     */
    private final int messages = 0;

    /**
     * 文章数
     */
    private final int articles = 0;

    /**
     * 告警数
     */
    private final int warnings = 0;
}
