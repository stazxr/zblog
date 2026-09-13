package com.github.stazxr.zblog.content.ext.domain.vo;

import com.github.stazxr.zblog.audit.model.AuditRecord;
import com.github.stazxr.zblog.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论信息
 *
 * @author SunTao
 * @since 2026-08-31
 */
@Getter
@Setter
@ApiModel("评论VO")
public class CommentVo implements Serializable {
    private static final long serialVersionUID = -532392806069237761L;

    /**
     * 评论ID
     */
    @ApiModelProperty("评论ID")
    private Long id;

    /**
     * 所属一级评论ID
     *
     * <p>0表示一级评论</p>
     */
    @ApiModelProperty("父评论ID")
    private Long parentId;

    /**
     * 回复评论ID
     */
    @ApiModelProperty("回复评论ID")
    private Long replyCommentId;

    /**
     * 评论用户ID
     */
    @ApiModelProperty("评论用户ID")
    private Long userId;

    /**
     * 评论用户昵称
     */
    @ApiModelProperty("评论用户昵称")
    private String userNickname;

    /**
     * 评论用户头像
     */
    @ApiModelProperty("评论用户头像")
    private String userAvatar;

    /**
     * 评论用户网站
     */
    @ApiModelProperty("评论用户网站")
    private String userWebsite;

    /**
     * 回复用户ID
     */
    @ApiModelProperty("回复用户ID")
    private Long replyUserId;

    /**
     * 回复用户昵称
     */
    @ApiModelProperty("回复用户昵称")
    private String replyUserNickname;

    /**
     * 回复用户头像
     */
    @ApiModelProperty("回复用户头像")
    private String replyUserAvatar;

    /**
     * 回复用户网站
     */
    @ApiModelProperty("回复用户网站")
    private String replyUserWebsite;

    /**
     * 评论访客ID
     */
    @ApiModelProperty("评论访客ID")
    private String visitorId;

    /**
     * 评论访客昵称
     */
    @ApiModelProperty("评论访客昵称")
    private String visitorNickname;

    /**
     * 评论访客头像
     */
    @ApiModelProperty("评论访客头像")
    private String visitorAvatar;

    /**
     * 回复访客ID
     */
    @ApiModelProperty("回复访客ID")
    private String replyVisitorId;

    /**
     * 回复访客昵称
     */
    @ApiModelProperty("回复访客昵称")
    private String replyVisitorNickname;

    /**
     * 回复访客头像
     */
    @ApiModelProperty("回复访客头像")
    private String replyVisitorAvatar;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String content;

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
     * 评论用户来源
     */
    @ApiModelProperty("评论用户来源")
    private String ipSource;

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
     * 评论原始内容
     */
    @ApiModelProperty("评论原始内容")
    private String originContent;

    /**
     * 评论用户IP
     */
    @ApiModelProperty("评论用户IP")
    private String ipAddress;

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
     * 评论时间
     */
    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;

    /**
     * 评论用户
     */
    @ApiModelProperty("评论用户")
    private CommentUserVo user;

    /**
     * 回复用户
     */
    @ApiModelProperty("回复用户")
    private CommentUserVo replyUser;

    /**
     * 评论级别
     */
    @ApiModelProperty("评论级别")
    private Integer level;

    /**
     * 审核记录
     */
    @ApiModelProperty("审核记录")
    private AuditRecord auditRecord;

    public CommentUserVo getUser() {
        return buildCommentUser(userId, userNickname, userAvatar, userWebsite, visitorId, visitorNickname, visitorAvatar);
    }

    public CommentUserVo getReplyUser() {
        return buildCommentUser(replyUserId, replyUserNickname, replyUserAvatar, replyUserWebsite, replyVisitorId, replyVisitorNickname, replyVisitorAvatar);
    }

    private CommentUserVo buildCommentUser(Long userId, String userNickname, String userAvatar, String website,
            String visitorId, String visitorNickname, String visitorAvatar) {
        CommentUserVo user = new CommentUserVo();
        if (userId != null) {
            user.setId(String.valueOf(userId));
            user.setNickname(userNickname);
            user.setAvatar(userAvatar);
            user.setWebsite(website);
            user.setUserType(1);
        } else if (StringUtils.isNotBlank(visitorId)) {
            user.setId(visitorId);
            user.setNickname(visitorNickname);
            user.setAvatar(visitorAvatar);
            user.setUserType(2);
        }
        return user;
    }
}
