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
        }
    }
}
