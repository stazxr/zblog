package com.github.stazxr.zblog.util;

import org.junit.Ignore;
import org.junit.Test;

public class ThrowableUtilsTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        try {
             throw new RuntimeException("Test exception");
        } catch (Exception e) {
            System.out.println(ThrowableUtils.getStackTrace(e));
            System.out.println("========================");
            System.out.println(ThrowableUtils.buildMessage("发生未知异常", e));
            System.out.println("========================");
            System.out.println(ThrowableUtils.getStackTrace(ThrowableUtils.getRootCause(e)));
            System.out.println("========================");
            System.out.println(ThrowableUtils.getStackTrace(ThrowableUtils.getMostSpecificCause(e)));
        }
    }
}
