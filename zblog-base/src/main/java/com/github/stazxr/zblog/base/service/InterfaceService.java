package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.log.domain.vo.LogVo;

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

    /**
     * 查询权限对应的接口列表
     *
     * @param queryDto 查询参数
     * @return interfaceList
     */
    PageInfo<InterfaceVo> queryPermInterface(InterfaceQueryDto queryDto);
}
