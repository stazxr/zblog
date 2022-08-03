package com.github.stazxr.zblog.base.component.captcha;

/**
 * 验证码配置枚举
 *
 * @author SunTao
 * @since 2022-05-20
 */
public enum CaptchaCodeEnum {
    /**
     * 算术验证码
     */
    Arithmetic("ArithmeticCaptcha"),

    /**
     * 中文验证码
     */
    Chinese("ChineseCaptcha"),

    /**
     * 英文与数字验证码
     */
    Spec("SpecCaptcha"),

    /**
     * 中文动态验证码
     */
    ChineseGif("ChineseGifCaptcha"),

    /**
     * 英文与数字动态验证码
     */
    Gif("GifCaptcha");

    private final String type;

    CaptchaCodeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
