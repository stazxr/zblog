package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论
 *
 * @author SunTao
 * @since 2026-08-31
 */
@Getter
@Setter
@TableName("comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 4695212836970479027L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论对象ID
     */
    private String objectId;

    /**
     * 评论类型
     */
    private Integer type;

    /**
     * 所属一级评论ID
     *
     * <p>0表示一级评论</p>
     */
    private Long parentId;

    /**
     * 回复用户ID
     */
    private Long replyUserId;

    /**
     * 评论展示内容
     */
    private String content;

    /**
     * 评论原始内容
     */
    private String originContent;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 评论用户IP
     */
    private String ipAddress;

    /**
     * 评论用户来源
     */
    private String ipSource;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 审核用户
     */
    private Long auditUser;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核原因
     */
    private String auditReason;

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