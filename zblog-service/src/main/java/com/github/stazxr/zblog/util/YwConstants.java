package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.domain.enums.ArticleStatus;

/**
 * 业务常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class YwConstants {
    /**
     * 前台只允许展示已发布状态的文章
     */
    public static final int APG = ArticleStatus.PUBLISHED.getType();
}
