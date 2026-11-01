package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章微信公众号同步配置
 *
 * @author SunTao
 * @since 2026-11-01
 */
@Getter
@Setter
@TableName("article_wechat_sync_config")
public class ArticleWechatSyncConfig implements Serializable {
    private static final long serialVersionUID = -1474172623049018532L;

    /**
     * 配置id
     */
    @TableId
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 同步的文章版本
     */
    private Integer articleVersion;

    /**
     * 微信公众号草稿id
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String mediaId;

    /**
     * 微信公众号封面素材id
     */
    private String thumbMediaId;

    /**
     * 微信公众号封面素材url
     */
    private String thumbMediaUrl;

    /**
     * 微信公众号文章标题
     */
    private String wechatTitle;

    /**
     * 微信公众号文章作者
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String wechatAuthor;

    /**
     * 微信公众号文章摘要
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String wechatDigest;

    /**
     * 同步状态
     */
    private Integer syncStatus;

    /**
     * 错误信息
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
