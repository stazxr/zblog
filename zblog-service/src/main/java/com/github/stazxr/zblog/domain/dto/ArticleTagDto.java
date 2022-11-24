package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 标签信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
public class ArticleTagDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签类型
     */
    private Integer type;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
