package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 自动审核记录查询参数
 *
 * @author SunTao
 * @since 2027-07-23
 */
@Getter
@Setter
@ApiModel("自动审核记录查询参数")
public class AuditRecordQueryDto extends PageParam {
    private static final long serialVersionUID = 48901576723951674L;

    /**
     * 审核场景
     */
    @ApiModelProperty("审核场景")
    private String scene;

    /**
     * 审核决策结果
     */
    @ApiModelProperty("审核决策结果")
    private String decision;
}
