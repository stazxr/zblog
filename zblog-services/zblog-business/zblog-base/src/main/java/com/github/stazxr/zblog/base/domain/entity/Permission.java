package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.stazxr.zblog.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 系统权限
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
@ApiModel("权限实体")
@TableName("permission")
public class Permission extends BaseEntity {
    private static final long serialVersionUID = 1439040306321618231L;

    /**
     * 权限ID
     */
    @TableId
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
     * 权限编码（应与路由中的编码保持一致，否则找不到权限可以访问的接口列表）
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#DIR},
     *  权限编码为 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#MENU},
     *  权限编码为路由编码或 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#BTN},
     *  权限编码则为路由编码
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#LINK},
     *  权限编码则为 NULL
     * </li>
     *
     * @see com.github.stazxr.zblog.bas.router.Router#code() 路由(接口)配置, 配置接口的访问规则
     * @see com.github.stazxr.zblog.bas.router.Resource#getResourceCode() 系统会将路由配置转为资源信息
     */
    @ApiModelProperty("权限编码")
    private String permCode;

    /**
     * 权限类型
     *
     * <p>
     * 配置规则:
     * <li>目录: 目录的上级只能是目录或 NULL, 目录的下级只能是目录或菜单或外链</li>
     * <li>菜单: 菜单的上级只能是目录, 下级只能是按钮</li>
     * <li>按钮: 按钮上级只能是菜单 或 NULL, 没有下级</li>
     * <li>外链: 外链上级只能是目录 或 NULL, 没有下级</li>
     * </p>
     *
     * @see com.github.stazxr.zblog.base.domain.enums.PermissionType
     */
    @ApiModelProperty("权限类型，1：目录、2：菜单、3：按钮、4：外链")
    private Integer permType;

    /**
     * 权限访问级别（可在服务运行后动态修改权限的访问级别）
     *
     * @see com.github.stazxr.zblog.bas.router.RouterLevel
     * @see com.github.stazxr.zblog.bas.router.RouterExtLevel
     * @see com.github.stazxr.zblog.bas.router.Router#level()
     * @see com.github.stazxr.zblog.bas.router.Resource#getResourceLevel()
     */
    @ApiModelProperty("权限访问级别: 1-公开权限;2-公共权限;4-受控权限;...")
    private Integer permLevel;

    /**
     * 组件名称
     *
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#DIR}:
     *  组件名称为 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#MENU}:
     *  组件名称为前端 Vue 组件的名称, 即 name 属性对应值，eg: User
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#BTN}:
     *  组件名称为 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#LINK}:
     *  组件名称为 NULL
     * </li>
     */
    @ApiModelProperty("组件名称")
    private String componentName;

    /**
     * 组件路径
     *
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#DIR}:
     *  组件路径为 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#MENU}:
     *  组件路径为前端 Vue 组件在 views 目录下的相对路径，eg: admin/system/user/index
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#BTN}:
     *  组件路径为 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#LINK}:
     *  组件路径为 NULL
     * </li>
     */
    @ApiModelProperty("组件路径")
    private String componentPath;

    /**
     * 路由地址 eg: xxx 或 xxx:${val} (非斜杠开头)
     *
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#DIR}
     *  浏览器访问地址中的路径
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#MENU}
     *  浏览器访问地址中的路径
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#BTN}
     *  前端路由地址为 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#LINK}
     *  前端路由地址为外链URL
     * </li>
     */
    @ApiModelProperty("前端路由地址")
    private String routerPath;

    /**
     * 权限图标
     *
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#DIR}:
     *  图标名称, 跟前端一致
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#MENU}:
     *  图标名称, 跟前端一致
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#BTN}:
     *  权限图标为 NULL
     * </li>
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#LINK}:
     *  图标名称, 跟前端一致
     * </li>
     */
    @ApiModelProperty("权限图标")
    private String icon;

    /**
     * 权限排序，默认99999
     */
    @ApiModelProperty(value = "权限排序", example = "99999")
    private Integer sort;

    /**
     * 是否缓存
     *
     * <li>前端缓存功能, 配置为 true, 重新访问打开的页面不会重新加载页面,
     *  只有菜单配置 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#MENU} 才会生效
     * </li>
     */
    @ApiModelProperty(value = "是否缓存")
    private Boolean cacheable;

    /**
     * 是否隐藏
     *
     * <li>如果是 {@link com.github.stazxr.zblog.base.domain.enums.PermissionType#BTN} 则为 NULL
     */
    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    /**
     * 是否删除
     */
    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;

    /**
     * 子集列表
     */
    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<Permission> children;

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Permission other = (Permission) obj;
        return id != null && id.equals(other.getId());
    }
}
