package com.github.stazxr.zblog.bas.captcha.factory;

import com.github.stazxr.zblog.bas.captcha.Captcha;
import com.github.stazxr.zblog.bas.captcha.CaptchaException;

/**
 * 生成验证码的工厂接口。
 *
 * @author SunTao
 * @since 2024-08-20
 */
public interface CaptchaFactory {
    /**
     * 根据指定的键生成验证码。
     *
     * @param key 用于生成验证码的唯一标识符
     * @return 生成的验证码对象
     * @throws CaptchaException 验证码生成失败
     */
    Captcha createCaptcha(String key) throws CaptchaException;
}

