package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 栏目信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
public class ArticleColumnDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 栏目名称
     */
    private String name;

    /**
     * 栏目预览图
     */
    private String imageUrl;

    /**
     * 栏目描述
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
