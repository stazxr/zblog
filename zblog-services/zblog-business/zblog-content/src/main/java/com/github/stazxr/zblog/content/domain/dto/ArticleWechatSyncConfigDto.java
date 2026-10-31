package com.github.stazxr.zblog.content.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章微信公众号同步配置信息
 *
 * @author SunTao
 * @since 2026-11-01
 */
@Getter
@Setter
@ApiModel("文章微信公众号同步配置DTO")
public class ArticleWechatSyncConfigDto implements Serializable {
    private static final long serialVersionUID = -875137277660093699L;

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
     * 微信公众号封面id
     */
    @ApiModelProperty(value = "微信公众号封面id")
    private Long wechatCoverImageId;

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
}
