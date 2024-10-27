package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 节点查询
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Getter
@Setter
@ToString
@ApiModel("节点查询参数")
public class NodeQueryDto extends PageParam {
    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称", notes = "模糊查询")
    private String name;

    /**
     * 节点ip
     */
    @ApiModelProperty(value = "节点ip", notes = "模糊查询")
    private String ip;
}
