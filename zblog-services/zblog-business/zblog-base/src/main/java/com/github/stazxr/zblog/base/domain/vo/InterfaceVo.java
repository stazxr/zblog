package com.github.stazxr.zblog.base.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * InterfaceVo
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Getter
@Setter
public class InterfaceVo {
    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口编码
     */
    private String code;

    /**
     * 接口级别
     */
    private Integer level;

    /**
     * 接口状态
     */
    private Integer status;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * 请求方式
     */
    private String method;
}
