package com.github.stazxr.zblog.content.ext.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 敏感词信息
 *
 * @author SunTao
 * @since 2026-07-23
 */
@Getter
@Setter
@ApiModel("敏感词VO")
public class SensitiveWordVo implements Serializable {
    private static final long serialVersionUID = 694331411528529511L;

    /**
     * 敏感词id
     */
    @ApiModelProperty("敏感词id")
    private Long id;

    /**
     * 敏感词
     */
    @ApiModelProperty("敏感词")
    private String word;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 风险等级
     */
    @ApiModelProperty("风险等级")
    private Integer level;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建用户id
     */
    @ApiModelProperty("创建用户id")
    private Long createUser;

    /**
     * 创建用户
     */
    @ApiModelProperty("创建用户")
    private String createUsername;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改用户id
     */
    @ApiModelProperty("修改用户id")
    private Long updateUser;

    /**
     * 修改用户
     */
    @ApiModelProperty("修改用户")
    private Long updateUsername;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
