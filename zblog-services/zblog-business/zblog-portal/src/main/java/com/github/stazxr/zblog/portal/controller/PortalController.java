package com.github.stazxr.zblog.portal.controller;

import com.github.stazxr.zblog.bas.ratelimit.annotation.RateLimit;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.portal.domain.bo.WebLoginUser;
import com.github.stazxr.zblog.portal.domain.dto.BarrageMessageDto;
import com.github.stazxr.zblog.portal.service.PortalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
     * 获取Web端登录用户信息
     *
     * @return WebLoginUser
     */
    @GetMapping("/webLoginId")
    @ApiOperation(value = "获取Web端登录用户信息")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "获取Web端登录用户信息", code = "PORTQ001", level = RouterLevel.OPEN)
    public WebLoginUser currentWebUserDetail() {
        return portalService.currentWebUserDetail();
    }

    /**
     * 查询博客页面信息
     *
     * @return Map<String, List<ThemePageVo>>
     *     K: pageLabel
     *     V: List<ThemePageVo>
     */
    @GetMapping("/queryPageInfo")
    @ApiOperation(value = "查询博客页面信息")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "查询博客页面信息", code = "PORTQ002", level = RouterLevel.OPEN)
    public Map<String, List<ThemePageVo>> queryPageInfo() {
        return portalService.queryPageInfo();
    }

    /**
     * 查询最新弹幕列表
     *
     * @return List<BarrageMessageVo>
     */
    @GetMapping(value = "/queryBarrageMessageList")
    @ApiOperation(value = "查询最新弹幕列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "查询最新弹幕列表", code = "PORTQ003", level = RouterLevel.OPEN)
    public List<BarrageMessageVo> queryBarrageMessageListByPage() {
        return portalService.queryBarrageMessageList();
    }

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
    @RateLimit(time = 60, count = 5, enableIp = true, enableApi = true, message = "{BARRAGE_MESSAGE_LIMITED}")
    public void addBarrageMessage(HttpServletRequest request, @RequestBody @Validated BarrageMessageDto barrageMessageDto) {
        portalService.addBarrageMessage(request, barrageMessageDto);
    }

    /**
     * 点赞弹幕
     *
     * @param request 请求信息
     * @param barrageMessageId 弹幕id
     */
    @Log
    @PostMapping(value = "/likeBarrageMessage")
    @ApiOperation(value = "点赞弹幕")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "barrageMessageId", value = "弹幕id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "点赞弹幕", code = "PORTU001", level = RouterLevel.OPEN)
    public void likeBarrageMessage(HttpServletRequest request, @RequestParam Long barrageMessageId) {
        portalService.likeBarrageMessage(request, barrageMessageId);
    }

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     */
    @Log
    @PostMapping(value = "/recordVisitor")
    @ApiOperation(value = "记录访客信息")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "记录访客信息", code = "PORTA002", level = RouterLevel.OPEN)
    @RateLimit(time = 60, count = 1, enableIp = true, enableApi = true)
    public void recordVisitor(HttpServletRequest request) {
        portalService.recordVisitor(request);
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
