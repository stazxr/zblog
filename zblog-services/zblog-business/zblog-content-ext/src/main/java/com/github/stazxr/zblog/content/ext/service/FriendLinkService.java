package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.ext.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.FriendLink;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;

/**
 * 友链管理业务层
 *
 * @author SunTao
 * @since 2021-03-16
 */
public interface FriendLinkService extends IService<FriendLink> {
    /**
     * 分页查询友链列表
     *
     * @param queryDto 查询参数
     * @return IPage<FriendLinkVo>
     */
    IPage<FriendLinkVo> queryFriendLinkListByPage(FriendLinkQueryDto queryDto);

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链id
     * @return FriendLinkVo
     */
    FriendLinkVo queryFriendLinkDetail(Long friendLinkId);

    /**
     * 新增友链
     *
     * @param friendLinkDto 友链信息
     */
    void addFriendLink(FriendLinkDto friendLinkDto);

    /**
     * 编辑友链
     *
     * @param friendLinkDto 友链信息
     */
    void editFriendLink(FriendLinkDto friendLinkDto);

    /**
     * 删除友链
     *
     * @param friendLinkId 友链ID
     */
    void deleteFriendLink(Long friendLinkId);
}
