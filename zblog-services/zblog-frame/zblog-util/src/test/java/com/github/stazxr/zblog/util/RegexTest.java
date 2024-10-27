package com.github.stazxr.zblog.util;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 正则测试工具类
 *
 * @author Thomas Sun
 * @since 2023-04-13
 */
public class RegexTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        String str = "6566.82+({混凝土C40}-495.1)*12.69";
        String str2 = "6566.82+({混凝土C40}-495.1-{混凝土C41})*12.69";
        String str3 = "6566.82+({混凝土C40XX${}}}-495.1-{混凝土C41)*12.69";
        String regex = "\\{[^}]+\\}";
        String regex2 = "\\$\\{[^}]+\\}";

        System.out.println(RegexUtils.findGroup(str, regex));
        System.out.println(RegexUtils.findGroup(str, regex2));
        System.out.println(RegexUtils.findGroup(str2, regex));
        System.out.println(RegexUtils.findGroup(str3, regex));
    }
}
