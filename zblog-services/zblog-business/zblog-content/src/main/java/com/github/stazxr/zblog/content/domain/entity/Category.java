package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章分类
 *
 * @author SunTao
 * @since 2021-01-17
 */
@Getter
@Setter
@TableName("category")
public class Category extends BaseEntity {
    private static final long serialVersionUID = -3089181571970402860L;

    /**
     * 分类ID
     */
    @TableId
    private Long id;

    /**
     * 父分类ID
     */
    @TableField(value = "`PID`")
    private Long pid;

    /**
     * 分类名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 路径标识
     */
    private String slug;

    /**
     * 分类封面图
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String imageUrl;

    /**
     * 分类描述
     */
    private String description;

    /**
     * SEO标题
     */
    private String seoTitle;

    /**
     * SEO关键词
     */
    private String seoKeywords;

    /**
     * SEO描述
     */
    private String seoDescription;

    /**
     * SEO收录配置
     */
    private Boolean seoSearch;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否前台展示
     */
    private Boolean visible;
}
