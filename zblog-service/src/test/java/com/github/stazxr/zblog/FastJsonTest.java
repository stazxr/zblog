package com.github.stazxr.zblog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试 FastJson
 *
 * @author Thomas Sun
 * @since 2022-09-15
 */
public class FastJsonTest {
    @Test
    @Ignore
    public void test1() {
        Map<String, Long> map = new HashMap<>();
        map.put("test", 1569612537647702017L);
        System.out.println(JSON.toJSONString(map, SerializerFeature.WriteBigDecimalAsPlain));
    }

    @Test
    @Ignore
    public void test2() {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("test", new BigDecimal("0.1321"));
        System.out.println(JSON.toJSONString(map, SerializerFeature.WriteBigDecimalAsPlain));
    }
}
