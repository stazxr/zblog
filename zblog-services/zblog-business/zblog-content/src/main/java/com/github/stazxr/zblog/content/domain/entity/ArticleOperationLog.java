package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章操作日志
 *
 * @author SunTao
 * @since 2026-10-27
 */
@Getter
@Setter
@TableName("article_operation_log")
public class ArticleOperationLog implements Serializable {
    private static final long serialVersionUID = 3864289666283686665L;

    /**
     * 操作日志id
     */
    @TableId
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 操作人id
     */
    private Long operatorId;

    /**
     * 操作类型
     */
    private Integer operationType;

    /**
     * 操作前文章类型
     */
    private Integer beforeType;

    /**
     * 操作后文章类型
     */
    private Integer afterType;

    /**
     * 操作前文章权限
     */
    private Integer beforePerm;

    /**
     * 操作后文章权限
     */
    private Integer afterPerm;

    /**
     * 操作前文章状态
     */
    private Integer beforeStatus;

    /**
     * 操作后文章状态
     */
    private Integer afterStatus;

    /**
     * 关联的正式版本号
     */
    private Integer version;

    /**
     * 操作说明
     */
    private String remark;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;
}
