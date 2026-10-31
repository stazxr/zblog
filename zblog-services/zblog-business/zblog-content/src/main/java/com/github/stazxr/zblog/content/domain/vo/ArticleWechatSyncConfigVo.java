package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章微信公众号同步配置信息
 *
 * @author SunTao
 * @since 2026-11-01
 */
@Getter
@Setter
@ApiModel("文章微信公众号同步配置VO")
public class ArticleWechatSyncConfigVo implements Serializable {
    private static final long serialVersionUID = -2302625264564381252L;

    /**
     * 配置id
     */
    @ApiModelProperty(value = "配置id")
    private Long id;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Long articleId;

    /**
     * 同步的文章版本
     */
    @ApiModelProperty(value = "同步的文章版本")
    private Integer articleVersion;

    /**
     * 微信公众号草稿id
     */
    @ApiModelProperty(value = "微信公众号草稿id")
    private String mediaId;

    /**
     * 微信公众号封面素材id
     */
    @ApiModelProperty(value = "微信公众号封面素材id")
    private String thumbMediaId;

    /**
     * 微信公众号封面素材url
     */
    @ApiModelProperty(value = "微信公众号封面素材url")
    private String thumbMediaUrl;

    /**
     * 微信公众号文章标题
     */
    @ApiModelProperty(value = "微信公众号文章标题")
    private String wechatTitle;

    /**
     * 微信公众号文章作者
     */
    @ApiModelProperty(value = "微信公众号文章作者")
    private String wechatAuthor;

    /**
     * 微信公众号文章摘要
     */
    @ApiModelProperty(value = "微信公众号文章摘要")
    private String wechatDigest;

    /**
     * 同步状态
     */
    @ApiModelProperty(value = "同步状态")
    private Integer syncStatus;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
