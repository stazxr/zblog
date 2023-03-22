package com.github.stazxr.zblog.domain.dto.query;

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
public class ArticleTagQueryDto extends PageParam {
    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", notes = "模糊查询")
    private String name;

    /**
     * 创建用户
     */
    @ApiModelProperty("创建用户")
    private String createUser;

    /**
     * 标签类型
     */
    @ApiModelProperty("标签类型，1：公共、2：定制")
    private Integer type;

    /**
     * 标签状态
     */
    @ApiModelProperty("标签状态")
    private Boolean enabled;
}
