package com.github.stazxr.zblog.portal.controller;

import com.github.stazxr.zblog.bas.ratelimit.annotation.RateLimit;
import com.github.stazxr.zblog.bas.ratelimit.core.RateLimitFallback;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.portal.domain.bo.WebInitInfo;
import com.github.stazxr.zblog.portal.domain.bo.WebLoginUser;
import com.github.stazxr.zblog.portal.domain.dto.ApplyFriendLinkDto;
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
     * 获取网站初始化信息
     *
     * @return WebInitInfo
     */
    @GetMapping("/init")
    @ApiOperation(value = "获取网站初始化信息")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "获取网站初始化信息", code = "PORTQ000", level = RouterLevel.OPEN)
    public WebInitInfo init() {
        return portalService.init();
    }

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
     * 记录访客信息
     *
     * @param request 请求信息
     */
    @PostMapping(value = "/recordVisitor")
    @ApiOperation(value = "记录访客信息")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "记录访客信息", code = "PORTA001", level = RouterLevel.OPEN)
    @RateLimit(count = 1, time = 3600, enableIp = true, fallback = RateLimitFallback.SUCCESS)
    public void recordVisitor(HttpServletRequest request) {
        portalService.recordVisitor(request);
    }

    /**
     * 记录访客日志
     *
     * @param request 请求信息
     */
    @PostMapping(value = "/recordVisitorLog")
    @ApiOperation(value = "记录访客日志")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "记录访客日志", code = "PORTA002", level = RouterLevel.OPEN)
    public void recordVisitorLog(HttpServletRequest request) {
        portalService.recordVisitorLog(request);
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
    public List<BarrageMessageVo> queryBarrageMessageList() {
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
    @Router(name = "新增弹幕", code = "PORTA003", level = RouterLevel.OPEN)
    @RateLimit(count = 5, time = 60, enableIp = true, message = "{BARRAGE_MESSAGE_LIMITED}")
    public void addBarrageMessage(HttpServletRequest request, @RequestBody @Validated BarrageMessageDto barrageMessageDto) {
        portalService.addBarrageMessage(request, barrageMessageDto);
    }

    /**
     * 点赞弹幕
     *
     * @param request 请求信息
     * @param barrageMessageId 弹幕id
     * @return boolean true:点赞成功 false:已点赞
     */
    @Log
    @PostMapping(value = "/likeBarrageMessage")
    @ApiOperation(value = "点赞弹幕")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "barrageMessageId", value = "弹幕id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "点赞弹幕", code = "PORTU001", level = RouterLevel.OPEN)
    public boolean likeBarrageMessage(HttpServletRequest request, @RequestParam Long barrageMessageId) {
        return portalService.likeBarrageMessage(request, barrageMessageId);
    }

    /**
     * 查询前台友链列表
     *
     * @return Map<String, List<FriendLinkVo>>
     */
    @GetMapping(value = "/queryFriendLinkList")
    @ApiOperation(value = "查询前台友链列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "查询前台友链列表", code = "PORTQ004", level = RouterLevel.OPEN)
    public Map<String, List<FriendLinkVo>> queryFriendLinkList() {
        return portalService.queryFriendLinkList();
    }

    /**
     * 友链申请
     *
     * @param friendLinkDto 友链信息
     */
    @Log
    @PostMapping(value = "/applyFriendLink")
    @ApiOperation(value = "友链申请")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "友链申请", code = "PORTA004", level = RouterLevel.OPEN)
    @RateLimit(count = 1, time = 60, enableIp = true)
    public void applyFriendLink(@RequestBody @Validated ApplyFriendLinkDto friendLinkDto) {
        portalService.applyFriendLink(friendLinkDto);
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
