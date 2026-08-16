package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.FriendLink;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 友链管理数据层
 *
 * @author SunTao
 * @since 2021-03-16
 */
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
    /**
     * 分页查询友链列表
     *
     * @param queryDto 查询参数
     * @return IPage<FriendLinkVo>
     */
    IPage<FriendLinkVo> selectFriendLinkList(@Param("page") Page<FriendLinkVo> page, @Param("query") FriendLinkQueryDto queryDto);

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链id
     * @return FriendLinkVo
     */
    FriendLinkVo selectFriendLinkDetail(@Param("friendLinkId") Long friendLinkId);

    /**
     * 查询前台友链列表
     *
     * @return FriendLinkVoList
     */
    List<FriendLinkVo> selectWebFriendLinkList();
}
