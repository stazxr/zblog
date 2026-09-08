package com.github.stazxr.zblog.portal.domain.vo;

import com.github.stazxr.zblog.portal.domain.enums.PortalUserType;
import com.github.stazxr.zblog.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论VO
 *
 * @author SunTao
 * @since 2026-09-07
 */
@ApiModel("评论VO")
public class PortalCommentVo implements Serializable {
    private static final long serialVersionUID = 2113751235800644866L;

    /**
     * 评论ID
     */
    @Getter
    @Setter
    @ApiModelProperty("评论ID")
    private Long id;

    /**
     * 所属一级评论ID
     *
     * <p>0表示一级评论</p>
     */
    @Getter
    @Setter
    @ApiModelProperty("父评论ID")
    private Long parentId;

    /**
     * 评论用户ID
     */
    @Setter
    @ApiModelProperty("评论用户ID")
    private Long userId;

    /**
     * 评论用户昵称
     */
    @Setter
    @ApiModelProperty("评论用户昵称")
    private String userNickname;

    /**
     * 评论用户头像
     */
    @Setter
    @ApiModelProperty("评论用户头像")
    private String userAvatar;

    /**
     * 评论用户网站
     */
    @Setter
    @ApiModelProperty("评论用户网站")
    private String userWebsite;

    /**
     * 回复用户ID
     */
    @Setter
    @ApiModelProperty("回复用户ID")
    private Long replyUserId;

    /**
     * 回复用户昵称
     */
    @Setter
    @ApiModelProperty("回复用户昵称")
    private String replyUserNickname;

    /**
     * 回复用户头像
     */
    @Setter
    @ApiModelProperty("回复用户头像")
    private String replyUserAvatar;

    /**
     * 回复用户网站
     */
    @Setter
    @ApiModelProperty("回复用户网站")
    private String replyUserWebsite;

    /**
     * 评论访客ID
     */
    @Setter
    @ApiModelProperty("评论访客ID")
    private String visitorId;

    /**
     * 评论访客昵称
     */
    @Setter
    @ApiModelProperty("评论访客昵称")
    private String visitorNickname;

    /**
     * 评论访客头像
     */
    @Setter
    @ApiModelProperty("评论访客头像")
    private String visitorAvatar;

    /**
     * 回复访客ID
     */
    @Setter
    @ApiModelProperty("回复访客ID")
    private String replyVisitorId;

    /**
     * 回复访客昵称
     */
    @Setter
    @ApiModelProperty("回复访客昵称")
    private String replyVisitorNickname;

    /**
     * 回复访客头像
     */
    @Setter
    @ApiModelProperty("回复访客头像")
    private String replyVisitorAvatar;

    /**
     * 评论内容
     */
    @Getter
    @Setter
    @ApiModelProperty("评论内容")
    private String content;

    /**
     * 点赞数
     */
    @Getter
    @Setter
    @ApiModelProperty("点赞数")
    private Integer likeCount;

    /**
     * 回复数
     */
    @Getter
    @Setter
    @ApiModelProperty("回复数")
    private Integer replyCount;

    /**
     * 评论用户来源
     */
    @Getter
    @Setter
    @ApiModelProperty("评论用户来源")
    private String ipSource;

    /**
     * 评论时间
     */
    @Getter
    @Setter
    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;

    /**
     * 评论用户
     */
    @ApiModelProperty("评论用户")
    private PortalUserVo user;

    /**
     * 回复用户
     */
    @ApiModelProperty("回复用户")
    private PortalUserVo replyUser;

    /**
     * 是否点赞
     */
    @Getter
    @Setter
    @ApiModelProperty("是否点赞")
    private Boolean liked;

    public PortalUserVo getUser() {
        return buildCommentUser(userId, userNickname, userAvatar, userWebsite, visitorId, visitorNickname, visitorAvatar);
    }

    public PortalUserVo getReplyUser() {
        return buildCommentUser(replyUserId, replyUserNickname, replyUserAvatar, replyUserWebsite, replyVisitorId, replyVisitorNickname, replyVisitorAvatar);
    }

    private PortalUserVo buildCommentUser(Long userId, String userNickname, String userAvatar, String website,
            String visitorId, String visitorNickname, String visitorAvatar) {
        PortalUserVo user = new PortalUserVo();
        if (userId != null) {
            user.setId(String.valueOf(userId));
            user.setNickname(userNickname);
            user.setAvatar(userAvatar);
            user.setWebsite(website);
            user.setUserType(PortalUserType.USER.getType());
        } else if (StringUtils.isNotBlank(visitorId)) {
            user.setId(visitorId);
            user.setNickname(visitorNickname);
            user.setAvatar(visitorAvatar);
            user.setUserType(PortalUserType.VISITOR.getType());
        }
        return user;
    }
}
