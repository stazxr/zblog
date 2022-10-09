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
     * 字典序列（黑白名单用）
     */
    private Long dictId;

    /**
     * 字典序列
     */
    private Long id;

    /**
     * 父序列
     */
    private Long pid;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典类型
     */
    private Integer type;

    /**
     * 字典KEY
     */
    private String key;

    /**
     * 字典VALUE
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
