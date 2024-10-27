package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 友链查询
 *
 * @author SunTao
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
@ApiModel("友链查询参数")
public class FriendLinkQueryDto extends PageParam {
    /**
     * 友链名称
     */
    @ApiModelProperty(value = "友链名称", notes = "模糊查询")
    private String name;

    /**
     * 友链状态
     */
    @ApiModelProperty("友链状态")
    private Boolean valid;
}
