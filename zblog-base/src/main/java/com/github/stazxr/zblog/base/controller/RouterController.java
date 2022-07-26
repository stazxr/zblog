package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.RouterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统路由管理
 *
 * @author SunTao
 * @since 2020-12-01
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/router")
public class RouterController {
    private final RouterService routerService;
}
