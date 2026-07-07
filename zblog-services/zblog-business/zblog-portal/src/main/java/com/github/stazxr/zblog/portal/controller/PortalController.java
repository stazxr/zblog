package com.github.stazxr.zblog.portal.controller;

import com.github.stazxr.zblog.bas.ratelimit.annotation.RateLimit;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.portal.domain.dto.BarrageMessageDto;
import com.github.stazxr.zblog.portal.service.PortalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户管理
 *
 * @author SunTao
 * @since 2027-07-07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portal")
@Api(value = "PortalController", tags = { "门户管理" })
public class PortalController {
    private final PortalService portalService;

    /**
     * 新增弹幕
     *
     * @param request 请求信息
     * @param barrageMessageDto 弹幕信息
     */
    @Log
    @PostMapping(value = "/addBarrageMessage")
    @ApiOperation(value = "新增弹幕")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "新增弹幕", code = "PORTA001", level = RouterLevel.OPEN)
    @RateLimit(time = 60, count = 5, enableIp = true, enableApi = true, message = "发送太快啦，请休息一下再发~")
    public void addBarrageMessage(HttpServletRequest request, @RequestBody @Validated BarrageMessageDto barrageMessageDto) {
        portalService.addBarrageMessage(request, barrageMessageDto);
    }

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
