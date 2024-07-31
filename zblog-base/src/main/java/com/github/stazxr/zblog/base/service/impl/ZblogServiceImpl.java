package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.base.component.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.domain.bo.LoginUser;
import com.github.stazxr.zblog.base.mapper.ZblogMapper;
import com.github.stazxr.zblog.base.service.ZblogService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 部分公共接口实现
 *
 * @author SunTao
 * @since 2022-07-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ZblogServiceImpl implements ZblogService {
    private final ZblogMapper zblogMapper;

    private final JwtDecoder jwtDecoder;

    private final JwtTokenStorage jwtTokenStorage;

    /**
     * 清除记住我信息
     *
     * @param username 用户名
     */
    @Override
    public void removeRememberMe(String username) {
        Assert.notNull(username, "用户名不能为空");
        zblogMapper.removeRememberMe(username);
    }

    /**
     * 检查用户的登录状态
     *
     * @param request 请求信息
     * @return userId
     */
    @Override
    public LoginUser checkUserLoginStatus(HttpServletRequest request) {
        try {
            // 查找当前 IP 对应的登录令牌
            String ip = IpUtils.getIp(request);
            String ssoToken = CacheUtils.get(Constants.CacheKey.ssoTkn.cacheKey().concat(":").concat(ip));

            if (StringUtils.isNotBlank(ssoToken)) {
                // 解析该 token 拿到用户信息
                JWTClaimsSet claimsSet = jwtDecoder.decode(ssoToken);
                List<String> audiences = claimsSet.getAudience();
                if (audiences != null && !audiences.isEmpty()) {
                    // 根据 userId 获取当前用户的登录信息
                    Long userId = Long.parseLong(audiences.get(0));
                    String accessToken = jwtTokenStorage.getAccessToken(userId);
                    if (StringUtils.isNotBlank(accessToken)) {
                        // 对比 ssoToken 与 accessToken 是否一致
                        if (ssoToken.equals(accessToken)) {
                            LoginUser loginUser = new LoginUser();
                            loginUser.setUserId(userId);
                            loginUser.setAccessToken(Constants.AUTHENTICATION_PREFIX.concat(accessToken));
                            return loginUser;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("checkUserLoginStatus catch eor", e);
        }

        return null;
    }
}
