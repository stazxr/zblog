package com.github.stazxr.zblog.base.service;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateEmailDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateHeadImgDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateSelfDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserLogQueryDto;
import com.github.stazxr.zblog.log.domain.vo.LogVo;

/**
 * 用户中心管理业务层
 *
 * @author SunTao
 * @since 2025-12-26
 */
public interface UserCenterService {
    /**
     * 强制修改密码
     *
     * @param passDto 用户密码信息
     */
    void forceUpdatePass(UserUpdatePassDto passDto);

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     */
    void updateUserPass(UserUpdatePassDto passDto);

    /**
     * 修改个人头像
     *
     * @param headImgDto 用户头像信息
     */
    void updateUserHeadImg(UserUpdateHeadImgDto headImgDto);

    /**
     * 修改个人邮箱
     *
     * @param emailDto 用户邮箱信息
     */
    void updateUserEmail(UserUpdateEmailDto emailDto);

    /**
     * 修改个人信息
     *
     * @param selfDto 用户个人信息
     */
    void updateUserSelfInfo(UserUpdateSelfDto selfDto);

    /**
     * 分页查询用户操作日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    PageInfo<LogVo> queryUserLogListByPage(UserLogQueryDto queryDto);
}
