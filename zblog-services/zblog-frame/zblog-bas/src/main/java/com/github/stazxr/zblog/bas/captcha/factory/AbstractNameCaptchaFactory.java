package com.github.stazxr.zblog.bas.captcha.factory;

import com.github.stazxr.zblog.bas.captcha.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 基于名称的验证码工厂实现。
 * <p>
 * 该类根据提供的名称从缓存的验证码映射中创建验证码。
 * </p>
 *
 * @author SunTao
 * @since 2024-08-20
 */
@Slf4j
public abstract class AbstractNameCaptchaFactory implements CaptchaFactory {
    private final Map<String, CaptchaConfig> captchaConfigMap;

    /**
     * 构造一个 NameCaptchaFactory 实例。
     *
     * @param captchaConfigMap 存储验证码的映射，键为验证码名称，值为验证码配置对象
     */
    public AbstractNameCaptchaFactory(Map<String, CaptchaConfig> captchaConfigMap) {
        this.captchaConfigMap = captchaConfigMap;
    }

    /**
     * 根据验证码名称创建验证码。
     *
     * @param captchaName 验证码名称
     * @return 对应的验证码对象，如果不存在则返回 null
     * @throws CaptchaException 验证码生成失败
     */
    @Override
    public Captcha createCaptcha(String captchaName) throws CaptchaException {
        if (captchaConfigMap.containsKey(captchaName)) {
            CaptchaConfig captchaConfig = captchaConfigMap.get(captchaName);

            try {
                // build Captcha
                return buildCaptcha(captchaConfig);
            } catch (Exception e) {
                throw new CaptchaException(CaptchaErrorCode.SCAPTA000, e);
            }
        }

        log.warn("Captcha {} not exists in the NameCaptchaFactory", captchaName);
        return null;
    }

    /**
     * 构建并返回一个验证码对象。
     *
     * @param captchaConfig 验证码配置信息，包含生成验证码所需的各种配置参数，例如验证码的类型、长度、类型、复杂度等。
     * @return 返回一个 {@link Captcha} 验证码对象，该对象包含生成的验证码信息。
     */
    public static Captcha buildCaptcha(CaptchaConfig captchaConfig) {
        checkCaptchaConfig(captchaConfig);
        com.wf.captcha.base.Captcha baseCaptcha = CaptchaBuilder.buildCaptcha(captchaConfig);
        Captcha captcha = new Captcha();
        captcha.setText(baseCaptcha.text());
        captcha.setBase64(baseCaptcha.toBase64());
        captcha.setDuration(captchaConfig.getDuration());
        return captcha;
    }

    private static void checkCaptchaConfig(CaptchaConfig captchaConfig) {
        String captchaType = captchaConfig.getCaptchaType();
        if (captchaType == null) {
            captchaConfig.setCaptchaType(CaptchaType.Default.name());
        }
        if (captchaConfig.getDuration() < 0) {
            captchaConfig.setDuration(0); // 兜底
        }
    }
}

