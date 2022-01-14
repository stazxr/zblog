package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.UserService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author SunTao
 * @since 2020-12-10
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：用户管理")
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
}
