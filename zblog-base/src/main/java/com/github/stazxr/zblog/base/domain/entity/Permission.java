package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
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
@TableName("permission")
@ApiModel("权限实体")
public class Permission extends BaseEntity {
    /**
     * 权限id
     */
    @TableId
    @ApiModelProperty("权限id")
    private Long id;

    /**
     * top's pid is null
     */
    @ApiModelProperty("权限pid")
    private Long pid;

    /**
     * 目录名称/菜单名称/权限名称
     */
    @ApiModelProperty("目录名称/菜单名称/权限名称")
    private String permName;

    /**
     * 权限类型
     * {@link com.github.stazxr.zblog.base.domain.enums.PermissionType}
     * DIR TOP IS DIR OR NULL, SUB IS DIR OR MENU
     * MENU TOP IS DIR, SUB IS BTN
     * BTN TOP IS MENU, NO SUB
     * * DIR IS FRAME, FORBIDDEN SUB
     * * MENU IS FRAME, FORBIDDEN SUB
     * *** 这里做成类似于目录，文件，文本的关系 ***
     */
    @ApiModelProperty("权限类型，1：目录、2：菜单、3：按钮")
    private Integer permType;

    /**
     * 权限编码（应与路由中的编码保持一致，否则找不到权限可以访问的接口列表）
     * if {@link PermissionType#DIR}, permCode is null
     * if {@link PermissionType#MENU}, permCode is null（isFrame） or route code
     * if {@link PermissionType#BTN}, permCode is null（isFrame） or route code
     */
    @ApiModelProperty("权限编码")
    private String permCode;

    /**
     * 权限访问级别（可在服务运行后动态修改权限的访问级别）
     * {@link com.github.stazxr.zblog.core.annotation.Router#level()}
     */
    @ApiModelProperty("权限访问级别，1：公开权限、2：公共权限、3：受控权限")
    private Integer permLevel;

    /**
     * 组件名称
     * if {@link PermissionType#DIR} be null
     * if {@link PermissionType#MENU} be vue component name
     * if {@link PermissionType#BTN} be null
     */
    @ApiModelProperty("组件名称，只有菜单有组件名称")
    private String componentName;

    /**
     * 组件路径
     * if {@link PermissionType#DIR} be null
     * if {@link PermissionType#MENU} be vue component path
     * if {@link PermissionType#BTN} be null
     */
    @ApiModelProperty("组件路径，只有菜单有组件路径")
    private String componentPath;

    /**
     * 前端路由地址 xxx or xxx:${val} (非斜杠开头)
     * if {@link PermissionType#DIR} be parent path or http(s) url (if iframe)
     * if {@link PermissionType#MENU} be children path or http(s) url (if iframe)
     * if {@link PermissionType#BTN} be null
     */
    @ApiModelProperty("前端路由地址")
    private String routerPath;

    /**
     * 权限图标
     * if {@link PermissionType#BTN} be empty string
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
     * only for {@link PermissionType#MENU}
     */
    @TableField(value = "`CACHE`")
    @ApiModelProperty(value = "是否缓存")
    private Boolean cache;

    /**
     * 是否隐藏
     * if {@link PermissionType#BTN} be null
     */
    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    /**
     * 是否外链
     * if {@link PermissionType#BTN} be null
     */
    @ApiModelProperty(name = "iFrame", value = "是否外链")
    private Boolean iFrame;

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
        return Objects.hash(getPermName());
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
        return !StringUtils.isEmpty(permName) && permName.equalsIgnoreCase(other.getPermName());
    }
}
