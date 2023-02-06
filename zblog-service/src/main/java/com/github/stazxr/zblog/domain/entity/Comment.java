package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 评论
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
@TableName("comment")
public class Comment extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

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
     * 上级评论
     */
    private Long parentId;

    /**
     * 评论对象
     */
    private Long objectId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论类型: see {@link com.github.stazxr.zblog.domain.enums.CommentType}
     */
    private Integer type;

    /**
     * 评论用户IP
     */
    private String ipAddress;

    /**
     * 评论用户地址
     */
    private String ipSource;

    /**
     * 是否审核
     */
    private Boolean isReview;

    /**
     * 是否已删除
     */
    @TableLogic
    private Boolean deleted;
}
