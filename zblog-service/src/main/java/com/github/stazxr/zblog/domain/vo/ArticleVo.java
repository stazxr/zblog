package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 文章信息
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Getter
@Setter
@ToString
public class ArticleVo extends BaseVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleType}
     */
    private Integer articleType;

    /**
     * 文章分类
     */
    private String categoryName;

    /**
     * 标签列表
     */
    private List<ArticleTagVo> tagList;

    /**
     * 浏览量
     */
    private int viewCount;

    /**
     * 点赞量
     */
    private int likeCount;

    /**
     * 文章状态: see {@link com.github.stazxr.zblog.domain.enums.ArticleStatus}
     */
    private Integer articleStatus;

    /**
     * 作者
     */
    private Long authorId;

    /**
     * 作者昵称
     */
    private String authorNickname;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 文章权限: see {@link com.github.stazxr.zblog.domain.enums.ArticlePerm}
     */
    private Integer articlePerm;

    /**
     * 是否允许评论
     */
    private Boolean commentFlag;

    /**
     * 评论数
     */
    private int commentCount;
}
