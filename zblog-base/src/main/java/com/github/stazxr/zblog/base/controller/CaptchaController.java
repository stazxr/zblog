package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * 登录验证码管理
 *
 *   TIPS:
 *      于 2022-05-20 废弃，替换方案：/api/auth/loginCode
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Slf4j
@Controller
@AllArgsConstructor
public class CaptchaController {
    private final DefaultKaptcha defaultKaptcha;

    /**
     * 登录验证码
     *
     * @param cacheKey 缓存标识
     * @param response 响应信息
     */
    @Deprecated
    @GetMapping(value = "/numCode")
    @Router(name = "登录验证码", code = "numCode", level = BaseConst.PermLevel.OPEN)
    public void numCodeForLogin(@RequestParam String cacheKey, HttpServletResponse response) {
        // cache numCode
        String correctNumCode = defaultKaptcha.createText();
        Constants.CacheKey loginNumCode = Constants.CacheKey.loginNumCode;
        CacheUtils.put(cacheKey, correctNumCode, loginNumCode.duration());

        // response image
        BufferedImage image = defaultKaptcha.createImage(correctNumCode);
        response.setContentType("image/png");
        try (OutputStream os = response.getOutputStream()) {
            ImageIO.write(image, "png", os);
        } catch (Exception e) {
            log.error("generate number error", e);
            CacheUtils.remove(cacheKey);
        }
    }
}
