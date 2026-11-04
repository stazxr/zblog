package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章内容（逻辑对象）
 *
 * @author SunTao
 * @since 2026-11-01
 */
@Getter
@Setter
@TableName("article_file")
public class ArticleFile implements Serializable {
    private static final long serialVersionUID = 8332431022801860929L;

    /**
     * 文件id
     */
    @TableId
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 物理文件id
     */
    private Long storageId;

    /**
     * 原文件名称
     */
    private String originalFilename;

    /**
     * 访问地址（冗余存储）
     */
    private String fileAccessUrl;

    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;
}
