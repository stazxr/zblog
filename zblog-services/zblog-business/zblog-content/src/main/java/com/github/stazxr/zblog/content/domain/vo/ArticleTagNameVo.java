package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章标签名称信息
 *
 * @author SunTao
 * @since 2026-10-18
 */
@Getter
@Setter
@ApiModel(value = "ArticleTagNameVo", description = "文章标签名称信息")
public class ArticleTagNameVo implements Serializable {
    private static final long serialVersionUID = -8660429690435731865L;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Long articleId;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
    private String tagName;
}
