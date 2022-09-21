package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;

/**
 * DictVo
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Data
public class DictVo {
    /**
     * ID
     */
    private Long id;

    /**
     * 字典名称
     */
    private String name;

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
     * 排序值
     */
    private Integer sort;

    /**
     * 是否允许编辑删除
     */
    private Boolean locked;

    /**
     * 字典状态
     */
    private Boolean enabled;
}
