package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.domain.vo.TagVo;
import com.github.stazxr.zblog.content.ext.converter.FriendLinkConverter;
import com.github.stazxr.zblog.content.ext.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.FriendLink;
import com.github.stazxr.zblog.content.ext.domain.enums.FriendLinkStatus;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.content.ext.mapper.FriendLinkMapper;
import com.github.stazxr.zblog.content.ext.service.FriendLinkService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 友链管理业务实现层
 *
 * @author SunTao
 * @since 2021-03-16
 */
@Service
@RequiredArgsConstructor
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {
    private final FriendLinkConverter friendLinkConverter;

    /**
     * 分页查询友链列表
     *
     * @param queryDto 查询参数
     * @return IPage<FriendLinkVo>
     */
    @Override
    public IPage<FriendLinkVo> queryFriendLinkListByPage(FriendLinkQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getName())) {
            queryDto.setName(queryDto.getName().trim());
        }

        // 分页查询
        Page<TagVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return baseMapper.selectFriendLinkList(page, queryDto);
    }

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链id
     * @return FriendLinkVo
     */
    @Override
    public FriendLinkVo queryFriendLinkDetail(Long friendLinkId) {
        FriendLinkVo friendLinkVo = baseMapper.selectFriendLinkDetail(friendLinkId);
        return ThrowUtils.requireNonNull(friendLinkVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增友链
     *
     * @param friendLinkDto 友链信息
     */
    @Override
    public void addFriendLink(FriendLinkDto friendLinkDto) {
        // 获取友链信息
        FriendLink friendLink = friendLinkConverter.dtoToEntity(friendLinkDto);
        // 设置审批状态为已审批
        friendLink.setStatus(FriendLinkStatus.PENDING.getStatus());
        // 新增时，不允许传入 FriendLinkId
        ThrowUtils.when(friendLink.getId() != null).system(BaseErrorCode.SCOREB001);
        // 友链信息检查
        checkFriendLink(friendLink);
        // 新增友链
        ThrowUtils.when(!save(friendLink)).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑友链
     *
     * @param friendLinkDto 友链信息
     */
    @Override
    public void editFriendLink(FriendLinkDto friendLinkDto) {
        // 获取友链信息
        FriendLink friendLink = friendLinkConverter.dtoToEntity(friendLinkDto);
        // 判断友链是否存在
        FriendLink dbFriendLink = baseMapper.selectById(friendLink.getId());
        ThrowUtils.throwIfNull(dbFriendLink, BaseErrorCode.ECOREA001);
        // 友链信息检查
        checkFriendLink(friendLink);
        // 编辑友链
        ThrowUtils.when(!updateById(friendLink)).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除友链
     *
     * @param friendLinkId 友链ID
     */
    @Override
    public void deleteFriendLink(Long friendLinkId) {
        // 判断友链是否存在
        FriendLink dbFriendLink = baseMapper.selectById(friendLinkId);
        ThrowUtils.throwIfNull(dbFriendLink, BaseErrorCode.ECOREA001);
        // 删除友链
        ThrowUtils.when(!removeById(friendLinkId)).system(BaseErrorCode.SCOREA003);
    }

    private void checkFriendLink(FriendLink friendLink) {
    }
}
