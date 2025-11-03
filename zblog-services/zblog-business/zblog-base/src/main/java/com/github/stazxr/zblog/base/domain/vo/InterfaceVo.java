package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口信息
 *
 * @author SunTao
 * @since 2025-11-03
 */
@Getter
@Setter
public class InterfaceVo extends BaseVo {
    private static final long serialVersionUID = -6011107531417391695L;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口编码
     */
    private String interfaceCode;

    /**
     * 请求地址
     */
    private String interfaceUri;

    /**
     * 请求方式
     */
    private String interfaceMethod;

    /**
     * 接口级别
     */
    private Integer interfaceLevel;

    /**
     * 接口状态
     */
    private Integer interfaceStatus;
}
