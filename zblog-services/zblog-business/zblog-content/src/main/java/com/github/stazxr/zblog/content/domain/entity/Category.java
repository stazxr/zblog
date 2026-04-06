package com.github.stazxr.zblog.content.domain.entity;

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
     * 唯一标识(URL)
     */
    private String slug;

    /**
     * 分类图片
     */
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
     * 是否允许搜索引擎收录
     */
    private Boolean allowIndex;

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
