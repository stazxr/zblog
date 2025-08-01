package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.core.util.ToStringUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * BaseVo
 *
 * @author SunTao
 * @since 2022-10-06
 */
@Getter
@Setter
public class BaseVo implements Serializable {
    private static final long serialVersionUID = -1239845243956657291L;

    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 创建用户名称
     */
    private String createUsername;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改用户
     */
    private Long updateUser;

    /**
     * 修改用户名称
     */
    private String updateUsername;

    /**
     * 修改时间
     */
    private String updateTime;

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
