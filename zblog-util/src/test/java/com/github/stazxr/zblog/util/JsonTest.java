package com.github.stazxr.zblog.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    @Ignore
    public void test2() throws Exception {
        List<Object> argList = new ArrayList<>();
        User user = new User();
        user.setName("11");
        user.setUsername("222");
        user.setIsAdmin(false);
        argList.add(user);
        Map<String, Object> param2 = new HashMap<>();
        param2.put("param2", "test");
        argList.add(param2);
        argList.add("param3");
        System.out.println(argList.isEmpty() ? "" : JSON.toJSONString(argList));
    }

    @Data
    static class User {
        private String name;

        private String username;

        private Boolean isAdmin;
    }
}
