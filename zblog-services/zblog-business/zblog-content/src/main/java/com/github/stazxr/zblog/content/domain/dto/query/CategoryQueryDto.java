package com.github.stazxr.zblog.content.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分类查询
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
@ApiModel("分类查询参数")
public class CategoryQueryDto extends PageParam {
    private static final long serialVersionUID = -4528031009059460582L;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 分类唯一标识
     */
    @ApiModelProperty("分类唯一标识")
    private String slug;

    /**
     * 是否允许搜索引擎收录
     */
    @ApiModelProperty("是否允许搜索引擎收录")
    private Boolean allowIndex;

    /**
     * 是否前台展示
     */
    @ApiModelProperty("是否前台展示")
    private Boolean visible;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * PID
     */
    @ApiModelProperty("PID")
    private Long pid;
}
