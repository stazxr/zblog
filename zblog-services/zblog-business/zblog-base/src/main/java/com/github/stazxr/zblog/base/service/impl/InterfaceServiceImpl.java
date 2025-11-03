package com.github.stazxr.zblog.base.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典服务实现层
 *
 * @author SunTao
 * @since 2025-11-02
 */
@Service
@RequiredArgsConstructor
public class InterfaceServiceImpl implements InterfaceService {
    private final InterfaceMapper interfaceMapper;

    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<DictVo>
     */
    @Override
    public PageInfo<InterfaceVo> queryInterfaceListByPage(InterfaceQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getInterfaceName())) {
            queryDto.setInterfaceName(queryDto.getInterfaceName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getInterfaceCode())) {
            queryDto.setInterfaceCode(queryDto.getInterfaceCode().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getInterfaceUri())) {
            queryDto.setInterfaceUri(queryDto.getInterfaceUri().trim());
        }
        // 分页查询
        try (Page<InterfaceVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<InterfaceVo> dataList = interfaceMapper.selectInterfaceList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }
}
