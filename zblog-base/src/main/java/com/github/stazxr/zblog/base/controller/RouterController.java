package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.RouterService;
import io.swagger.annotations.Api;
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
@Api(tags = "系统：路由管理")
@RequestMapping("/api/router")
public class RouterController {
    private final RouterService routerService;
}
