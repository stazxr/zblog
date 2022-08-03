package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户密码更新日志
 *
 * @author SunTao
 * @since 2022-08-03
 */
@Slf4j
@Getter
@Setter
@Builder
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
     * 修改时间
     */
    private String updateTime;
}
