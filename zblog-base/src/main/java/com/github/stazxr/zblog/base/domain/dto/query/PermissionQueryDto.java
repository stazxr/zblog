package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限查询
 *
 * @author SunTao
 * @since 2022-08-25
 */
@Getter
@Setter
@ToString
public class PermissionQueryDto extends PageParam {
    /**
     * 权限序列
     */
    private Long permId;

    /**
     * 角色序列
     */
    private Long roleId;

    /**
     * 模糊查询（标题、权限标识、组件名称、组件路径）
     */
    private String blurry;

    /**
     * 状态
     */
    private Boolean enabled;

    /**
     * 外链
     */
    private Boolean iFrame;

    /**
     * 是否只显示菜单
     */
    private Boolean onlyShowMenu;

    /**
     * 是否补充顶级菜单
     */
    private Boolean needTop;
}
