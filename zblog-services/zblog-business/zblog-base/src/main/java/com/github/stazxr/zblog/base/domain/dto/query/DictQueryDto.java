package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典查询参数
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
@ApiModel("字典查询参数")
public class DictQueryDto extends PageParam {
    private static final long serialVersionUID = -8231077447608203396L;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称", notes = "模糊查询")
    private String dictName;
}
