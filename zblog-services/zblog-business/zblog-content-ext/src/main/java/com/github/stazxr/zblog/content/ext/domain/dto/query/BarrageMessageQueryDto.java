package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 弹幕查询参数
 *
 * @author SunTao
 * @since 2027-07-06
 */
@Getter
@Setter
@ApiModel("弹幕查询参数")
public class BarrageMessageQueryDto extends PageParam {
    private static final long serialVersionUID = -5369954701902504527L;

    /**
     * 弹幕内容
     */
    @ApiModelProperty("弹幕内容")
    private String content;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * IP
     */
    @ApiModelProperty("IP")
    private String ip;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Integer auditStatus;
}
