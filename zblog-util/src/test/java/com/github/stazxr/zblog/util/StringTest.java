package com.github.stazxr.zblog.util;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 字符串测试工具类
 *
 * @author Thomas Sun
 * @since 2022-09-28
 */
public class StringTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        String tmp = "1111,2222,3333,";
        System.out.println(tmp.substring(0, tmp.length() - 1));
    }
}
