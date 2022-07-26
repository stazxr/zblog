package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.base.mapper.ZblogMapper;
import com.github.stazxr.zblog.base.service.ZblogService;
import com.github.stazxr.zblog.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 部分公共接口实现
 *
 * @author SunTao
 * @since 2022-07-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ZblogServiceImpl implements ZblogService {
    @Resource
    private ZblogMapper zblogMapper;

    /**
     * 清除记住我信息
     *
     * @param username 用户名
     */
    @Override
    public void removeRememberMe(String username) {
        Assert.notNull(username, "用户名不能为空");
        zblogMapper.removeRememberMe(username);
    }
}
