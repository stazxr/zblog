package com.github.stazxr.zblog.content.service;

import com.github.stazxr.zblog.content.domain.bo.ArticleAccessContext;
import com.github.stazxr.zblog.content.domain.bo.ArticleAccessResult;
import com.github.stazxr.zblog.content.domain.entity.Article;

/**
 * 文章访问权限服务
 *
 * @author Sun Tao
 * @since 2026-09-19
 */
public interface ArticleAccessService {
    /**
     * 检查文章是否允许当前访问者访问
     *
     * @param article 文章
     * @param context 访问上下文
     * @return 访问结果
     */
    ArticleAccessResult check(Article article, ArticleAccessContext context);

//    /**
//     * 校验文章访问密码
//     *
//     * @param articleId 文章ID
//     * @param password 访问密码
//     * @param context 访问上下文
//     * @return 访问结果
//     */
//    ArticleAccessResult verifyPassword(Long articleId, String password, ArticleAccessContext context);
//
//    /**
//     * 校验公众号访问
//     *
//     * @param articleId 文章ID
//     * @param verifyToken 验证凭证
//     * @param context 访问上下文
//     * @return 访问结果
//     */
//    ArticleAccessResult verifyWechat(Long articleId, String verifyToken, ArticleAccessContext context);
//
//    /**
//     * 创建付费订单
//     *
//     * @param articleId 文章ID
//     * @param context 访问上下文
//     * @return 订单号
//     */
//    String createPayOrder(Long articleId, ArticleAccessContext context);
}
