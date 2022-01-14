package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

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
public class Permission extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 权限名称
     */
    private String permName;

    /**
     * 父权限ID, top perm is null
     */
    private Long pid;

    /**
     * 权限对应的路由信息
     * if {@link PermissionType#BTN} not null see: {@link Router#getId()}, else be null.
     */
    private Long routerId;

    /**
     * 权限路径
     * if {@link PermissionType#DIR} be empty string
     * if {@link PermissionType#MENU} be menu path
     * if {@link PermissionType#BTN} be router url, see {@link Router#getUrl()}
     */
    private String permPath;

    /**
     * 权限类型
     */
    private PermissionType permType;

    /**
     * 权限访问级别
     * {@link com.github.stazxr.zblog.core.annotation.Router#level()}
     */
    private Integer level;

    /**
     * 权限图标
     */
    private String icon;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 权限状态（启用/禁用）
     */
    private Boolean enabled;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;

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
