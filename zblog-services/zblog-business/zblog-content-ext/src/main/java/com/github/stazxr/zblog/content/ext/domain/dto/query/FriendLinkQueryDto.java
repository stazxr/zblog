package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 友链查询
 *
 * @author SunTao
 * @since 2026-04-15
 */
@Getter
@Setter
@ApiModel("友链查询参数")
public class FriendLinkQueryDto extends PageParam {
    private static final long serialVersionUID = -1214395791386269505L;

    /**
     * 网站名称
     */
    @ApiModelProperty("网站名称")
    private String name;

    /**
     * 网站地址
     */
    @ApiModelProperty("网站地址")
    private String url;

    /**
     * 友链状态
     */
    @ApiModelProperty("友链状态")
    private Integer status;

    /**
     * 是否展示
     */
    @ApiModelProperty("是否展示")
    private Boolean isVisible;

    /**
     * 是否允许传递SEO权重
     */
    @ApiModelProperty("是否允许传递SEO权重")
    private Boolean allowFollow;
}
