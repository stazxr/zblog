package com.github.stazxr.zblog.bas.captcha;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 验证码配置信息
 *
 * @author SunTao
 * @since 2022-08-20
 */
@Getter
@Setter
public class CaptchaConfig {
    /**
     * 验证码类型，默认数字字母组合，see {@link CaptchaType}
     */
    private String captchaType = CaptchaType.Default.name();

    /**
     * 验证码有效期，单位秒，默认不过期，0 表示不过期
     */
    private int duration;

    /**
     * 验证码内容长度，默认四个单位长度
     */
    private int length = 4;

    /**
     * 验证码宽度，默认 130
     */
    private int width = 130;

    /**
     * 验证码高度，默认 40
     */
    private int height = 40;

    /**
     * 验证码字体
     */
    private String fontName;

    /**
     * 字体大小，默认 28
     */
    private int fontSize = 28;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
