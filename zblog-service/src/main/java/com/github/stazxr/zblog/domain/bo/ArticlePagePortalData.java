package com.github.stazxr.zblog.domain.bo;

import com.github.stazxr.zblog.core.base.BaseVo;
import com.github.stazxr.zblog.domain.vo.ArticleTagVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 门户-文章页面信息列表
 *
 * @author SunTao
 * @since 2023-01-06
 */
@Getter
@Setter
@ToString
public class ArticlePagePortalData extends BaseVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleType}
     */
    private String articleTypeName;

    /**
     * 文章分类
     */
    private String categoryId;

    /**
     * 文章分类名称
     */
    private String categoryName;

    /**
     * 是否允许评论
     */
    private Boolean commentFlag;

    /**
     * 封面类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleImgType}
     */
    private Integer coverImageType;

    /**
     * 原文链接（转载或翻译需要填写该字段）
     */
    private String reprintLink;

    /**
     * 转载说明
     */
    private String reprintDesc;

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
     * 浏览量
     */
    private int viewCount;

    /**
     * 点赞量
     */
    private int likeCount;

    /**
     * 标签明细列表
     */
    private List<ArticleTagVo> tagList;

    /**
     * 封面链接列表
     */
    private List<String> articleImgLinkList;

    /**
     * 推荐文章列表
     */
    private List<ArticleSimpleData> recommendList;

    /**
     * 最新文章列表
     */
    private List<ArticleSimpleData> newestList;

    /**
     * 上一篇
     */
    private ArticleSimpleData lastArticle;

    /**
     * 下一篇
     */
    private ArticleSimpleData nextArticle;
}
