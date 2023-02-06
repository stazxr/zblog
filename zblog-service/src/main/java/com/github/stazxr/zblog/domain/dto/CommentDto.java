package com.github.stazxr.zblog.domain.dto;

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
public class CommentDto {
    /**
     * 评论用户
     */
    private Long userId;

    /**
     * 回复用户
     */
    private Long replyUserId;

    /**
     * 回复评论
     */
    private Long replyCommentId;

    /**
     * 评论对象
     */
    private Long objectId;

    /**
     * 上级评论
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论类型: see {@link com.github.stazxr.zblog.domain.enums.CommentType}
     */
    private Integer type;
}
