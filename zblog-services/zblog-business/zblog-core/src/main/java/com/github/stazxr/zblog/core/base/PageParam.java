package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.util.Assert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * PageParam
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Getter
@Setter
@ApiModel("分页参数")
public class PageParam implements Serializable {
    /**
     * 页码
     */
    @ApiModelProperty(name = "page", value = "页码", example = "1")
    private Integer page;

    /**
     * 每页显示的数目
     */
    @ApiModelProperty(name = "pageSize", value = "每页显示的数目", example = "10")
    private Integer pageSize;

    @ApiModelProperty(hidden = true)
    private Integer defaultPage = 1;

    @ApiModelProperty(hidden = true)
    private Integer defaultPageSize = 10;

    public void checkPage() {
        Assert.notNull(page, "参数【page】不能为空");
        Assert.notNull(pageSize, "参数【pageSize】不能为空");
    }
}
