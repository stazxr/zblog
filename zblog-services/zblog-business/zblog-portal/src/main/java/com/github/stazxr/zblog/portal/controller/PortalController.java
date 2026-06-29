package com.github.stazxr.zblog.portal.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户管理
 *
 * @author SunTao
 * @since 2026-04-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portal")
@Api(value = "PortalController", tags = { "门户管理" })
public class PortalController {



//    /**
//     * 前台登录
//     *
//     * @param request    请求信息
//     * @param loginDto   登录信息
//     * @return User
//     */
//    @PostMapping(value = "/webLogin")
//    @ApiOperation(value = "前台登录")
//    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
//    @Router(name = "前台登录", code = "webLogin", level = RouterLevel.OPEN)
//    public Result webLogin(HttpServletRequest request, @RequestBody UserLoginDto loginDto) {
//        return portalService.webLogin(request, loginDto);
//    }
}
