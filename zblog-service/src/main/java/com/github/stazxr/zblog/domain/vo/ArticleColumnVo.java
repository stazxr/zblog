package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * ArticleColumnVo
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
public class ArticleColumnVo extends BaseVo {
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
