package com.github.stazxr.zblog.content.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 标签查询
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
@ApiModel("标签查询参数")
public class TagQueryDto extends PageParam {
    private static final long serialVersionUID = 2296445247589027937L;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", notes = "模糊查询")
    private String name;

    /**
     * SLUG
     */
    @ApiModelProperty("SLUG")
    private String slug;

    /**
     * 是否允许搜索引擎收录
     */
    @ApiModelProperty("是否允许搜索引擎收录")
    private Boolean allowIndex;

    /**
     * 标签状态
     */
    @ApiModelProperty("标签状态")
    private Boolean enabled;
}
