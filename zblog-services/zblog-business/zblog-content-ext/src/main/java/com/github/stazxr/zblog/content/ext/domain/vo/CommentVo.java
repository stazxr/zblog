package com.github.stazxr.zblog.content.ext.domain.vo;

import io.swagger.annotations.ApiModelProperty;
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
public class CommentVo implements Serializable {
    private static final long serialVersionUID = -532392806069237761L;

    /**
     * 评论id
     */
    @ApiModelProperty("评论id")
    private Long id;

    /**
     * 所属一级评论id
     *
     * <p>0表示一级评论</p>
     */
    @ApiModelProperty("所属一级评论id")
    private Long parentId;

    /**
     * 评论类型
     */
    @ApiModelProperty("评论类型")
    private Integer type;

    /**
     * 评论对象id
     */
    @ApiModelProperty("评论对象id")
    private String objectId;

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
     * 评论访客id
     */
    @ApiModelProperty("评论访客id")
    private String visitorId;

    /**
     * 回复访客id
     */
    @ApiModelProperty("回复访客id")
    private String replyVisitorId;

    /**
     * 评论展示内容
     */
    @ApiModelProperty("评论展示内容")
    private String content;

    /**
     * 评论原始内容
     */
    @ApiModelProperty("评论原始内容")
    private String originContent;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer likeCount;

    /**
     * 回复数
     */
    @ApiModelProperty("回复数")
    private Integer replyCount;

    /**
     * 评论用户IP
     */
    @ApiModelProperty("评论用户IP")
    private String ipAddress;

    /**
     * 评论用户来源
     */
    @ApiModelProperty("评论用户来源")
    private String ipSource;

    /**
     * 用户代理
     */
    @ApiModelProperty("用户代理")
    private String userAgent;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 审核用户id
     */
    @ApiModelProperty("审核用户id")
    private Long auditUser;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核原因
     */
    @ApiModelProperty("审核原因")
    private String auditReason;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
