package com.github.stazxr.zblog.domain.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 社交信息
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Data
@ApiModel("网站社交信息")
public class SocialInfo {
    /**
     * QQ
     */
    @ApiModelProperty("QQ")
    private String qq = "";

    /**
     * 微信
     */
    @ApiModelProperty("微信")
    private String weChat = "";

    /**
     * Github
     */
    @ApiModelProperty("Github")
    private String github = "";

    /**
     * Gitee
     */
    @ApiModelProperty("Gitee")
    private String gitee = "";

    /**
     * CSDN
     */
    @ApiModelProperty("CSDN")
    private String csdn = "";
}
