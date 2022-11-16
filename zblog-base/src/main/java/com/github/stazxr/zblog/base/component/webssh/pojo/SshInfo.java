package com.github.stazxr.zblog.base.component.webssh.pojo;

import lombok.Data;

/**
 * Ssh 信息
 *
 * @author SunTao
 * @since 2022-11-02
 */
@Data
public class SshInfo {
    private String operate;

    private String host;

    private Integer port = 22;

    private String username;

    private String password;

    private String command = "";
}
