package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 主题封面
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Getter
@Setter
@TableName("theme_page")
public class ThemePage extends BaseEntity {
    private static final long serialVersionUID = -4769835795977810805L;

    /**
     * 主题封面id
     */
    @TableId
    private Long id;

    /**
     * 主题编号
     */
    private Long themeId;

    /**
     * 页面编号
     */
    private Long pageId;

    /**
     * 封面地址
     */
    private String pageCover;
}