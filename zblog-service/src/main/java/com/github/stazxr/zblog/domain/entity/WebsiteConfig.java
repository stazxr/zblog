package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 网站配置
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Getter
@Setter
@TableName("website_config")
public class WebsiteConfig extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置信息
     */
    private String config;
}
