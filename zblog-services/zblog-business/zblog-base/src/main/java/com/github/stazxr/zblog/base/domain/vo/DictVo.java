package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.base.domain.enums.DictType;
import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典信息
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
@ApiModel("字典VO")
public class DictVo extends BaseVo {
    private static final long serialVersionUID = 3006167677027752347L;

    /**
     * 字典ID
     */
    @ApiModelProperty("字典id")
    private Long id;

    /**
     * 上级字典ID
     */
    @ApiModelProperty("字典pid")
    private Long pid;

    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @ApiModelProperty("字典类型")
    private Integer dictType;

    /**
     * 字典key
     */
    @ApiModelProperty("字典key")
    private String dictKey;

    /**
     * 字典Value
     */
    @ApiModelProperty("字典value")
    private String dictValue;

    /**
     * 字典排序
     */
    @ApiModelProperty("字典排序")
    private Integer dictSort;

    /**
     * 字典描述
     */
    @ApiModelProperty("字典描述")
    private String dictDesc;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * 子节点数目
     */
    @ApiModelProperty("子节点数")
    private int subCount;

    /**
     * 是否包含子节点
     */
    @ApiModelProperty("是否包含子节点")
    private Boolean hasChildren;

    public Boolean getHasChildren() {
        if (DictType.ITEM.getValue().equals(this.dictType)) {
            return false;
        }

        return subCount > 0;
    }
}
