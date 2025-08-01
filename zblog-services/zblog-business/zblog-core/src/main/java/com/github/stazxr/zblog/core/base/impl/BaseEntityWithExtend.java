package com.github.stazxr.zblog.core.base.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.github.stazxr.zblog.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 带扩展字段的基类实体。
 * 该类可以用于需要额外字段的场景，提供了扩展字段的灵活管理。
 *
 * @author SunTao
 * @since 2022-06-23
 */
@Getter
@Setter
@ApiModel("带扩展字段的基类实体")
public class BaseEntityWithExtend extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 扩展字段，存储 JSON 字符串
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty("扩展字段")
    private String extensionsJson;
}
