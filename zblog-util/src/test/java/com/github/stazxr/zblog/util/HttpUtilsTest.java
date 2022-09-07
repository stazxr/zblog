package com.github.stazxr.zblog.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.util.http.HttpUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试HTTP请求工具类
 *
 * @author Thomas Sun
 * @since 2022-09-07
 */
public class HttpUtilsTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("corpid", "corpid");
        param.put("corpsecret", "corpsecret");
        String result = HttpUtils.get("http://httpbin.org/get", param, null);
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject);
    }
}
