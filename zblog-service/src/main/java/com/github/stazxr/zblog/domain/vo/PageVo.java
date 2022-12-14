package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面页面展示信息
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Getter
@Setter
public class PageVo extends BaseVo {
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
