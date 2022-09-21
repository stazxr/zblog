package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

/**
 * 字典信息
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Data
public class DictDto {
    /**
     * 字典序列
     */
    private Long id;

    /**
     * 字典序列
     */
    private Long dictId;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典键
     */
    private String key;

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典描述
     */
    private String desc;

    /**
     * 字典排序
     */
    private String sort;

    /**
     * 字典状态
     */
    private Boolean enabled;
}
