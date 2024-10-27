package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.base.domain.enums.DictType;
import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典信息
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
public class DictVo extends BaseVo {
    /**
     * ID
     */
    private Long id;

    /**
     * PID
     */
    private Long pid;

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
     * 字典类型，1：组；2：项
     */
    private Integer type;

    /**
     * 字典排序
     */
    private Integer sort;

    /**
     * 是否锁定
     */
    private Boolean locked;

    /**
     * 字典状态
     */
    private Boolean enabled;

    /**
     * 子节点数目
     */
    private int subCount;

    /**
     * 是否包含子节点
     */
    private Boolean hasChildren;

    public Boolean getHasChildren() {
        if (DictType.ITEM.getValue().equals(this.type)) {
            return false;
        }

        return subCount > 0;
    }
}
