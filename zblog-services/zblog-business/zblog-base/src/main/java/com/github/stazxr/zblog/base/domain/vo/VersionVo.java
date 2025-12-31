package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 修订版本
 *
 * @author SunTao
 * @since 2022-10-14
 */
@Getter
@Setter
@ApiModel("版本VO")
public class VersionVo extends BaseVo {
    private static final long serialVersionUID = -2864645948448605939L;

    /**
     * 版本id
     */
    @ApiModelProperty("版本id")
    private Long id;

    /**
     * 版本名称
     */
    @ApiModelProperty("版本名称")
    private String versionName;

    /**
     * 版本描述
     */
    @ApiModelProperty("版本描述")
    private String updateContent;

    /**
     * 版本排序，默认99999
     */
    @ApiModelProperty("版本排序")
    private Integer sort;
}
