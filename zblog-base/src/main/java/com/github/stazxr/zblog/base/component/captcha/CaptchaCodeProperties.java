package com.github.stazxr.zblog.base.component.captcha;

import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.exception.BadConfigurationException;
import com.github.stazxr.zblog.util.StringUtils;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.Data;

import java.awt.*;
import java.util.Objects;

/**
 * 验证码可配置信息
 *
 * @author SunTao
 * @since 2022-05-20
 */
@Data
public class CaptchaCodeProperties {
    /**
     * 验证码类型
     */
    private String captchaType;

    /**
     * 验证码有效期秒
     */
    private int duration = Constants.CacheKey.loginCode.duration();

    /**
     * 验证码基本信息
     */
    private CaptchaCode captchaCode;

    /**
     * 根据配置信息获取验证码
     *
     * @return Captcha
     */
    public Captcha getCaptcha() {
        if (Objects.isNull(captchaCode)) {
            captchaCode = new CaptchaCode();
            if (StringUtils.isBlank(captchaType)) {
                captchaType = CaptchaCodeEnum.Arithmetic.name();
            }
        }
        return CaptchaCodeBuild.build(captchaType, captchaCode);
    }

    static class CaptchaCodeBuild {
        /**
         * 构造验证码对象
         *
         * @param captchaType 验证码类型
         * @param captchaCode 验证码基本信息
         * @return Captcha
         */
        public static Captcha build(String captchaType, CaptchaCode captchaCode) {
            Captcha captcha;
            CaptchaCodeEnum codeEnum = CaptchaCodeEnum.valueOf(captchaType);
            switch (codeEnum) {
                case Arithmetic:
                    captcha = new FixedArithmeticCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                    captcha.setLen(captchaCode.getLength());
                    break;
                case Chinese:
                    captcha = new ChineseCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                    captcha.setLen(captchaCode.getLength());
                    break;
                case Spec:
                    captcha = new SpecCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                    captcha.setLen(captchaCode.getLength());
                    break;
                case ChineseGif:
                    captcha = new ChineseGifCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                    captcha.setLen(captchaCode.getLength());
                    break;
                case Gif:
                    captcha = new GifCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                    captcha.setLen(captchaCode.getLength());
                    break;
                default:
                    throw new BadConfigurationException("验证码配置信息错误！正确配置查看 LoginCodeEnum ");
            }

            if (StringUtils.isNotBlank(captchaCode.getFontName())) {
                captcha.setFont(new Font(captchaCode.getFontName(), Font.PLAIN, captchaCode.getFontSize()));
            }
            return captcha;
        }
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }
}
