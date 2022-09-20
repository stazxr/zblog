package com.github.stazxr.zblog.util;

import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JSON测试类
 *
 * @author Thomas Sun
 * @since 2022-09-07
 */
public class JsonTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        System.out.println(JSON.toJSONString(null));
    }
}
