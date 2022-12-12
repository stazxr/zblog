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
     * QQ 号
     */
    private String qqNum = "";

    /**
     * QQ 链接
     */
    private String qq = "";

    /**
     * Github 链接
     */
    private String github = "";

    /**
     * Gitee 链接
     */
    private String gitee = "";
}
