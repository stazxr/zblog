package com.github.stazxr.zblog;

import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.service.RoleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "PROFILES=prod",
    "SERVER_PORT=19992",
    "jasypt.encryptor.password=xxx",
    "spring.mail.password=xxx"
})
@Disabled
public class MybatisPlusTest {
    @Autowired
    private RoleService roleService;

    @Test
    public void testOptimisticLocker() {
        // 乐观锁不会生效
        Role role = new Role();
        role.setId(3561010960472735744L);
        role.setRoleName("普通用户");
        roleService.updateById(role);
    }

    @Test
    public void testOptimisticLocker2() {
        // 乐观锁生效，需要先查再改
        Role role = roleService.getById(3561011719994081280L);
        role.setRoleName("测试用户");
        roleService.updateById(role);
    }
}
