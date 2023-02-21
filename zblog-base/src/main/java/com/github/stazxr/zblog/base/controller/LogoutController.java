package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.core.annotation.IgnoreResult;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.log.annotation.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.stazxr.zblog.core.base.BaseConst.PermLevel.PUBLIC;

/**
 * 登出管理
 *
 * @author SunTao
 * @since 2022-07-24
 */
@RestController
public class LogoutController {
    /**
     * 系统登出，这里只做路由注册，逻辑在处理器中
     */
    @Log
    @IgnoreResult
    @PostMapping("/api/logout")
    @Router(name = "系统注销", code = "logout", level = PUBLIC)
    public void logout() {
    }
}
