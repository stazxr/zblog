package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.core.util.ToStringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页查询参数基类
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Getter
@Setter
@ApiModel("分页参数")
public class PageParam implements Serializable {
    private static final long serialVersionUID = 1L;

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
        if (page == null || pageSize == null) {
            ThrowUtils.system(BaseErrorCode.SCOREB000);
        }
    }

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
