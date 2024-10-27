package com.github.stazxr.zblog.bas.captcha;

import com.github.stazxr.zblog.util.UuidUtils;

import java.util.Date;

/**
 * 表示一个验证码对象
 *
 * @author SunTao
 * @since 2024-08-21
 */
public class Captcha {
    private final String captchaId;
    private String text;
    private String base64;
    private final Date createTime;
    private int duration;

    /**
     * 创建一个新的 {@code Captcha} 实例。
     * <p>
     * 初始化时生成唯一的验证码 ID，并设置当前时间为创建时间。
     * </p>
     */
    public Captcha() {
        this.captchaId = UuidUtils.genUuidStr();
        this.createTime = new Date();
    }

    /**
     * 获取验证码的唯一标识符。
     *
     * @return 验证码的唯一标识符
     */
    public String getCaptchaId() {
        return captchaId;
    }

    /**
     * 获取验证码的内容。
     *
     * @return 验证码的内容
     */
    public String getText() {
        return text;
    }

    /**
     * 设置验证码的内容。
     *
     * @param text 验证码的内容
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 获取验证码的 Base64 编码字符串。
     *
     * @return 验证码的 Base64 编码字符串
     */
    public String getBase64() {
        return base64;
    }

    /**
     * 设置验证码的 Base64 编码字符串。
     *
     * @param base64 验证码的 Base64 编码字符串
     */
    public void setBase64(String base64) {
        this.base64 = base64;
    }

    /**
     * 获取验证码的创建时间。
     *
     * @return 验证码的创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置验证码的有效时间（秒）。
     *
     * @param duration 验证码的有效时间（秒）
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 获取验证码的有效时间（秒）。
     *
     * @return 验证码的有效时间（秒）
     */
    public int getDuration() {
        return duration;
    }
}

