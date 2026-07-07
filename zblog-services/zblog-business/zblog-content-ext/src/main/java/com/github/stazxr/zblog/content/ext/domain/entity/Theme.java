package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.content.ext.domain.enums.ThemeOwnerType;
import com.github.stazxr.zblog.content.ext.domain.enums.ThemeType;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 主题
 *
 * <p>
 * 系统主题：
 * OWNER_TYPE=0
 *
 * 用户主题：
 * OWNER_TYPE=1
 * OWNER_ID=用户ID
 * </p>
 *
 * <p>
 * 系统主题可设置默认主题(IS_DEFAULT)
 * 用户主题可设置激活主题(IS_ACTIVE)
 * </p>
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Getter
@Setter
@TableName("theme")
public class Theme extends BaseEntity {
    private static final long serialVersionUID = -3434434170467181151L;

    /**
     * 主题id
     */
    @TableId
    private Long id;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 主题类型
     */
    private ThemeType themeType;

    /**
     * 主题归属
     */
    private ThemeOwnerType ownerType;

    /**
     * 所属用户
     */
    private Long ownerId;

    /**
     * 是否默认主题
     */
    private Boolean isDefault;

    /**
     * 是否激活主题
     */
    private Boolean isActive;

    /**
     * 主题预览图
     */
    private String previewCover;

    /**
     * 主题描述
     */
    private String description;
}