package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.entity.User;
import com.github.stazxr.zblog.core.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * 用户数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 设置用户的登录时间
     *
     * @param username  用户名
     * @param loginTime 登录时间
     */
    @Update("UPDATE sys_user_tbl SET `LAST_LOGIN_TIME`=#{loginTime} WHERE `USERNAME`=#{username}")
    void setUserLoginTime(String username, String loginTime);

    /**
     * 删除用户对应的角色信息
     *
     * @param userId 用户Id
     */
    @Delete("DELETE FROM sys_user_role_tbl WHERE `USER_ID`=#{userId}")
    void clearUserRoleByUserId(Long userId);

    /**
     * 用户授权
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     */
    @Insert("INSERT INTO sys_user_role_tbl(`USER_ID`, `ROLE_ID`) VALUES(#{userId}, #{roleId})")
    void saveUserRole(Long userId, Long roleId);

    /**
     * 修改密码
     *
     * @param userId   用户ID
     * @param password 新密码
     * @param changTime 修改时间
     * @return boolean
     */
    @Update("UPDATE sys_user_tbl SET `PASSWORD`=#{password}, `CHANGE_PWD`=0, `LAST_CHANGE_PWD_TIME`=#{changTime} WHERE `ID`=#{userId}")
    boolean updateUserPwd(Long userId, String password, String changTime);

    /**
     * 修改用户密码状态
     *
     * @param username 用户名
     * @param status   状态
     */
    @Update("UPDATE sys_user_tbl SET `CHANGE_PWD`=#{status} WHERE `USERNAME`=#{username}")
    void updateUserPwdStatus(String username, boolean status);
}
