package com.github.stazxr.zblog.util.math;

import java.util.Random;

/**
 * 随机数工具类
 *
 * @author SunTao
 * @since 2021-03-21
 */
public class RandomUtils {
    /**
     * 生成六位随机验证码
     *
     * @return 六位随机验证码
     */
    public static String sixCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int num = random.nextInt(max - min) + min;
        return String.valueOf(num);
    }

    /**
     * 生成四位随机验证码
     *
     * @return 四位随机验证码
     */
    public static String fourCode() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        int num = random.nextInt(max - min) + min;
        return String.valueOf(num);
    }
}
