package com.github.stazxr.zblog.domain.bo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文章数量统计
 *
 * @author SunTao
 * @since 2023-01-03
 */
@Getter
@Setter
@ToString
public class ArticleCountData {
    /**
     * 全部
     */
    private int allCount;

    /**
     * 全部可见
     */
    private int publicCount;

    /**
     * 仅我可见
     */
    private int privateCount;

    /**
     * 待审核
     */
    private int auditCount;

    /**
     * 待发布
     */
    private int publishCount;

    /**
     * 待处理
     */
    private int dealCount;

    /**
     * 草稿箱
     */
    private int draftCount;

    /**
     * 回收站
     */
    private int deleteCount;

    /**
     * 已发布
     */
    private int publishedCount;
}
