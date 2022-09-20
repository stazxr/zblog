package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色查询
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Getter
@Setter
@ToString
public class RoleQueryDto extends PageParam {
    /**
     * 权限ID
     */
    private Long permId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色状态
     */
    private Boolean enabled;
}
