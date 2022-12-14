package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 页面信息
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Getter
@Setter
@ToString
public class PageDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 页面标签
     */
    private String pageLabel;

    /**
     * 页面封面
     */
    private String pageCover;

    /**
     * 页面排序
     */
    private Integer pageSort;
}
