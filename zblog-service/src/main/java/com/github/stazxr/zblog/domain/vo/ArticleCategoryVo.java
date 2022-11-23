package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * CategoryVo
 *
 * @author SunTao
 * @since 2022-11-18
 */
@Getter
@Setter
public class ArticleCategoryVo extends BaseVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 上级分类
     */
    private Long pid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类预览图
     */
    private String imageUrl;

    /**
     * 分类描述
     */
    private String desc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 文章数
     */
    private Integer articleCount;

    /**
     * 子类列表
     */
    private List<ArticleCategoryVo> children;
}
