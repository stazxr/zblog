package com.github.stazxr.zblog.portal.domain.dto;

import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 评论信息
 *
 * @author SunTao
 * @since 2026-09-04
 */
@Getter
@Setter
@ApiModel("评论DTO")
public class CommentDto extends BaseDto {
    private static final long serialVersionUID = 2837345846869700412L;

    /**
     * 评论对象ID
     */
    @ApiModelProperty("评论对象ID")
    private String objectId;

    /**
     * 评论类型
     *
     * @see com.github.stazxr.zblog.content.ext.domain.enums.CommentType
     */
    @ApiModelProperty("评论类型")
    private Integer type;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String content;

    /**
     * 父评论ID
     *
     * 一级评论：0L
     * 回复评论：一级评论ID
     */
    @ApiModelProperty("父评论ID")
    private Long parentId;

    /**
     * 回复评论ID
     *
     * 新增评论：NULL
     * 回复评论：实际回复的评论ID
     */
    @ApiModelProperty("回复评论ID")
    private Long replyCommentId;
}
