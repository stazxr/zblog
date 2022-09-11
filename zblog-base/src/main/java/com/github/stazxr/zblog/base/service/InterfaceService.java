package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Interface;

/**
 * 接口业务层
 *
 * @author SunTao
 * @since 2021-06-23
 */
public interface InterfaceService extends IService<Interface> {
    /**
     * 移除所有的接口信息
     */
    void clearInterface();
}
