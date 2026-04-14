package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.cache.CacheInfo;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.security.jwt.storage.TokenPayload;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private static final String LOGIN_USER_KEY = "LOGIN:TOKEN:";

    /**
     * 查询在线用户列表
     *
     * @return List<CacheInfo>
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询在线用户列表")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询在线用户列表", code = "SESSQ001")
    public List<TokenPayload> scan(@RequestParam(required = false) String userId) {
        userId = StringUtils.isBlank(userId) ? "*" : userId;
        List<TokenPayload> tokenPayloads = new ArrayList<>();
        List<CacheInfo> scan = GlobalCache.scan(LOGIN_USER_KEY + userId);
        if (scan.size() > 0) {
            for (CacheInfo cacheInfo : scan) {
                tokenPayloads.add((TokenPayload) cacheInfo.getValue());
            }
        }
        return tokenPayloads;
    }
}
