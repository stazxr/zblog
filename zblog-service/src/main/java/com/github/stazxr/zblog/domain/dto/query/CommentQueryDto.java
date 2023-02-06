package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论查询
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
@ToString
public class CommentQueryDto extends PageParam {
    /**
     * 页码
     */
    private Integer current;

    /**
     * 评论类型
     */
    private Integer type;

    /**
     * 评论对象
     */
    private Long objectId;

    /**
     * 上级评论
     */
    private Long parentId;

    /**
     * 评论状态
     */
    private Integer status;

    /**
     * 评论文章标题
     */
    private String articleTitle;

    /**
     * 评论说说标题
     */
    private String talkTitle;

    /**
     * 评论用户
     */
    private String nickname;
}
