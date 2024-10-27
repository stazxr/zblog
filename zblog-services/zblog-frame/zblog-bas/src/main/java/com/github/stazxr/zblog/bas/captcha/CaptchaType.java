package com.github.stazxr.zblog.bas.captcha;

/**
 * 验证码类型
 *
 * @author SunTao
 * @since 2022-05-20
 */
public enum CaptchaType {
    /**
     * 英文与数字验证码
     */
    Default,

    /**
     * 中文验证码
     */
    Chinese,

    /**
     * 算术验证码
     */
    Arithmetic,

    /**
     * 英文与数字动态验证码
     */
    Gif,

    /**
     * 中文动态验证码
     */
    ChineseGif;

    public static CaptchaType of(String type) {
        try {
            return CaptchaType.valueOf(type);
        } catch (Exception e) {
            throw new CaptchaException("No support captcha type: " + type);
        }
    }
}
