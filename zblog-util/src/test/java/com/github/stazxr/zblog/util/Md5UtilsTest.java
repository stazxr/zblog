package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.secret.Md5Utils;
import com.github.stazxr.zblog.util.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.github.stazxr.zblog.util.time.DateUtils.YMD_PATTERN;

/**
 * 测试日期工具类
 *
 * @author Thomas Sun
 * @since 2022-08-19
 */
public class Md5UtilsTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        String str = "1.0.0.0>Googol>Henan";
        String s1 = Md5Utils.md5Hex(str);
        String s2 = Md5Utils.md5Hex(str);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(DigestUtils.md5DigestAsHex(s1.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(s2.getBytes()));
    }
}
