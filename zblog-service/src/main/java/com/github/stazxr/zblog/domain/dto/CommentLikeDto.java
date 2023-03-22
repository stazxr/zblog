package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论点赞信息
 *
 * @author SunTao
 * @since 2023-02-06
 */
@Getter
@Setter
@ToString
@ApiModel("评论点赞信息")
public class CommentLikeDto {
    /**
     * 点赞用户id
     */
    @ApiModelProperty("点赞用户id")
    private Long userId;

    /**
     * 评论id
     */
    @ApiModelProperty("评论id")
    private Long commentId;
}
