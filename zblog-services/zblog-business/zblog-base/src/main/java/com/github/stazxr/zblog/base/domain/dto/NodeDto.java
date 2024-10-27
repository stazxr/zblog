package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 节点信息
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Data
@ApiModel("节点信息")
public class NodeDto {
    /**
     * 节点id
     */
    @ApiModelProperty("节点id")
    private Long id;

    /**
     * 节点名称
     */
    @ApiModelProperty("节点名称")
    private String name;

    /**
     * 节点ip
     */
    @ApiModelProperty("节点ip")
    private String ip;

    /**
     * 节点端口
     */
    @ApiModelProperty("节点端口")
    private Integer port;

    /**
     * 登录用户
     */
    @ApiModelProperty("登录用户")
    private String sshUser;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    private String sshPwd;

    /**
     * 描述信息
     */
    @ApiModelProperty("描述信息")
    private String desc;
}
