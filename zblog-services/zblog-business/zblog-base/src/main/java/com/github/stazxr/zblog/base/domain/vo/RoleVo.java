package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * RoleVo
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Getter
@Setter
public class RoleVo extends BaseVo {
    private static final long serialVersionUID = -5262794053587745904L;

    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 角色状态
     */
    private Boolean enabled;
}
