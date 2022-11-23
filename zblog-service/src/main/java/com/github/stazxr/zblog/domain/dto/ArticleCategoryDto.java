package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典查询
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
public class ArticleCategoryDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 上级分类
     */
    private Long pid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类预览图
     */
    private String imageUrl;

    /**
     * 分类描述
     */
    private String desc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
