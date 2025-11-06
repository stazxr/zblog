package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口查询参数
 *
 * @author SunTao
 * @since 2022-09-16
 */
@Getter
@Setter
@ApiModel("接口查询参数")
public class InterfaceQueryDto extends PageParam {
    private static final long serialVersionUID = -6587683908302795254L;

    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称", notes = "模糊查询")
    private String interfaceName;

    /**
     * 接口编码
     */
    @ApiModelProperty(value = "接口编码", notes = "模糊查询")
    private String interfaceCode;

    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址", notes = "模糊查询")
    private String interfaceUri;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    private String interfaceMethod;

    /**
     * 接口级别
     */
    @ApiModelProperty(value = "接口级别")
    private Integer interfaceLevel;

    /**
     * 接口状态
     */
    @ApiModelProperty(value = "接口状态")
    private Integer interfaceStatus;
}
