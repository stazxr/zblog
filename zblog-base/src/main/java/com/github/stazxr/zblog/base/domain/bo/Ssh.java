package com.github.stazxr.zblog.base.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * SSH
 *
 * @author SunTao
 * @since 2022-11-10
 */
@Data
@AllArgsConstructor
public class Ssh implements Serializable {
    /**
     * IP
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 登录用户
     */
    private String user;

    /**
     * 登录密码
     */
    private String password;
}
