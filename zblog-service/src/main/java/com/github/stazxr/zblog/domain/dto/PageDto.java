package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 页面信息
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Getter
@Setter
@ToString
@ApiModel("页面信息")
public class PageDto {
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
     * 页面封面
     */
    @ApiModelProperty("页面封面")
    private String pageCover;

    /**
     * 页面排序
     */
    @ApiModelProperty("页面排序")
    private Integer pageSort;
}
