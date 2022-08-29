package com.github.stazxr.zblog.base.domain.dto;

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
     * 权限ID
     */
    private Long permId;

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
}
