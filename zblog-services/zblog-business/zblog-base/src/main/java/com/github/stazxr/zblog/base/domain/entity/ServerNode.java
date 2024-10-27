package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务器节点
 *
 * @author SunTao
 * @since 2022-11-02
 */
@Getter
@Setter
@TableName("server_node")
public class ServerNode extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 节点名称
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
    @TableField(value = "`DESC`")
    private String desc;
}
