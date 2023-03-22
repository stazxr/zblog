package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文章点赞信息
 *
 * @author SunTao
 * @since 2023-02-07
 */
@Getter
@Setter
@ToString
@ApiModel("文章点赞信息")
public class ArticleLikeDto {
    /**
     * 点赞用户id
     */
    @ApiModelProperty("点赞用户id")
    private Long userId;

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Long articleId;
}
