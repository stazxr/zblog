package com.github.stazxr.zblog.base.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 激活的存储配置类别信息
 *
 * @author SunTao
 * @since 2022-11-01
 */
@Getter
@Setter
public class ActiveStorageTypeVo {
    /**
     * 系统配置的存储类型
     */
    private Integer defaultType;

    /**
     * 系统配置的存储类型名称
     */
    private String defaultTypeName;

    /**
     * 激活的存储类型
     */
    private Integer activeType;

    /**
     * 激活的存储类型名称
     */
    private String activeTypeName;
}
