package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章栏目
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
     * 栏目名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 栏目预览图
     */
    private String imageUrl;

    /**
     * 栏目描述
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
