package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.core.util.ToStringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * BaseDto
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Slf4j
@Getter
@Setter
public class BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private String updateTime;

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
