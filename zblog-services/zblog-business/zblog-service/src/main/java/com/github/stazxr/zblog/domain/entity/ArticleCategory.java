package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("article_category")
public class ArticleCategory extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 上级分类
     */
    @TableField(value = "`PID`")
    private Long pid;

    /**
     * 分类名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 分类预览图
     */
    private String imageUrl;

    /**
     * 分类描述
     */
    @TableField(value = "`DESC`")
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
     * 是否已删除（逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;
}
