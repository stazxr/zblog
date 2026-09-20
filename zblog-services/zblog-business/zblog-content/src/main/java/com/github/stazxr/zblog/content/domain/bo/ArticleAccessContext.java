package com.github.stazxr.zblog.content.domain.bo;

/**
 * 文章访问上下文
 *
 * @author Sun Tao
 * @since 2026-09-19
 */
public class ArticleAccessContext {
    /**
     * 当前登录用户ID
     */
    private Long userId;

    /**
     * 访客ID
     */
    private String visitorId;

    /**
     * 访问密码
     */
    private String password;

    /**
     * 公众号验证凭证
     */
    private String verifyToken;

    /**
     * 是否已登录
     */
    public boolean isLogin() {
        return userId != null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }
}