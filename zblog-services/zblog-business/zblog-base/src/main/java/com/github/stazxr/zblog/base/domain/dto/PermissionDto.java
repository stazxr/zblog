package com.github.stazxr.zblog.base.domain.dto;

import com.github.stazxr.zblog.bas.validate.group.Create;
import com.github.stazxr.zblog.bas.validate.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * PermissionDto
 *
 * @author SunTao
 * @since 2022-06-29
 */
@Getter
@Setter
public class PermissionDto extends BaseDto {
    private static final long serialVersionUID = -7631261139679214658L;

    /**
     * 权限ID
     */
    @NotNull(groups = Update.class, message = "{perm.valid.id.NotNull}")
    @ApiModelProperty("权限id")
    private Long id;

    /**
     * 上级权限ID
     */
    @ApiModelProperty("权限pid")
    private Long pid;

    /**
     * 权限名称
     */
    @NotBlank(message = "{perm.valid.permName.NotBlank}")
    @ApiModelProperty("权限名称")
    private String permName;

    /**
     * 权限编码
     */
    @ApiModelProperty("权限编码")
    private String permCode;

    /**
     * 权限类型
     */
    @NotNull(message = "{perm.valid.permType.NotNull}")
    @ApiModelProperty("权限类型，1：目录、2：菜单、3：按钮")
    private Integer permType;

    /**
     * 权限访问级别
     */
    @NotNull(message = "{perm.valid.permLevel.NotNull}")
    @ApiModelProperty("权限访问级别")
    private Integer permLevel;

    /**
     * 组件名称
     */
    @ApiModelProperty("组件名称")
    private String componentName;

    /**
     * 组件路径
     */
    @ApiModelProperty("组件路径")
    private String componentPath;

    /**
     * 路由地址
     */
    @ApiModelProperty("前端路由地址")
    private String routerPath;

    /**
     * 权限图标
     */
    @ApiModelProperty("权限图标")
    private String icon;

    /**
     * 权限排序
     */
    @NotNull(message = "{perm.valid.sort.NotNull}")
    @ApiModelProperty(value = "权限排序", example = "99999")
    private Integer sort;

    /**
     * 是否缓存
     */
    @ApiModelProperty(value = "是否缓存")
    private Boolean cacheable;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    /**
     * 是否外链
     */
    @ApiModelProperty(name = "iFrame", value = "是否外链")
    private Boolean iFrame;

    /**
     * 是否启用
     */
    @NotNull(message = "{perm.valid.enabled.NotNull}")
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
}
