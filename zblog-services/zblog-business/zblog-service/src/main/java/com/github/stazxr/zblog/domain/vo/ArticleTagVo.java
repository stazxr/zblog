package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * ArticleTagVo
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
public class ArticleTagVo extends BaseVo {
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

    /**
     * 文章数
     */
    private Integer articleCount;
}
