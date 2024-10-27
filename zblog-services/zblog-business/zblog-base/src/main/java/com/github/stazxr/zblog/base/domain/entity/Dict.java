package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 字典名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 父ID
     */
    @TableField(value = "`PID`")
    private Long pid;

    /**
     * 字典 key
     */
    @TableField(value = "`KEY`")
    private String key;

    /**
     * 字典 value
     */
    @TableField(value = "`VALUE`")
    private String value;

    /**
     * 类型，1：组；2：项
     */
    @TableField(value = "`TYPE`")
    private Integer type;

    /**
     * 字典描述
     */
    @TableField(value = "`DESC`")
    private String desc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否锁定
     */
    private Boolean locked;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;
}
