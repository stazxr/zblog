package com.github.stazxr.zblog.captcha.handler;

import com.github.stazxr.zblog.captcha.Captcha;
import com.github.stazxr.zblog.captcha.CaptchaException;

/**
 * 用于处理验证码的接口。
 * <p>
 * 该接口定义了创建验证码、获取验证码内容以及验证验证码的方法。
 * </p>
 *
 * @author SunTao
 * @since 2024-08-21
 */
public interface CaptchaHandler {
    /**
     * 创建一个新的验证码。
     *
     * @param key 生成验证码所属内容的字符串标识（JSON、TYPE等）
     * @return 创建的验证码对象
     * @throws CaptchaException 验证码生成失败
     */
    Captcha createCaptcha(String key) throws CaptchaException;

    /**
     * 获取指定验证码的内容。
     *
     * @param captchaId 验证码的唯一标识符
     * @return 验证码的内容
     */
    String getCaptchaText(String captchaId);

    /**
     * 验证验证码的正确性。
     *
     * @param captchaId 验证码的唯一标识符
     * @param text 用户输入的验证码文本
     * @return 如果验证码文本正确则返回 {@code true}，否则返回 {@code false}
     */
    boolean verifyCaptcha(String captchaId, String text);
}

