package com.github.stazxr.zblog.content.ext.service;

import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;

/**
 * 友链健康检测业务层
 *
 * @author SunTao
 * @since 2026-08-31
 */
public interface FriendLinkHealthService {
    /**
     * 检测单个友链
     *
     * @param friendLink 友链
     */
    void checkFriendLink(FriendLinkVo friendLink);
}
