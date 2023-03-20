package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.util.Assert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * PageParam
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Getter
@Setter
@ApiModel
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

    /**
     * 以下列格式排序标准：property[,asc | desc]。
     * 默认排序顺序为升序。支持多种排序条件：
     * 如：id,asc
     */
    @ApiModelProperty(name = "sort", value = "排序", notes = "以下列格式排序标准：property[,asc | desc]", example = "id,asc")
    private List<String> sort;

    private Integer defaultPage = 1;

    private Integer defaultPageSize = 10;

    public void checkPage() {
        Assert.notNull(page, "参数【page】不能为空");
        Assert.notNull(pageSize, "参数【pageSize】不能为空");
    }
}
