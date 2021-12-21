//package com.github.stazxr.zblog.base.mapper;
//
//import com.github.stazxr.zblog.base.domain.entity.Permission;
//import com.github.stazxr.zblog.core.base.BaseMapper;
//import org.apache.ibatis.annotations.Select;
//
//import java.util.List;
//import java.util.Set;
//
///**
// * 权限数据持久层
// *
// * @author SunTao
// * @since 2020-11-15
// */
//public interface PermissionMapper extends BaseMapper<Permission> {
//    /**
//     * 根据用户编号获取菜单列表
//     *
//     * @param username 用户名
//     * @return 菜单列表
//     */
//    @Select("SELECT * FROM sys_permission_tbl WHERE `DELETED` IS FALSE AND `ACTIVE` IS TRUE AND `TYPE` IN ('DIR', 'MENU') AND " +
//            "(" +
//                "`LEVEL` = 'PUBLIC' OR `LEVEL` = 'OPEN' OR `ID` IN " +
//                "(" +
//                    "SELECT `PERMISSION_ID` FROM sys_role_permission_tbl WHERE `ROLE_ID` IN " +
//                    "(" +
//                        "SELECT `ROLE_ID` FROM sys_role_tbl WHERE `DELETED` IS FALSE AND `ROLE_ID` IN " +
//                        "(" +
//                            "SELECT `ROLE_ID` FROM sys_user_role_tbl WHERE `USER_ID` = " +
//                            "(" +
//                                "SELECT `ID` FROM sys_user_tbl WHERE `DELETED` IS FALSE AND `USERNAME`=#{username}" +
//                            ")" +
//                        ")" +
//                    ")" +
//                ")" +
//            ") ORDER BY `ORDER` ASC"
//    )
//    List<Permission> selectUserMenus(String username);
//
//    /**
//     * 查询用户对应的权限列表
//     *
//     * @param username 用户名
//     * @return perms，用户拥有的权限信息
//     */
//    @Select(
//        "SELECT DISTINCT `PERM` FROM sys_permission_tbl WHERE `DELETED` IS FALSE AND `ACTIVE` IS TRUE AND `PERM` IS NOT NULL AND " +
//        "(" +
//            "`LEVEL` = 'PUBLIC' OR `LEVEL` = 'OPEN' OR `ID` IN " +
//            "(" +
//                "SELECT `PERMISSION_ID` FROM sys_role_permission_tbl WHERE `ROLE_ID` IN " +
//                "(" +
//                    "SELECT `ROLE_ID` FROM sys_role_tbl WHERE `DELETED` IS FALSE AND `ROLE_ID` IN " +
//                    "(" +
//                        "SELECT `ROLE_ID` FROM sys_user_role_tbl WHERE `USER_ID` = " +
//                        "(" +
//                            "SELECT `ID` FROM sys_user_tbl WHERE `DELETED` IS FALSE AND `USERNAME`=#{username}" +
//                        ")" +
//                    ")" +
//                ")" +
//            ")" +
//        ")"
//    )
//    Set<String> selectUserPerms(String username);
//}
