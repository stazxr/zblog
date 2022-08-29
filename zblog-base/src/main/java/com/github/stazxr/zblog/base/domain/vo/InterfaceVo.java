package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.base.domain.enums.InterfaceType;
import lombok.Data;

/**
 * InterfaceVo
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Data
public class InterfaceVo {
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
     * 接口类型: see InterfaceType
     */
    private Integer type;

    /**
     * 创建时间
     */
    private String createTime;
}
