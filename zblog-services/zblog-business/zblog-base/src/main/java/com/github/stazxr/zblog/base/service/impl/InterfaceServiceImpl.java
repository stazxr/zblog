package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.service.InterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 接口业务实现层
 *
 * @author SunTao
 * @since 2021-06-23
 */
@Service
@RequiredArgsConstructor
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface> implements InterfaceService {
    private final InterfaceMapper interfaceMapper;

    /**
     * 移除所有的接口信息
     */
    @Override
    public void clearInterface() {
        interfaceMapper.clearInterface();
    }
}
