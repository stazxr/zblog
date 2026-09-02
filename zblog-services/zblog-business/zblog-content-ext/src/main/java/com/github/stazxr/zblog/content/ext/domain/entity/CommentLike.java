package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论点赞
 *
 * @author suntao
 * @since 2026-08-31
 */
@Getter
@Setter
@TableName("comment_like")
public class CommentLike implements Serializable {
    private static final long serialVersionUID = -7939045452788030640L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 点赞用户ID
     */
    private Long userId;

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 点赞IP
     */
    private String ipAddress;

    /**
     * 点赞IP来源
     */
    private String ipSource;

    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新用户
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}