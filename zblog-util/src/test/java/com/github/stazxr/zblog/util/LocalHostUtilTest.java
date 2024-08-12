package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.net.LocalHostUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

@Ignore
public class LocalHostUtilTest {
    @Test
    public void testAllMethod() throws Exception {
        System.out.println(LocalHostUtils.getLocalHostName());
        System.out.println(LocalHostUtils.getLocalHostAddress());
        System.out.println(LocalHostUtils.getLocalIp());
        System.out.println(Arrays.toString(LocalHostUtils.getLocalIps()));
        System.out.println(LocalHostUtils.getMacAddress());
    }
}
