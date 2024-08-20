package com.github.stazxr.zblog.captcha.handler;

import com.github.stazxr.zblog.captcha.Captcha;
import com.github.stazxr.zblog.captcha.CaptchaCache;
import com.github.stazxr.zblog.captcha.CaptchaException;
import com.github.stazxr.zblog.captcha.factory.CaptchaFactory;
/**
 * {@link CaptchaHandler} 接口的实现类。
 *
 * @author SunTao
 * @since 2024-08-21
 */
public class CaptchaHandlerImpl implements CaptchaHandler {
    private CaptchaFactory captchaFactory;

    public CaptchaHandlerImpl(CaptchaFactory captchaFactory) {
        this.captchaFactory = captchaFactory;
    }

    /**
     * 创建一个新的验证码。
     *
     * @param key 生成验证码所属内容的字符串标识（例如 JSON、TYPE 等）
     * @return 创建的 {@link Captcha} 对象
     * @throws CaptchaException 验证码生成失败
     */
    @Override
    public Captcha createCaptcha(String key) throws CaptchaException {
        return captchaFactory.createCaptcha(key);
    }

    /**
     * 获取指定验证码的内容。
     *
     * @param captchaId 验证码的唯一标识符
     * @return 验证码的内容，如果验证码不存在或已过期则返回 {@code null}
     */
    @Override
    public String getCaptchaText(String captchaId) {
        return CaptchaCache.get(captchaId);
    }

    /**
     * 验证验证码的正确性。
     *
     * @param captchaId 验证码的唯一标识符
     * @param text      用户输入的验证码文本
     * @return 如果验证码文本正确则返回 {@code true}，否则返回 {@code false}
     */
    @Override
    public boolean verifyCaptcha(String captchaId, String text) {
        String captchaText = getCaptchaText(captchaId);
        return captchaText != null && captchaText.equals(text);
    }

    /**
     * 设置验证码工厂
     *
     * @param captchaFactory 用于创建验证码的 {@link CaptchaFactory} 实例
     */
    public void setCaptchaFactory(CaptchaFactory captchaFactory) {
        this.captchaFactory = captchaFactory;
    }
}
