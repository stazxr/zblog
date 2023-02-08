package com.github.stazxr.zblog.domain.dto.setting;

import lombok.Data;

/**
 * 其他信息
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Data
public class OtherInfo {
    /**
     * 游客头像
     */
    private String touristAvatar = "";

    /**
     * 用户头像
     */
    private String userAvatar = "";

    /**
     * 文章浏览配置
     */
    private int articleViewInterval = -1;

    /**
     * 文章搜索策略
     */
    private String articleSearchStrategy = "mysql";

    /**
     * 是否评论审核
     */
    private Integer isCommentReview = 1;

    /**
     * 是否留言审核
     */
    private Integer isMessageReview = 1;

    /**
     * 是否邮箱通知
     */
    private Integer isEmailNotice = 0;

    /**
     * 是否打赏
     */
    private Integer isReward = 0;

    /**
     * 微信二维码
     */
    private String weiXinQrCode;

    /**
     * 支付宝二维码
     */
    private String alipayQrCode;

    /**
     * 文章封面
     */
    private String articleCover = "";

    /**
     * 是否开启音乐
     */
    private Integer isMusicPlayer = 0;
}
