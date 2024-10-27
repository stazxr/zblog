package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.domain.vo.BlogWebVo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 门户数据持久层
 *
 * @author SunTao
 * @since 2022-12-09
 */
public interface PortalMapper {
    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    BlogWebVo selectBlogWebInfo();

    /**
     * 查询评论点赞列表
     *
     * @param userId 用户ID
     * @return likes
     */
    Set<Long> selectCommentLikeSet(@Param("userId") Long userId);

    /**
     * 查询说说点赞列表
     *
     * @param userId 用户ID
     * @return likes
     */
    Set<Long> selectTalkLikeSet(@Param("userId") Long userId);

    /**
     * 查询文章点赞列表
     *
     * @param userId 用户ID
     * @return likes
     */
    Set<Long> selectArticleLikeSet(@Param("userId") Long userId);

    /**
     * 根据 qq openid 查询关联用户id
     *
     * @param openId qq openId
     * @return userId
     */
    Long selectUserIdByQqOpenId(@Param("openId") String openId);

    /**
     * 插入 qq openid 和用户的关联关系
     *
     * @param userId 用户id
     * @param openId openid
     */
    void insertUserOauthQqRelation(@Param("userId") Long userId, @Param("openId") String openId);
}
