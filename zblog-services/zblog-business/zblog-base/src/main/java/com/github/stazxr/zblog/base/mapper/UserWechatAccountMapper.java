package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.UserWechatAccount;
import com.github.stazxr.zblog.base.domain.vo.UserWechatAccountVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户微信公众号配置数据层
 *
 * @author SunTao
 * @since 2026-10-30
 */
public interface UserWechatAccountMapper extends BaseMapper<UserWechatAccount> {
    /**
     * 查询个人微信公众号配置信息
     *
     * @param userId 用户id
     * @return UserWechatAccountVo 微信公众号配置信息
     */
    UserWechatAccountVo selectByUserId(@Param("userId") Long userId);
}
