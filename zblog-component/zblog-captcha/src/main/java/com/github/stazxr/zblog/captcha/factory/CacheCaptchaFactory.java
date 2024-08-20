package com.github.stazxr.zblog.captcha.factory;

import com.github.stazxr.zblog.captcha.Captcha;
import com.github.stazxr.zblog.captcha.CaptchaCache;
import com.github.stazxr.zblog.captcha.CaptchaConfig;
import com.github.stazxr.zblog.captcha.CaptchaException;

import java.util.Map;

/**
 * 基于缓存的验证码工厂类，继承自 {@link AbstractNameCaptchaFactory}。
 * <p>
 * 该类增强了创建验证码的接口，可以将创建的验证码存储到缓存中。
 * </p>
 *
 * @author SunTao
 * @since 2024-08-21
 */
public class CacheCaptchaFactory extends AbstractNameCaptchaFactory {
    /**
     * 构造一个 {@code CacheCaptchaFactory} 实例。
     *
     * @param captchaConfigMap 存储验证码配置的映射，键为验证码名称，值为 {@link CaptchaConfig} 对象
     */
    public CacheCaptchaFactory(Map<String, CaptchaConfig> captchaConfigMap) {
        super(captchaConfigMap);
    }

    /**
     * 根据验证码名称创建一个新的验证码，并将其存储到缓存中。
     *
     * @param captchaName 验证码名称
     * @return 创建的 {@link Captcha} 对象，如果验证码配置不存在则返回 {@code null}
     * @throws CaptchaException 验证码生成失败
     */
    @Override
    public Captcha createCaptcha(String captchaName) throws CaptchaException {
        Captcha captcha = super.createCaptcha(captchaName);
        if (captcha != null) {
            CaptchaCache.put(captcha.getCaptchaId(), captcha.getText(), captcha.getDuration());
        }
        return captcha;
    }
}
