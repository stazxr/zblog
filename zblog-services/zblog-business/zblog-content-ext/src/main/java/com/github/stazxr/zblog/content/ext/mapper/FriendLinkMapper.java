package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.domain.vo.TagVo;
import com.github.stazxr.zblog.content.ext.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.FriendLink;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
    IPage<FriendLinkVo> selectFriendLinkList(@Param("page") Page<TagVo> page, @Param("query") FriendLinkQueryDto queryDto);

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链id
     * @return FriendLinkVo
     */
    FriendLinkVo selectFriendLinkDetail(@Param("friendLinkId") Long friendLinkId);


//    /**
//     * 查询友链列表
//     *
//     * @param queryDto 查询参数
//     * @return FriendLinkVoLink
//     */
//    List<FriendLinkVo> selectFriendLinkList(FriendLinkQueryDto queryDto);
//
//    /**
//     * 查询友链详情
//     *
//     * @param friendLinkId 友链序号
//     * @return FriendLinkVo
//     */
//    FriendLinkVo selectFriendLinkDetail(@Param("friendLinkId") Long friendLinkId);
//
//    /**
//     * 根据友链地址查询友链信息
//     *
//     * @param linkUrl 友链地址
//     * @return FriendLink
//     */
//    FriendLink selectByLinkUrl(@Param("linkUrl") String linkUrl);
//
//    /**
//     * 查询友链列表
//     *
//     * @return FriendLinkVoList
//     */
//    List<FriendLinkVo> selectWebFriendLinkList();
}
