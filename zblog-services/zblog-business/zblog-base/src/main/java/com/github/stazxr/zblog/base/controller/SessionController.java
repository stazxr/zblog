package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.cache.CacheInfo;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.jwt.storage.TokenPayload;
import com.github.stazxr.zblog.base.domain.error.SessionErrorCode;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话管理
 *
 * @author SunTao
 * @since 2026-03-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
@Api(value = "SessionController", tags = { "会话管理" })
public class SessionController {
    private static final String LOGIN_USER_KEY = "TOKEN:";

    private final JwtTokenStorage jwtTokenStorage;

    /**
     * 查询在线用户列表
     *
     * @return List<CacheInfo>
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询在线用户列表")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询在线用户列表", code = "SESSQ001")
    public List<TokenPayload> scan() {
        List<TokenPayload> tokenPayloads = new ArrayList<>();
        List<CacheInfo> users = GlobalCache.scan(LOGIN_USER_KEY + "*", true);
        if (users.size() > 0) {
            for (CacheInfo user : users) {
                if (user.getValue() instanceof TokenPayload) {
                    tokenPayloads.add((TokenPayload) user.getValue());
                }
            }
        }
        return tokenPayloads;
    }

    /**
     * 踢出用户
     */
    @Log
    @PostMapping("/kickout")
    @ApiOperation(value = "踢出用户")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "踢出用户", code = "SESSD001")
    public void kickout(@RequestParam String userId) {
        Long loginId = SecurityUtils.getLoginId();
        ThrowUtils.throwIf(userId.equals(String.valueOf(loginId)), SessionErrorCode.ESESSA000);
        TokenPayload tokenPayload = jwtTokenStorage.get(userId);
        if (tokenPayload != null) {
            tokenPayload.setKickOut(true);
            jwtTokenStorage.update(userId, tokenPayload);
        }
    }
}
