package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * DictVo
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
public class NodeVo extends BaseVo {
    /**
     * ID
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
    private String sshPwd = "[PROTECTED]";

    /**
     * 描述信息
     */
    private String desc;
}
