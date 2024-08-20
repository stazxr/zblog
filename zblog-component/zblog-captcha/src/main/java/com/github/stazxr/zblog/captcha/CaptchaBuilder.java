package com.github.stazxr.zblog.captcha;

import com.github.stazxr.zblog.util.StringUtils;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;

import java.awt.*;
import java.util.Arrays;

/**
 * 生成验证码的构建器
 *
 * @author SunTao
 * @since 2024-08-18
 */
public class CaptchaBuilder {
    /**
     * 构建并返回一个验证码对象。
     * <p>
     * 实现此方法的类需要提供具体的验证码生成逻辑，并返回一个包含生成的验证码的 {@link Captcha} 对象。
     * </p>
     *
     * @return 返回一个 {@link Captcha} 验证码对象，该对象包含生成的验证码信息。
     */
    public static Captcha buildCaptcha(CaptchaConfig config) {
        return Builder.build(config);
    }

    private static class Builder {
        public static com.wf.captcha.base.Captcha build(CaptchaConfig captchaConfig) {
            com.wf.captcha.base.Captcha captcha;
            String captchaType = captchaConfig.getCaptchaType();
            CaptchaType codeEnum = CaptchaType.of(captchaType);
            switch (codeEnum) {
                case Default:
                    captcha = new SpecCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case Chinese:
                    captcha = new ChineseCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case Arithmetic:
                    captcha = new CaptchaBuilder.FixedArithmeticCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case Gif:
                    captcha = new GifCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case ChineseGif:
                    captcha = new ChineseGifCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                default:
                    throw new IllegalArgumentException("验证码类型错误！支持范围: " + Arrays.toString(CaptchaType.values()));
            }

            if (StringUtils.isNotBlank(captchaConfig.getFontName())) {
                captcha.setFont(new Font(captchaConfig.getFontName(), Font.PLAIN, captchaConfig.getFontSize()));
            }
            return captcha;
        }
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        public String text() {
            // 当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
            String captchaValue = super.text();
            String point = ".";
            if (captchaValue.contains(point)) {
                captchaValue = captchaValue.split("\\.")[0];
            }
            return captchaValue;
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

