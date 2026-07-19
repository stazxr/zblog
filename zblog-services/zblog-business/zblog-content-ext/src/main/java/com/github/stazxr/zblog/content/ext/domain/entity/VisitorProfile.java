package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 访客展示信息
 *
 * @author SunTao
 * @since 2026-07-19
 */
@Getter
@Setter
@TableName("visitor_profile")
public class VisitorProfile {

    /**
     * 访客id
     */
    @TableId
    private String visitorId;

    /**
     * 访客昵称
     */
    private String nickname;

    /**
     * 访问头像
     */
    private String avatar;
}
