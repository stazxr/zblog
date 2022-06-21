package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/perms")
public class PermissionController {
    private final PermissionService permissionService;

    /**
     * 构建菜单树
     *
     * @return menuTree
     */
    @Log
    @GetMapping(value = "/buildMenus")
    @Router(name = "构建菜单树", code = "buildMenus")
    public Result buildMenus() {
        return Result.success().data(permissionService.buildMenus(SecurityUtils.getLoginId()));
    }
}
