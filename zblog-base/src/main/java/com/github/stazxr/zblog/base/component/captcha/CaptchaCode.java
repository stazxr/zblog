package com.github.stazxr.zblog.base.component.captcha;

import lombok.Data;

/**
 * 验证码基本信息
 *
 * @author SunTao
 * @since 2022-05-20
 */
@Data
public class CaptchaCode {
    /**
     * 验证码内容长度，默认两个单位长度
     */
    private int length = 2;

    /**
     * 验证码宽度
     */
    private int width = 130;

    /**
     * 验证码高度
     */
    private int height = 40;

    /**
     * 验证码字体
     */
    private String fontName;

    /**
     * 字体大小
     */
    private int fontSize = 25;
}
