package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ArticleColumnVo
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
public class ArticleColumnVo extends BaseVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 专栏名称
     */
    private String name;

    /**
     * 专栏预览图
     */
    private String imageUrl;

    /**
     * 专栏描述
     */
    private String desc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否首页显示
     */
    private Boolean pageShow;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 文章数
     */
    private Integer articleCount;

    /**
     * 文章列表
     */
    List<ArticleColumnArticleVo> articles;
}
