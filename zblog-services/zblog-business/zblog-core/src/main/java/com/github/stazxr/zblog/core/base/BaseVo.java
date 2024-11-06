package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.core.util.ToStringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * BaseVo
 *
 * @author SunTao
 * @since 2022-10-06
 */
@Slf4j
@Getter
@Setter
public class BaseVo implements Serializable {
    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改用户
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private String updateTime;

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
