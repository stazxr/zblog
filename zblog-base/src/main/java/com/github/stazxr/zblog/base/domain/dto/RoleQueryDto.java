package com.github.stazxr.zblog.base.domain.dto;

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
}
