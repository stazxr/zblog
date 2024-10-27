package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 评论回复页面展示信息
 *
 * @author SunTao
 * @since 2023-02-06
 */
@Getter
@Setter
public class CommentReplyVo extends BaseVo {
    /**
     * 评论ID
     */
    private Long id;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论用户昵称
     */
    private String nickname;

    /**
     * 评论用户头像
     */
    private String avatar;

    /**
     * 评论用户网站
     */
    private String website;

    /**
     * 评论时间
     */
    private String createTime;

    /**
     * 回复评论ID
     */
    private Long replyCommentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论用户地址
     */
    private String ipSource;

    /**
     * 评论回复用户ID
     */
    private Long replyUserId;

    /**
     * 评论回复用户昵称
     */
    private String replyNickname;

    /**
     * 评论回复用户网站
     */
    private String replyWebsite;

    /**
     * 点赞数
     */
    private int likeCount;

    /**
     * 回复量
     */
    private int replyCount;

    /**
     * 回复列表
     */
    private List<CommentReplyVo> replyList;
}
