package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.impl.BaseEntityWithExtend;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章
 *
 * @author SunTao
 * @since 2021-01-18
 */
@Getter
@Setter
@TableName("article")
public class Article extends BaseEntityWithExtend {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 概要
     */
    private String remark;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章内容（去除标签）
     */
    private String contentMd;

    /**
     * 文章关键字
     */
    private String keywords;

    /**
     * 原文链接（转载或翻译需要填写该字段）
     */
    private String reprintLink;

    /**
     * 转载说明
     */
    private String reprintDesc;

    /**
     * 封面类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleImgType}
     */
    private Integer coverImageType;

    /**
     * 文章类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleType}
     */
    private Integer articleType;

    /**
     * 文章状态: see {@link com.github.stazxr.zblog.domain.enums.ArticleStatus}
     */
    private Integer articleStatus;

    /**
     * 文章权限: see {@link com.github.stazxr.zblog.domain.enums.ArticlePerm}
     */
    private Integer articlePerm;

    /**
     * 文章分类
     */
    private Long categoryId;

    /**
     * 作者
     */
    private Long authorId;

    /**
     * 是否允许评论
     */
    private Boolean commentFlag;

    /**
     * 其他信息
     */
    @TableField(value = "`DESC`")
    private String desc;

    /**
     * 是否已删除（使用逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;
}
