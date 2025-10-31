package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典
 *
 * @author SunTao
 * @since 2021-02-20
 */
@Getter
@Setter
@TableName("dict")
public class Dict extends BaseEntity {
    private static final long serialVersionUID = 5454007640493795517L;

    /**
     * 字典ID
     */
    @TableId
    private Long id;

    /**
     * 上级字典ID
     */
    private Long pid;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private Integer dictType;

    /**
     * 字典key
     */
    private String dictKey;

    /**
     * 字典Value
     */
    private String dictValue;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典描述
     */
    private String dictDesc;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
