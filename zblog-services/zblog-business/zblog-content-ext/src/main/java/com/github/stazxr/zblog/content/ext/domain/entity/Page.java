package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.content.ext.domain.enums.PageDisplayMode;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面
 *
 * @author SunTao
 * @since 2026-06-12
 */
@Getter
@Setter
@TableName("page")
public class Page extends BaseEntity {
    private static final long serialVersionUID = -1418611832649084034L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 页面标签
     */
    private String pageLabel;

    /**
     * 页面展示模式
     */
    private PageDisplayMode displayMode;

    /**
     * 页面排序
     */
    private Integer pageSort;
}
