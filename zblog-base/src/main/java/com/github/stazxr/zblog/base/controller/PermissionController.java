package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.PermissionService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
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
@Api(tags = "系统：权限管理")
@RequestMapping("/api/perms")
public class PermissionController {
    private final PermissionService permissionService;
}
