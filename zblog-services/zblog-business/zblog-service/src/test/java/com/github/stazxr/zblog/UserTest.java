package com.github.stazxr.zblog;

import com.github.stazxr.zblog.base.domain.entity.User;
import org.junit.Ignore;
import org.junit.Test;

public class UserTest {
    @Test
    @Ignore
    public void testUserToString() {
        User user = new User();
        user.setId(11L);
        user.setUsername("suntao");
        user.setPassword("password");
        user.setEmail("1027353579@qq.com");
        user.setEnabled(true);
        user.setCreateUser(1L);
        System.out.println("TOString: " + user);
    }
}
