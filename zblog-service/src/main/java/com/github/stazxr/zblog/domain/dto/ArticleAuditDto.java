package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 文章审核信息
 *
 * @author SunTao
 * @since 2023-01-26
 */
@Getter
@Setter
@ToString
public class ArticleAuditDto {
    /**
     * 审核状态
     */
    private Boolean status;

    /**
     * 审核意见
     */
    private String opinion;

    /**
     * 文章列表
     */
    private List<Long> articleIds;
}
