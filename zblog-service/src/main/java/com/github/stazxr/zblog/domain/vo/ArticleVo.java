package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
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
     * 文章内容
     */
    private String content;

    /**
     * 文章关键字
     */
    private String keywords;

    /**
     * 文章摘要
     */
    private String remark;

    /**
     * 文章类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleType}
     */
    private Integer articleType;

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
     * 文章权限: see {@link com.github.stazxr.zblog.domain.enums.ArticlePerm}
     */
    private Integer articlePerm;

    /**
     * 文章状态: see {@link com.github.stazxr.zblog.domain.enums.ArticleStatus}
     */
    private Integer articleStatus;

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
     * 作者
     */
    private Long authorId;

    /**
     * 作者昵称
     */
    private String authorNickname;

    /**
     * 作者性别
     */
    private Integer authorGender;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 备注
     */
    private String desc;

    /**
     * 字数
     */
    private String wordCount;

    /**
     * 浏览量
     */
    private int viewCount;

    /**
     * 点赞量
     */
    private int likeCount;

    /**
     * 评论数
     */
    private int commentCount;

    /**
     * 标签明细列表
     */
    private List<ArticleTagVo> tagList;

    /**
     * 封面列表
     */
    private List<File> articleImg;

    /**
     * 标签序号列表
     */
    private List<Long> articleTag;

    /**
     * 封面链接列表
     */
    private List<String> articleImgLinkList;

    /**
     * 上一篇文章
     */
    private ArticleVo lastArticle;

    /**
     * 下一篇文章
     */
    private ArticleVo nextArticle;

    /**
     * 推荐列表
     */
    private List<ArticleVo> recommendList;

    /**
     * 最新列表
     */
    private List<ArticleVo> newestList;

    public List<String> getArticleImgLinkList() {
        List<String> tmp = new ArrayList<>();
        if (this.articleImg != null) {
            for (File file : articleImg) {
                tmp.add(file.getDownloadUrl());
            }
        }
        return tmp;
    }

    public List<Long> getArticleTag() {
        List<Long> tmp = new ArrayList<>();
        if (this.tagList != null) {
            for (ArticleTagVo tagVo : tagList) {
                tmp.add(tagVo.getId());
            }
        }
        return tmp;
    }
}
