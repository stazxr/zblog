package com.github.stazxr.zblog.base.security.handler;

import com.github.stazxr.zblog.base.cache.UserRoleCache;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.security.jwt.ZblogToken;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * CustomAuthenticationSuccessHandler
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
   private final JwtTokenGenerator jwtTokenGenerator;

    private final RoleService roleService;

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        // get user
        User principal = (User) authentication.getPrincipal();
        String username = principal.getUsername();

        // get roles
        List<Role> userRoles = roleService.queryRolesByUserId(principal.getId());
        UserRoleCache.put(username, userRoles);

        // generate token
        Map<String, Object> data = new HashMap<>(2);
        ZblogToken token = jwtTokenGenerator.getTokenResponse(principal);
        data.put("access_token", token.getAccessToken());
        data.put("refresh_token", token.getRefreshToken());
        data.put("additional", token.getAdditional());

        // return
        ResponseUtils.responseJsonWriter(response, Result.success("登录成功").data(data));
    }
}
