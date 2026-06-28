package com.github.stazxr.zblog.content.ext.domain.vo;

import com.github.stazxr.zblog.content.ext.domain.enums.PageDisplayMode;
import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面信息
 *
 * @author SunTao
 * @since 2026-06-12
 */
@Getter
@Setter
@ApiModel("页面VO")
public class PageVo extends BaseVo {
    private static final long serialVersionUID = -2547228956874761989L;

    /**
     * 页面id
     */
    @ApiModelProperty("页面id")
    private Long id;

    /**
     * 页面名称
     */
    @ApiModelProperty("页面名称")
    private String pageName;

    /**
     * 页面标签
     */
    @ApiModelProperty("页面标签")
    private String pageLabel;

    /**
     * 页面展示模式
     */
    @ApiModelProperty("页面展示模式")
    private PageDisplayMode displayMode;

    /**
     * 页面排序
     */
    @ApiModelProperty("页面排序")
    private Integer pageSort;
}
