package com.github.stazxr.zblog.content.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 文章审核信息
 *
 * @author SunTao
 * @since 2026-11-17
 */
@Getter
@Setter
@ApiModel(value = "文章审核DTO")
public class ArticleReviewDto implements Serializable {
    private static final long serialVersionUID = 8707837244254703075L;

    /**
     * 文章ID
     */
    @NotNull(message = "{TECH_PARAM_MISS}")
    @ApiModelProperty(value = "文章ID")
    private Long id;

}
