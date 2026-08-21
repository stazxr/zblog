package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 网站链接配置
 *
 * @author SunTao
 * @since 2026-08-20
 */
@Getter
@Setter
@TableName("website_link_config")
public class WebsiteLinkConfig implements Serializable {
    private static final long serialVersionUID = 8412273880734519007L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 链接名称
     */
    private String linkName;

    /**
     * 链接类型
     */
    private String linkType;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * 图标
     */
    private String linkIcon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}