package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 路由综合信息
 *
 * @author SunTao
 * @since 2022-06-24
 */
@Data
@ToString
public class RouterVo {
    /**
     * 路由名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 权限编码
     */
    private Long permId;

    /**
     * 权限是否启用
     */
    private Boolean permEnabled;

    /**
     * 权限访问级别
     */
    private Integer permLevel;

    /**
     * 日志是否展示
     */
    private Boolean logShowed;
}
