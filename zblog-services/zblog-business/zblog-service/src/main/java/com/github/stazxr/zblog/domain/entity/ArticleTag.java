package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("article_tag")
public class ArticleTag extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 标签名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 标签类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleTagType}
     */
    private Integer type;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否已删除
     */
    @TableLogic
    private Boolean deleted;
}
