package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

/**
 * 节点新增更新字段信息
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Data
public class NodeDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 节点 IP
     */
    private String ip;

    /**
     * SSH 端口
     */
    private Integer port;

    /**
     * 登录用户
     */
    private String sshUser;

    /**
     * 登录密码
     */
    private String sshPwd;

    /**
     * 描述信息
     */
    private String desc;
}
