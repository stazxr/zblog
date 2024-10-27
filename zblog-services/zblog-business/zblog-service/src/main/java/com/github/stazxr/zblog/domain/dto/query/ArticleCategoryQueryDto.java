package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 类别查询
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
@ToString
@ApiModel("类别查询参数")
public class ArticleCategoryQueryDto extends PageParam {
    /**
     * 类别名称
     */
    @ApiModelProperty("类别名称")
    private String name;

    /**
     * 类别状态
     */
    @ApiModelProperty("类别状态")
    private Boolean enabled;

    /**
     * 是否只查询一级的类别列表
     */
    @ApiModelProperty("是否只查询一级的类别列表")
    private Boolean firstLevel;
}
