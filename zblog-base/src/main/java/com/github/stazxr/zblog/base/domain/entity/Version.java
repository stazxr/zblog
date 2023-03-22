package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统版本
 *
 * @author SunTao
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("version")
@ApiModel("版本实体")
public class Version extends BaseEntity {
    /**
     * 版本id
     */
    @TableId
    @ApiModelProperty("版本id")
    private Long id;

    /**
     * 版本名称
     */
    @ApiModelProperty("版本名称")
    private String versionName;

    /**
     * 版本描述
     */
    @ApiModelProperty("版本描述")
    private String updateContent;

    /**
     * 是否删除
     */
    @TableLogic
    @ApiModelProperty("是否删除")
    private Boolean deleted;
}
