package com.github.stazxr.zblog.base.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
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
 * 修订版本
 *
 * @author SunTao
 * @since 2022-10-14
 */
@Getter
@Setter
@ApiModel("版本DTO")
public class VersionDto extends BaseDto {
    private static final long serialVersionUID = -2864645948448605939L;

    /**
     * 版本id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("版本id")
    private Long id;

    /**
     * 版本名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{VERSION_VERSIONNAME_REQUIRED}")
    @ApiModelProperty("版本名称")
    private String versionName;

    /**
     * 版本描述
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{VERSION_UPDATECONTENT_REQUIRED}")
    @ApiModelProperty("版本描述")
    private String updateContent;

    /**
     * 版本排序
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PARAM_SORT_REQUIRED}")
    @Min(value = 1, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MIN1}")
    @Max(value =99999, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MAX99999}")
    @ApiModelProperty("版本排序")
    private Integer sort;
}
