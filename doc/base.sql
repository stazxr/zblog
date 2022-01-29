/*
SQLyog Community v13.1.7 (64 bit)
MySQL - 5.7.29-log : Database - zblog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zblog` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `zblog`;

/*Table structure for table `user` */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `NICKNAME` VARCHAR(20) NOT NULL COMMENT '昵称',
  `USERNAME` VARCHAR(20) NOT NULL COMMENT '用户名',
  `PASSWORD` VARCHAR(60) NOT NULL COMMENT '密码',
  `EMAIL` VARCHAR(100) NOT NULL DEFAULT NULL COMMENT '邮箱',
  `TELEPHONE` VARCHAR(11) NOT NULL DEFAULT NULL COMMENT '手机号',
  `QQ` VARCHAR(15) NOT NULL DEFAULT NULL COMMENT 'QQ',
  `GENDER` TINYINT(1) NOT NULL COMMENT '权限类型: 1-男;2-女;3-隐藏',
  `SIGNATURE` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '签名',
  `HEAD_IMG_URL` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '头像地址',
  `LOGIN_TIME` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '最后一次登录时间',
  `CHANGE_PWD_TIME` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '最后一次修改密码时间',
  `CHANGE_PWD` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否需要登陆修改密码',
  `LOCKED` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '账户是否登录锁定',
  `TEMP` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否是临时账户',
  `EXPIRED_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '临时账户的过期时间',
  `ADMIN` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否是系统管理员',
  `BUILD_IN` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否是内置用户',
  `ENABLED` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `DELETED` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除',
  `VERSION` INT(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `USERNAME` (`USERNAME`),
  UNIQUE KEY `NICKNAME` (`NICKNAME`),
  UNIQUE KEY `EMAIL` (`EMAIL`),
  UNIQUE KEY `TELEPHONE` (`TELEPHONE`),
  UNIQUE KEY `QQ` (`QQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';

/*Data for the table `user` */
INSERT INTO user (ID, NICKNAME, USERNAME, PASSWORD, EMAIL, TELEPHONE, QQ, GENDER, SIGNATURE, HEAD_IMG_URL, LOGIN_TIME, CHANGE_PWD_TIME, LOCKED, TEMP, EXPIRED_TIME, ADMIN, BUILD_IN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (1, '管理员', 'admin', '$2a$10$YycIf.XoN2Wl/vsoqVFZPOvyLnhv2rJNOpy2GZtPldZbqhvcNeb5a', 'stazxr@qq.com', '', '', '3', 'github开源网址：https://github.com/stazxr/zblog，创作不易，感觉还可以请star，比心', '', '', '', 0, 0, '', 1, 1, 1, 0, 1, 'system', '2021-05-17 01:48:00', '2021-05-17', '', '');

/*Table structure for table `role` */
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `ROLE_NAME` VARCHAR(25) NOT NULL COMMENT '角色名称',
  `ROLE_CODE` VARCHAR(25) NOT NULL COMMENT '角色编码',
  `DESC` VARCHAR(127) NOT NULL DEFAULT '' COMMENT '角色描述',
  `ENABLED` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '角色状态',
  `DELETED` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除',
  `VERSION` INT(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `ROLE_NAME` (`ROLE_NAME`),
  UNIQUE KEY `ROLE_CODE` (`ROLE_CODE`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色表';

/*Table structure for table `user_role_relation` */
DROP TABLE IF EXISTS `user_role_relation`;
CREATE TABLE `user_role_relation` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `USER_ID` BIGINT(64) NOT NULL COMMENT '用户序列',
  `ROLE_ID` BIGINT(64) NOT NULL COMMENT '角色序列',
  `VERSION` INT(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `USER_ROLE` (`USER_ID`, `ROLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

/*Table structure for table `permission` */
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `PERM_NAME` VARCHAR(20) NOT NULL COMMENT '权限名称',
  `PID` BIGINT(64) DEFAULT NULL COMMENT '上级权限序列',
  `ROUTER_ID` BIGINT(64) DEFAULT NULL COMMENT '路由序列',
  `PERM_PATH` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '权限路径',
  `PERM_TYPE` TINYINT(1) NOT NULL COMMENT '权限类型: 1-目录;2-菜单;3-按钮',
  `LEVEL` TINYINT(1) NOT NULL COMMENT '访问级别: 1-公开权限;2-公共权限;3-受控权限',
  `ICON` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '权限图标（只有目录和菜单有）',
  `SORT` INT(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `ENABLED` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '权限状态',
  `DELETED` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `VERSION` INT(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `PERM_NAME` (`PERM_NAME`),
  UNIQUE KEY `ROUTER_ID` (`ROUTER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='权限表';

/*Table structure for table `role_permission_relation` */
DROP TABLE IF EXISTS `role_permission_relation`;
CREATE TABLE `role_permission_relation` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `ROLE_ID` BIGINT(64) NOT NULL COMMENT '角色序列',
  `PERM_ID` BIGINT(64) NOT NULL COMMENT '权限序列',
  `VERSION` INT(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `ROLE_PERM` (`ROLE_ID`, `PERM_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色权限关联表';

/*Table structure for table `router` */
DROP TABLE IF EXISTS `router`;
CREATE TABLE `router` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `NAME` VARCHAR(25) NOT NULL COMMENT '路由名称',
  `CODE` VARCHAR(200) DEFAULT NULL COMMENT '权限编码',
  `LEVEL` TINYINT(1) NOT NULL COMMENT '访问级别: 1-公开权限;2-公共权限;3-受控权限',
  `URL` VARCHAR(1000) NOT NULL COMMENT '路由链接',
  `METHOD` VARCHAR(50) DEFAULT NULL COMMENT '路由请求方式',
  `STATUS` VARCHAR(50) DEFAULT NULL COMMENT '路由状态',
  `REMARK` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '备注',
  `VERSION` INT(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='路由表';

/*Table structure for table `dict` */
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `NAME` VARCHAR(50) NOT NULL COMMENT '字典名称',
  `KEY` VARCHAR(50) DEFAULT NULL COMMENT '字典 key',
  `VALUE` TEXT DEFAULT NULL COMMENT '字典 value',
  `PID` BIGINT(64) DEFAULT NULL COMMENT '父ID',
  `TYPE` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '类型: 1-组;2-项',
  `SORT` INT(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `DESC` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '字典描述',
  `LOCKED` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否允许编辑,删除',
  `UNIQUE` TINYINT(1) NOT NULL DEFAULT '0' COMMENT 'key是否唯一',
  `ENABLED` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `DELETED` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除',
  `VERSION` INT(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='字典表';

/*Data for the table `dict` */
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (1, '系统参数', null, null, null, 1, 1, '系统参数', 1, 1, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (2, '路由白名单', null, null, null, 1, 2, '路由白名单', 1, 1, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3, '路由黑名单', null, null, null, 1, 3, '路由黑名单', 1, 1, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (4, '错误处理', 'routerWhiteList', '/error', 2, 2, 1, '', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (5, 'swagger文档', 'routerWhiteList', '/v2/api-docs', 2, 2, 2, '', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (6, 'swaggerUi配置', 'routerWhiteList', '/configuration/ui', 2, 2, 3, '', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (7, 'swagger资源配置', 'routerWhiteList', '/swagger-resources', 2, 2, 3, '', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (8, 'swagger安全配置', 'routerWhiteList', '/configuration/security', 2, 2, 4, '', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;