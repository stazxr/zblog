package com.github.stazxr.zblog.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 前台页面信息
 *
 * @author SunTao
 * @since 2022-12-25
 */
@Getter
@Setter
@ToString
public class PageInfo {
    /**
     * 页面ID
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
     * 封面地址
     */
    private String pageCover;
}
