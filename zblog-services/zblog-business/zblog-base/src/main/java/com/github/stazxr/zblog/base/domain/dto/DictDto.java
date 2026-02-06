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
 * DictDto
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Getter
@Setter
@ApiModel("字典DTO")
public class DictDto extends BaseDto {
    private static final long serialVersionUID = 517310641739041347L;

    /**
     * 字典ID
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("字典id")
    private Long id;

    /**
     * 上级字典ID
     */
    @ApiModelProperty("字典pid")
    private Long pid;

    /**
     * 字典名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{DICT_DICTNAME_REQUIRED}")
    @ApiModelProperty("字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("字典类型")
    private Integer dictType;

    /**
     * 字典key
     */
    @ApiModelProperty("字典key")
    private String dictKey;

    /**
     * 字典Value
     */
    @ApiModelProperty("字典value")
    private String dictValue;

    /**
     * 字典排序
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PARAM_SORT_REQUIRED}")
    @Min(value = 1, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MIN1}")
    @Max(value =99999, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MAX99999}")
    @ApiModelProperty("字典排序")
    private Integer dictSort;

    /**
     * 字典描述
     */
    @ApiModelProperty("字典描述")
    private String dictDesc;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
