package com.github.stazxr.zblog.domain.dto.setting;

import lombok.Data;

/**
 * 社交信息
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Data
public class SocialInfo {
    /**
     * QQ
     */
    private String qq = "";

    /**
     * 微信
     */
    private String weChat = "";

    /**
     * Github
     */
    private String github = "";

    /**
     * Gitee
     */
    private String gitee = "";

    /**
     * CSDN
     */
    private String csdn = "";
}
