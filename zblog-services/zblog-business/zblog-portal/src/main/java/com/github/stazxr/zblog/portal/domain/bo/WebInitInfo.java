package com.github.stazxr.zblog.portal.domain.bo;

import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 网站初始化信息
 *
 * @author SunTao
 * @since 2027-08-22
 */
@Getter
@Setter
@ApiModel("网站初始化信息")
public class WebInitInfo implements Serializable {
    private static final long serialVersionUID = -2276604381760439948L;

    /**
     * 网站配置
     */
    @ApiModelProperty("网站配置")
    private WebsiteConfig config;

    /**
     * 网站封面
     */
    @ApiModelProperty("网站封面")
    private Map<String, List<ThemePageVo>> pages;

    /**
     * 网站链接
     */
    @ApiModelProperty("网站链接")
    private Map<String, String> links;
}
