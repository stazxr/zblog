package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章标签
 *
 * @author SunTao
 * @since 2020-12-20
 */
@Getter
@Setter
@TableName("tag")
public class Tag extends BaseEntity {
    private static final long serialVersionUID = -7232299087107540561L;

    /**
     * 标签ID
     */
    @TableId
    private Long id;

    /**
     * 标签名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 唯一标识(URL)
     */
    private String slug;

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
     * 是否启用
     */
    private Boolean enabled;
}
