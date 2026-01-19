package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户密码更新记录
 *
 * @author SunTao
 * @since 2022-08-03
 */
@Getter
@Setter
@TableName("user_pass_log")
public class UserPassLog {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户编码
     */
    private Long userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码修改时间
     */
    private LocalDateTime changePasswordTime;
}
