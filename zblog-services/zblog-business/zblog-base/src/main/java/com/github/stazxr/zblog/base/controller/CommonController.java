package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.notify.mail.MailReceiver;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.domain.enums.CacheKey;
import com.github.stazxr.zblog.base.domain.enums.MailTemplate;
import com.github.stazxr.zblog.base.util.MailTemplateSender;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.math.RandomUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 公共管理
 *
 * @author SunTao
 * @since 2025-12-27
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
@Api(value = "CommonController", tags = { "公共管理" })
public class CommonController {
    private final MailTemplateSender mailTemplateSender;

    @Value("${zblog.security.PublicKey}")
    private String systemPublicKey;

    /**
     * 获取系统公钥
     *
     * @return 系统公钥
     */
    @GetMapping("/querySystemPublicKey")
    @ApiOperation(value = "获取系统公钥")
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "获取系统公钥", code = "COMMQ001", level = RouterLevel.PUBLIC)
    public String querySystemPublicKey() {
        return systemPublicKey;
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 对方邮箱
     * @return 验证码缓存key
     */
    @PostMapping("sendEmailCode")
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "发送邮箱验证码", code = "COMMQ002", level = RouterLevel.OPEN)
    public String sendCode(@RequestParam String email) {
        Assert.notBlank(email, "邮箱不能为空");
        Assert.failIfFalse(RegexUtils.match(email, RegexUtils.Regex.EMAIL_REGEX), "邮箱格式不正确");

        // 获取邮件内容
        MailTemplate emailCode = MailTemplate.EMAIL_CODE;
        MailReceiver receiver = MailReceiver.setReceive(email);
        Map<String, Object> variables = new LinkedHashMap<>();
        String random = RandomUtils.sixNumberCode();
        variables.put("code", random);
        variables.put("time", 5);
        mailTemplateSender.send(receiver, emailCode, variables);

        // 缓存验证码
        CacheKey emailCodeCache = CacheKey.EMAIL_CODE;
        String emailCodeCacheKey = String.format(emailCodeCache.getKey(), SequenceUtils.getId(), Locale.ROOT);
        GlobalCache.put(emailCodeCacheKey, random, emailCodeCache.getTtl());
        return emailCodeCacheKey;
    }
}
