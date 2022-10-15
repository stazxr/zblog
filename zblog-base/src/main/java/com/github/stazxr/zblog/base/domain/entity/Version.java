package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统版本
 *
 * @author SunTao
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("version")
public class Version extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 版本描述
     */
    private String updateContent;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;
}
