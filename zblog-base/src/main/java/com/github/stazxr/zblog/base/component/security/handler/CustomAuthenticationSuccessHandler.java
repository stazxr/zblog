package com.github.stazxr.zblog.base.component.security.handler;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.component.security.filter.CustomRememberMeFilter;
import com.github.stazxr.zblog.base.component.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.domain.entity.UserTokenStorage;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenGenerator jwtTokenGenerator;

    private final UserService userService;

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // get user
        User user = (User) authentication.getPrincipal();

        // generate token
        int tokenVersion = 1;
        String token = jwtTokenGenerator.generateToken(request, user, 1, null);
        Map<String, Object> data = new HashMap<>(2);
        data.put("access_token", Constants.AUTHENTICATION_PREFIX.concat(token));
        data.put("change_pwd", user.getChangePwd());

        // storage token
        Long userId = user.getId();
        UserTokenStorage tokenStorage = UserTokenStorage.builder().userId(userId).lastedToken(token).version(tokenVersion).build();
        userService.storageUserToken(tokenStorage, 1);

        // set login time
        userService.updateUserLoginInfo(request, userId);

        // remember me
        Boolean fromRememberMe = (Boolean) request.getAttribute(CustomRememberMeFilter.FROM_REMEMBER_ME);
        if (fromRememberMe != null && fromRememberMe) {
            log.info("user {} login with remember me", user.getUsername());
        }

        // return
        ResponseUtils.responseJsonWriter(response, Result.success("登录成功").data(data));
    }
}
