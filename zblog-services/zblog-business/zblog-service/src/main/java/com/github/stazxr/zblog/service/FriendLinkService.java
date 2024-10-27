package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.domain.entity.FriendLink;
import com.github.stazxr.zblog.domain.vo.FriendLinkVo;

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
     * @return FriendLinkVoList
     */
    PageInfo<FriendLinkVo> queryFriendLinkListByPage(FriendLinkQueryDto queryDto);

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链ID
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
