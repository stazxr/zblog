package com.github.stazxr.zblog.strategy;

import com.github.stazxr.zblog.domain.vo.ArticleVo;

import java.util.List;

/**
 * 文章搜索策略
 *
 * @author SunTao
 * @since 2023-02-08
 */
public interface ArticleSearchStrategy {
    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List<ArticleVo>} 文章列表
     */
    List<ArticleVo> searchArticle(String keywords);
}
