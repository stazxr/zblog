package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户最新令牌记录表
 *
 * @author SunTao
 * @since 2022-08-18
 */
@Slf4j
@Getter
@Setter
@Builder
@TableName("user_token_storage")
public class UserTokenStorage {
    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 用户当前的Token
     */
    private String lastedToken;

    /**
     * 令牌版本
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
}
