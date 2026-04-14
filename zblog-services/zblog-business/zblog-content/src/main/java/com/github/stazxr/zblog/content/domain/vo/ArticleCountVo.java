package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章数量统计
 *
 * 用于文章列表页各状态数量统计展示
 *
 * @author SunTao
 * @since 2023-01-03
 */
@Getter
@Setter
@ApiModel("文章数量统计")
public class ArticleCountVo implements Serializable {
    private static final long serialVersionUID = 1626944225703433965L;

    /**
     * 全部文章数量
     */
    private int totalCount;

    /**
     * 公开文章数量
     */
    private int publicCount;

    /**
     * 私密文章数量
     */
    private int privateCount;

    /**
     * 待审核数量
     */
    private int pendingAuditCount;

    /**
     * 待发布数量
     */
    private int pendingPublishCount;

    /**
     * 待处理数量（如审核不通过、待整改）
     */
    private int pendingDealCount;

    /**
     * 草稿箱数量
     */
    private int draftCount;

    /**
     * 回收站数量
     */
    private int recycleCount;
}
