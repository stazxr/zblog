package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.domain.entity.FriendLink;
import com.github.stazxr.zblog.domain.vo.FriendLinkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 友链数据持久层
 *
 * @author SunTao
 * @since 2021-03-16
 */
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
    /**
     * 查询友链列表
     *
     * @param queryDto 查询参数
     * @return FriendLinkVoLink
     */
    List<FriendLinkVo> selectFriendLinkList(FriendLinkQueryDto queryDto);

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链序号
     * @return FriendLinkVo
     */
    FriendLinkVo selectFriendLinkDetail(@Param("friendLinkId") Long friendLinkId);

    /**
     * 根据友链地址查询友链信息
     *
     * @param linkUrl 友链地址
     * @return FriendLink
     */
    FriendLink selectByLinkUrl(@Param("linkUrl") String linkUrl);

    /**
     * 查询友链列表
     *
     * @return FriendLinkVoList
     */
    List<FriendLinkVo> selectWebFriendLinkList();
}
