package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章操作日志信息
 *
 * @author SunTao
 * @since 2026-10-27
 */
@Getter
@Setter
@ApiModel(value = "ArticleOperationLogVo", description = "文章操作日志信息")
public class ArticleOperationLogVo implements Serializable {
    private static final long serialVersionUID = 5733293993612548755L;

    /**
     * 操作日志id
     */
    @ApiModelProperty(value = "操作日志id")
    private Long id;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Long articleId;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private Long operatorId;

    /**
     * 操作人昵称
     */
    @ApiModelProperty(value = "操作人昵称")
    private Long operatorUserNickname;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private Integer operationType;

    /**
     * 操作前文章类型
     */
    @ApiModelProperty(value = "操作前文章类型")
    private Integer beforeType;

    /**
     * 操作后文章类型
     */
    @ApiModelProperty(value = "操作后文章类型")
    private Integer afterType;

    /**
     * 操作前文章权限
     */
    @ApiModelProperty(value = "操作前文章权限")
    private Integer beforePerm;

    /**
     * 操作后文章权限
     */
    @ApiModelProperty(value = "操作后文章权限")
    private Integer afterPerm;

    /**
     * 操作前文章状态
     */
    @ApiModelProperty(value = "操作前文章状态")
    private Integer beforeStatus;

    /**
     * 操作后文章状态
     */
    @ApiModelProperty(value = "操作后文章状态")
    private Integer afterStatus;

    /**
     * 关联的正式版本号
     */
    @ApiModelProperty(value = "发布版本")
    private Integer version;

    /**
     * 操作说明
     */
    @ApiModelProperty(value = "操作说明")
    private String remark;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
}
