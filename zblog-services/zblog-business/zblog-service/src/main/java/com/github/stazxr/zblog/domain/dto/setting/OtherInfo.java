package com.github.stazxr.zblog.domain.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 其他信息
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Data
@ApiModel("网站其他信息")
public class OtherInfo {
    /**
     * 游客头像
     */
    @ApiModelProperty(value = "游客头像")
    private String touristAvatar = "";

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatar = "";

    /**
     * 文章浏览配置
     */
    @ApiModelProperty(value = "文章浏览配置", example = "-1")
    private int articleViewInterval = -1;

    /**
     * 文章搜索策略
     */
    @ApiModelProperty(value = "文章搜索策略", example = "mysql")
    private String articleSearchStrategy = "mysql";

    /**
     * 是否评论审核
     */
    @ApiModelProperty(value = "是否评论审核", example = "1")
    private Integer isCommentReview = 1;

    /**
     * 是否留言审核
     */
    @ApiModelProperty(value = "是否留言审核", example = "1")
    private Integer isMessageReview = 1;

    /**
     * 是否邮箱通知
     */
    @ApiModelProperty(value = "是否邮箱通知", example = "0")
    private Integer isEmailNotice = 0;

    /**
     * 是否打赏
     */
    @ApiModelProperty(value = "是否打赏", example = "0")
    private Integer isReward = 0;

    /**
     * 微信二维码
     */
    @ApiModelProperty(value = "微信二维码")
    private String weiXinQrCode;

    /**
     * 支付宝二维码
     */
    @ApiModelProperty(value = "支付宝二维码")
    private String alipayQrCode;

    /**
     * 文章封面
     */
    @ApiModelProperty(value = "文章封面")
    private String articleCover = "";

    /**
     * 是否开启音乐
     */
    @ApiModelProperty(value = "是否开启音乐", example = "0")
    private Integer isMusicPlayer = 0;
}
