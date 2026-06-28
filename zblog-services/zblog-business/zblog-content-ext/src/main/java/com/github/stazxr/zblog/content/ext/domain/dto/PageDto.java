package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.ext.domain.enums.PageDisplayMode;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 页面信息
 *
 * @author SunTao
 * @since 2026-06-12
 */
@Getter
@Setter
@ApiModel("页面DTO")
public class PageDto extends BaseDto {
    private static final long serialVersionUID = -7906206907400313717L;

    /**
     * 页面id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("页面id")
    private Long id;

    /**
     * 页面名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{PAGE_PAGENAME_REQUIRED}")
    @ApiModelProperty("页面名称")
    private String pageName;

    /**
     * 页面标签
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{PAGE_PAGELABEL_REQUIRED}")
    @ApiModelProperty("页面标签")
    private String pageLabel;

    /**
     * 页面展示模式
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PAGE_DISPLAYMODE_REQUIRED}")
    @ApiModelProperty("页面展示模式")
    private PageDisplayMode displayMode;

    /**
     * 页面排序
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PARAM_SORT_REQUIRED}")
    @Min(value = 1, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MIN1}")
    @Max(value =99999, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MAX99999}")
    @ApiModelProperty("页面排序")
    private Integer pageSort;
}
