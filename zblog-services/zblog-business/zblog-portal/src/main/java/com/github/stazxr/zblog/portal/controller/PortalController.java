package com.github.stazxr.zblog.portal.controller;

import com.github.stazxr.zblog.audit.AuditContext;
import com.github.stazxr.zblog.audit.AuditResult;
import com.github.stazxr.zblog.audit.model.AuditScene;
import com.github.stazxr.zblog.audit.service.AuditService;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.portal.domain.dto.MessageDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    private final AuditService auditService;

    /**
     * 弹幕留言
     *
     * @param request    请求信息
     * @param messageDto 留言信息
     */
    @PostMapping(value = "/saveMessage")
    @ApiOperation(value = "弹幕留言")
    @ApiVersion(value = BaseConst.ApiVersion.V_P_1_0_0)
    @Router(name = "弹幕留言", code = "saveMessage", level = RouterLevel.OPEN)
    public void recordVisitor(HttpServletRequest request, @RequestBody MessageDto messageDto) {
        AuditContext context = new AuditContext();
        context.setUserId(1L);
        context.setContent(messageDto.getMessageContent());
        context.setScene(AuditScene.COMMENT);
        AuditResult audit = auditService.audit(context);
        System.out.println("请求结果: " + audit);
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
