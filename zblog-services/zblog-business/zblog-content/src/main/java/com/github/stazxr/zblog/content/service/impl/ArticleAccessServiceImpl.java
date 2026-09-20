package com.github.stazxr.zblog.content.service.impl;

import com.github.stazxr.zblog.content.domain.bo.ArticleAccessContext;
import com.github.stazxr.zblog.content.domain.bo.ArticleAccessResult;
import com.github.stazxr.zblog.content.domain.entity.Article;
import com.github.stazxr.zblog.content.domain.enums.ArticlePerm;
import com.github.stazxr.zblog.content.service.ArticleAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 文章访问权限服务实现
 *
 * @author Sun Tao
 * @since x2026-09-19
 */
@Service
@RequiredArgsConstructor
public class ArticleAccessServiceImpl implements ArticleAccessService {
    // private final ArticleAccessUserMapper articleAccessUserMapper;

    // private final ArticleAccessGrantMapper articleAccessGrantMapper;

    // private final ArticleOrderMapper articleOrderMapper;

    @Override
    public ArticleAccessResult check(Article article, ArticleAccessContext context) {
        ArticlePerm perm = ArticlePerm.of(article.getArticlePerm());
        if (perm == null) {
            return ArticleAccessResult.deny(null, "DENY", "文章访问权限配置错误");
        }

//        ArticleAccessService
//        ↓
//        ArticleAccessChecker
//        ├── PublicAccessChecker
//        ├── LoginAccessChecker
//        ├── SelfAccessChecker
//        ├── WechatAccessChecker
//        ├── PasswordAccessChecker
//        ├── PayAccessChecker
//        └── UserAccessChecker
        switch (perm) {
            case PUBLIC:
                return ArticleAccessResult.allow(perm);
            case LOGIN:
                return checkLogin(perm, context);
            case SELF:
                return checkSelf(article, perm, context);
            case WECHAT:
                return checkWechat(article, perm, context);
            case PASSWORD:
                return checkPassword(article, perm, context);
            case PAY:
                return checkPay(article, perm, context);
            case USER:
                return checkUser(article, perm, context);
            default:
                return ArticleAccessResult.deny(perm, "DENY", "暂无访问权限");
        }
    }

    private ArticleAccessResult checkLogin(ArticlePerm perm, ArticleAccessContext context) {
        if (!context.isLogin()) {
            return ArticleAccessResult.deny(perm, "LOGIN", "请登录后访问");
        }

        return ArticleAccessResult.allow(perm);
    }

    private ArticleAccessResult checkSelf(Article article, ArticlePerm perm, ArticleAccessContext context) {
        if (!context.isLogin()) {
            return ArticleAccessResult.deny(perm, "LOGIN", "请登录后访问");
        }
        if (!Objects.equals(article.getAuthorId(), context.getUserId())) {
            return ArticleAccessResult.deny(perm, "DENY", "无权访问该文章");
        }
        return ArticleAccessResult.allow(perm);
    }

    private ArticleAccessResult checkWechat(Article article, ArticlePerm perm, ArticleAccessContext context) {
        if (hasWechatGrant(article.getId(), context)) {
            return ArticleAccessResult.allow(perm);
        }

        return ArticleAccessResult.deny(perm, "WECHAT", "请完成公众号验证后访问");
    }

    private ArticleAccessResult checkPassword(Article article, ArticlePerm perm, ArticleAccessContext context) {
        if (hasPasswordGrant(article.getId(), context)) {
            return ArticleAccessResult.allow(perm);
        }

        return ArticleAccessResult.deny(perm, "PASSWORD", "请输入访问密码");
    }

    private ArticleAccessResult checkPay(Article article, ArticlePerm perm, ArticleAccessContext context) {
        if (!context.isLogin()) {
            return ArticleAccessResult.deny(perm, "LOGIN", "请登录后购买");
        }

        if (hasPayGrant(article.getId(), context.getUserId())) {
            return ArticleAccessResult.allow(perm);
        }

        return ArticleAccessResult.deny(perm, "PAY", "请购买后访问");
    }

    private ArticleAccessResult checkUser(Article article, ArticlePerm perm, ArticleAccessContext context) {
        if (!context.isLogin()) {
            return ArticleAccessResult.deny(perm, "LOGIN", "请登录后访问");
        }

        // boolean hasPermission = articleAccessUserMapper.exists(article.getId(), context.getUserId());
        // if (!hasPermission) {
        if (true) {
            return ArticleAccessResult.deny(perm, "DENY", "您没有访问该文章的权限");
        }

        return ArticleAccessResult.allow(perm);
    }

    private boolean hasWechatGrant(Long articleId, ArticleAccessContext context) {
        // return articleAccessGrantMapper.exists(articleId, context.getUserId(), context.getVisitorId(), 2);
        return false;
    }

    private boolean hasPasswordGrant(Long articleId, ArticleAccessContext context) {
        // return articleAccessGrantMapper.exists(articleId, context.getUserId(), context.getVisitorId(), 1);
        return false;
    }

    private boolean hasPayGrant(Long articleId, Long userId) {
        // return articleOrderMapper.existsPaid(articleId, userId);
        return false;
    }
}