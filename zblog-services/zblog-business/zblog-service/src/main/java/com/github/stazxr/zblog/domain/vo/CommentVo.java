package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 评论页面展示信息
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
public class CommentVo extends BaseVo {
    /**
     * 主键
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
     * 评论内容
     */
    private String content;

    /**
     * 评论用户地址
     */
    private String ipSource;

    /**
     * 点赞数
     */
    private int likeCount;

    /**
     * 回复量
     */
    private int replyCount;

    /**
     * 评论回复用户昵称
     */
    private String replyNickname;

    /**
     * 文章/说说标题
     */
    private String objectTitle;

    /**
     * 评论类型: see {@link com.github.stazxr.zblog.domain.enums.CommentType}
     */
    private Integer type;

    /**
     * 评论对象
     */
    private Long objectId;

    /**
     * 是否审核
     */
    private Boolean isReview;

    /**
     * 回复列表
     */
    private List<CommentReplyVo> replyList;
}
