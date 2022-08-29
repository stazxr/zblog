package com.github.stazxr.zblog.base.component.security.handler;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登出处理器
 *
 * @author SunTao
 * @since 2022-01-20
 */
@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.getPrincipal() != null) {
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            log.info("用户 {} 注销成功", username);
        }

        Result result = Result.success(ResultCode.LOGOUT_SUCCESS);
        ResponseUtils.responseJsonWriter(response, result);
    }
}
