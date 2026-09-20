package com.github.stazxr.zblog.content.domain.bo;

import com.github.stazxr.zblog.content.domain.enums.ArticlePerm;

/**
 * 文章访问检查结果
 *
 * @author Sun Tao
 * @since 2026-09-19
 */
public class ArticleAccessResult {
    /**
     * 是否允许访问
     */
    private boolean allowed;

    /**
     * 访问权限类型
     */
    private ArticlePerm perm;

    /**
     * 未授权时的处理类型
     */
    private String action;

    /**
     * 提示信息
     */
    private String message;

    private ArticleAccessResult() {
    }

    public static ArticleAccessResult allow(ArticlePerm perm) {
        ArticleAccessResult result = new ArticleAccessResult();
        result.allowed = true;
        result.perm = perm;
        return result;
    }

    public static ArticleAccessResult deny(ArticlePerm perm, String action, String message) {
        ArticleAccessResult result = new ArticleAccessResult();
        result.allowed = false;
        result.perm = perm;
        result.action = action;
        result.message = message;
        return result;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public ArticlePerm getPerm() {
        return perm;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
