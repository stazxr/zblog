package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章专栏
 *
 * @author SunTao
 * @since 2022-05-31
 */
@Getter
@Setter
@TableName("article_column")
public class ArticleColumn extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 专栏名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 专栏预览图
     */
    private String imageUrl;

    /**
     * 专栏描述
     */
    @TableField(value = "`DESC`")
    private String desc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否首页展示
     */
    private Boolean pageShow;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
