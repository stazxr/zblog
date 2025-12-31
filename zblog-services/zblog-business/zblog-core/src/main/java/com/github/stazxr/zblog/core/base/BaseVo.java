package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.core.util.ToStringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础响应实体
 *
 * @author SunTao
 * @since 2022-10-06
 */
@Getter
@Setter
@ApiModel("基础VO")
public class BaseVo implements Serializable {
    private static final long serialVersionUID = -1239845243956657291L;

    /**
     * 创建用户
     */
    @ApiModelProperty("创建用户")
    private Long createUser;

    /**
     * 创建用户名称
     */
    @ApiModelProperty("创建用户名称")
    private String createUsername;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 修改用户
     */
    @ApiModelProperty("修改用户")
    private Long updateUser;

    /**
     * 修改用户名称
     */
    @ApiModelProperty("修改用户名称")
    private String updateUsername;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private String updateTime;

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
