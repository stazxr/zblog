package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.RoleQueryDto;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    /**
     * 查询权限对应的角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @GetMapping(value = "/queryPermRole")
    @Router(name = "查询权限对应的角色列表", code = "queryPermRole", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermRole(RoleQueryDto queryDto) {
        return Result.success().data(roleService.queryPermRole(queryDto));
    }
}
