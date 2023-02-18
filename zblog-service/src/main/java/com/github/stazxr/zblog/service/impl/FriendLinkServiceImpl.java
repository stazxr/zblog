package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.converter.FriendLinkConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.domain.entity.FriendLink;
import com.github.stazxr.zblog.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.mapper.FriendLinkMapper;
import com.github.stazxr.zblog.service.FriendLinkService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @return FriendLinkVoList
     */
    @Override
    public PageInfo<FriendLinkVo> queryFriendLinkListByPage(FriendLinkQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectFriendLinkList(queryDto));
    }

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链ID
     * @return FriendLinkVo
     */
    @Override
    public FriendLinkVo queryFriendLinkDetail(Long friendLinkId) {
        Assert.notNull(friendLinkId, "friendLinkId【tagId】不能为空");
        FriendLinkVo friendLinkVo = baseMapper.selectFriendLinkDetail(friendLinkId);
        Assert.notNull(friendLinkVo, "查询友链信息失败，友链【" + friendLinkId + "】不存在");
        return friendLinkVo;
    }

    /**
     * 新增友链
     *
     * @param friendLinkDto 友链信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriendLink(FriendLinkDto friendLinkDto) {
        friendLinkDto.setId(null);
        FriendLink friendLink = friendLinkConverter.dtoToEntity(friendLinkDto);
        checkFriendLink(friendLink);
        Assert.isTrue(baseMapper.insert(friendLink) != 1, "新增失败");
    }

    /**
     * 编辑友链
     *
     * @param friendLinkDto 友链信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editFriendLink(FriendLinkDto friendLinkDto) {
        Assert.notNull(friendLinkDto.getId(), "参数【id】不能为空");
        FriendLink friendLink = friendLinkConverter.dtoToEntity(friendLinkDto);
        checkFriendLink(friendLink);
        Assert.isTrue(baseMapper.updateById(friendLink) != 1, "修改失败");
    }

    /**
     * 删除友链
     *
     * @param friendLinkId 友链ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriendLink(Long friendLinkId) {
        Assert.notNull(friendLinkId, "参数【friendLinkId】不能为空");
        Assert.isTrue(baseMapper.deleteById(friendLinkId) != 1, "删除失败");
    }

    private void checkFriendLink(FriendLink friendLink) {
        Assert.isTrue(StringUtils.isBlank(friendLink.getName()), "友链名称不能为空");
        Assert.isTrue(StringUtils.isBlank(friendLink.getLinkUrl()), "友链地址不能为空");
        Assert.notNull(friendLink.getValid(), "友链状态不能为空");

        FriendLink dbLink = baseMapper.selectByLinkUrl(friendLink.getLinkUrl());
        boolean isExist = dbLink != null && !dbLink.getId().equals(friendLink.getId());
        DataValidated.isTrue(isExist, "友链[" + friendLink.getLinkUrl() + "]已存在");
    }
}
