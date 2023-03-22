package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论删除参数
 *
 * @author SunTao
 * @since 2023-02-07
 */
@Getter
@Setter
@ToString
@ApiModel("评论删除参数")
public class CommentDeleteDto {
    /**
     * 评论用户id
     */
    @ApiModelProperty("评论用户id")
    private Long userId;

    /**
     * 评论id
     */
    @ApiModelProperty("评论id")
    private Long commentId;
}
