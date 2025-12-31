package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 权限信息
 *
 * @author SunTao
 * @since 2022-06-30
 */
@Getter
@Setter
@ApiModel("权限VO")
public class PermissionVo extends BaseVo {
    private static final long serialVersionUID = 1093135022712327296L;

    /**
     * 权限ID
     */
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
    @ApiModelProperty("权限类型，1：目录、2：菜单、3：按钮、4：外链")
    private Integer permType;

    /**
     * 权限访问级别
     */
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
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    /**
     * 是否包含子节点，懒加载用
     */
    @ApiModelProperty(value = "是否包含子节点，懒加载用")
    private Boolean hasChildren;

    /**
     * 子权限列表，非懒加载用
     */
    @ApiModelProperty(value = "子权限列表，非懒加载用")
    private List<PermissionVo> children;

    /**
     * 角色编码列表
     */
    @ApiModelProperty(value = "角色编码列表")
    private List<String> roleCodeList;
}
