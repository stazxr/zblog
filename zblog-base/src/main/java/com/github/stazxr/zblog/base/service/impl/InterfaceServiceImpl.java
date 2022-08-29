package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.util.Assert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 查询权限对应的接口列表
     *
     * @param queryDto 查询参数
     * @return interfaceList
     */
    @Override
    public PageInfo<InterfaceVo> queryPermInterface(InterfaceQueryDto queryDto) {
        Assert.notNull(queryDto.getPage(), "参数page不能为空");
        Assert.notNull(queryDto.getPageSize(), "参数pageSize不能为空");
        Assert.notNull(queryDto.getPermId(), "权限ID不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<InterfaceVo> dataList = interfaceMapper.selectInterfaceList(queryDto);
        return new PageInfo<>(dataList);
    }
}
