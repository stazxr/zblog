package com.github.stazxr.zblog.content.ext.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 网站链接配置信息
 *
 * @author SunTao
 * @since 2026-08-22
 */
@Getter
@Setter
@ApiModel("网站配置VO")
public class WebsiteLinkConfigVo implements Serializable {
    private static final long serialVersionUID = 620060912562241496L;

    /**
     * 主键
     */
    @ApiModelProperty("网站链接配置id")
    private Long id;

    /**
     * 链接名称
     */
    @ApiModelProperty("链接名称")
    private String linkName;

    /**
     * 链接类型
     */
    @ApiModelProperty("链接类型")
    private String linkType;

    /**
     * 链接地址
     */
    @ApiModelProperty("链接地址")
    private String linkUrl;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String linkIcon;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}