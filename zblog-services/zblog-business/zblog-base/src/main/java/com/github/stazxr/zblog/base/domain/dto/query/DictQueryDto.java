package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典查询
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
@ToString
@ApiModel("字典查询参数")
public class DictQueryDto extends PageParam {
    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称", notes = "模糊查询")
    private String dictName;

    /**
     * 字典Key
     */
    @ApiModelProperty(value = "字典Key", notes = "模糊查询")
    private String dictKey;

    /**
     * 字典状态
     */
    @ApiModelProperty("字典状态")
    private Boolean enabled;
}
