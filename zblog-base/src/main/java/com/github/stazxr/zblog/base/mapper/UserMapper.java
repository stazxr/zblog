package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.core.base.BaseMapper;

import java.util.List;

/**
 * 用户数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询用户列表
     *
     * @param queryDto 查询参数
     * @return userList
     */
    List<UserVo> selectUserList(UserQueryDto queryDto);
}
