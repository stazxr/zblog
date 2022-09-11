package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.service.InterfaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 接口业务实现层
 *
 * @author SunTao
 * @since 2021-06-23
 */
@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface> implements InterfaceService {
    @Resource
    private InterfaceMapper interfaceMapper;

    /**
     * 移除所有的接口信息
     */
    @Override
    public void clearInterface() {
        interfaceMapper.clearInterface();
    }
}
