package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 日历信息
 *
 * @author SunTao
 * @since 2022-04-06
 */
@Getter
@Setter
@TableName("calendar")
public class Calendar extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 日期（yyyy-MM-dd）
     */
    @TableField(value = "`DATE`")
    private String date;

    /**
     * 标识（1：工作日；0：节假日）
     */
    @TableField(value = "`FLAG`")
    private String flag;

    /**
     * 备注
     */
    @TableField(value = "`REMARK`")
    private String remark;
}
