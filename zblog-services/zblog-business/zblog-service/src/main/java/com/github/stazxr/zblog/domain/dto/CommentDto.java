package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论信息
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
@ToString
@ApiModel("评论信息")
public class CommentDto {
    /**
     * 评论用户id
     */
    @ApiModelProperty("评论用户id")
    private Long userId;

    /**
     * 回复用户id
     */
    @ApiModelProperty("回复用户id")
    private Long replyUserId;

    /**
     * 回复评论id
     */
    @ApiModelProperty("回复评论id")
    private Long replyCommentId;

    /**
     * 评论对象id
     */
    @ApiModelProperty("评论对象id")
    private Long objectId;

    /**
     * 上级评论id
     */
    @ApiModelProperty("上级评论id")
    private Long parentId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String content;

    /**
     * 评论类型: see {@link com.github.stazxr.zblog.domain.enums.CommentType}
     */
    @ApiModelProperty("评论类型，1：文章、2：友链、3：说说")
    private Integer type;
}
