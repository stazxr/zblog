package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章定时发布任务记录
 *
 * @author SunTao
 * @since 2026-10-28
 */
@Getter
@Setter
@TableName("article_auto_publish_timing")
public class ArticleAutoPublishTiming implements Serializable {
    private static final long serialVersionUID = 7714558146678149019L;

    /**
     * 记录id
     */
    @TableId
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章当前已发布版本
     */
    private Integer version;

    /**
     * 计划发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 任务状态：0待发布 1执行中 2已发布 3失败 4已取消
     */
    private Integer status;

    /**
     * 已重试次数
     */
    private Integer retryCount;

    /**
     * 最近一次执行失败原因
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 任务完成时间
     */
    private LocalDateTime finishTime;
}
