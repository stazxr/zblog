package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章封面地址信息
 *
 * @author SunTao
 * @since 2026-10-18
 */
@Getter
@Setter
@ApiModel(value = "ArticleCoverImageVo", description = "文章封面地址信息")
public class ArticleCoverImageVo implements Serializable {
    private static final long serialVersionUID = 980901161806681135L;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Long articleId;

    /**
     * 文章封面地址
     */
    @ApiModelProperty(value = "文章封面地址")
    private String coverImage;
}
