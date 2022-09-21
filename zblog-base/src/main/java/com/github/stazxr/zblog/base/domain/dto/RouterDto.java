package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

/**
 * 路由信息
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Data
public class RouterDto {
    /**
     * 请求地址
     */
    private String uri;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 日志是否展示
     */
    private Boolean logShowed;
}
