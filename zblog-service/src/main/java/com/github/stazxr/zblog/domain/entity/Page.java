package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Getter
@Setter
@TableName("page")
public class Page extends BaseEntity {
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
     * 页面封面
     */
    private String pageCover;

    /**
     * 页面排序
     */
    private Integer pageSort;
}
