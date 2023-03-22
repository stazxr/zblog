package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 弹幕查询参数
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
@ToString
@ApiModel("弹幕查询参数")
public class MessageQueryDto extends PageParam {
    /**
     * 弹幕内容
     */
    @ApiModelProperty(value = "弹幕内容")
    private String content;

    /**
     * 弹幕状态
     */
    @ApiModelProperty(value = "弹幕状态，1：已审核、2、未审核")
    private Integer status;
}
