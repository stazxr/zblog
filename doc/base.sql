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
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zblog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zblog`;

/*Table structure for table `persistent_logins` */
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `user` */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `NICKNAME` VARCHAR(20) NOT NULL COMMENT '昵称',
  `USERNAME` VARCHAR(20) NOT NULL COMMENT '用户名',
  `PASSWORD` VARCHAR(60) NOT NULL COMMENT '密码',
  `EMAIL` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `TELEPHONE` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
  `QQ` VARCHAR(15) DEFAULT NULL COMMENT 'QQ',
  `GENDER` TINYINT(2) NOT NULL COMMENT '性别: 1-男;2-女;3-隐藏',
  `SIGNATURE` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '签名',
  `HEAD_IMG_URL` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '头像地址',
  `LOGIN_TIME` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '最后一次登录时间',
  `CHANGE_PWD_TIME` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '最后一次修改密码时间',
  `CHANGE_PWD` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否需要登陆修改密码',
  `LOCKED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '账户是否登录锁定',
  `TEMP` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否是临时账户',
  `EXPIRED_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '临时账户的过期时间',
  `ADMIN` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否是系统管理员',
  `BUILD_IN` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否是内置用户',
  `ENABLED` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_USERNAME` (`USERNAME`),
  UNIQUE KEY `KEY_NICKNAME` (`NICKNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

/*Data for the table `user` */
INSERT INTO user (ID, NICKNAME, USERNAME, PASSWORD, EMAIL, TELEPHONE, QQ, GENDER, SIGNATURE, HEAD_IMG_URL, LOGIN_TIME, CHANGE_PWD_TIME, LOCKED, TEMP, EXPIRED_TIME, ADMIN, BUILD_IN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (1, '管理员', 'admin', '$2a$10$S9s8OySDf1B.0HerBauT1.QnrrOzsmAOU7rTXhVJL3UY2Z02Ul6FG', 'stazxr@qq.com', '', '', '3', 'https://github.com/stazxr/zblog', '', '', '', 0, 0, '', 1, 1, 1, 0, 1, 'system', '2021-05-17 01:48:00', '2021-05-17', '', '');

/*Table structure for table `role` */
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `ROLE_NAME` VARCHAR(25) NOT NULL COMMENT '角色名称',
  `ROLE_CODE` VARCHAR(25) NOT NULL COMMENT '角色编码',
  `DESC` VARCHAR(127) NOT NULL DEFAULT '' COMMENT '角色描述',
  `ENABLED` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '角色状态',
  `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_ROLE_NAME` (`ROLE_NAME`),
  UNIQUE KEY `KEY_ROLE_CODE` (`ROLE_CODE`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

/*Table structure for table `user_role_relation` */
DROP TABLE IF EXISTS `user_role_relation`;
CREATE TABLE `user_role_relation` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `USER_ID` BIGINT(64) NOT NULL COMMENT '用户编号',
  `ROLE_ID` BIGINT(64) NOT NULL COMMENT '角色编号',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_USER_ROLE` (`USER_ID`, `ROLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

/*Table structure for table `permission` */
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `PID` BIGINT(64) DEFAULT NULL COMMENT '上级权限编号',
  `SUB_COUNT` TINYINT(5) DEFAULT 0 COMMENT '子节点数目',
  `PERM_NAME` VARCHAR(50) NOT NULL COMMENT '目录名称/菜单名称/权限名称',
  `PERM_TYPE` TINYINT(5) NOT NULL COMMENT '权限类型: 1-目录;2-菜单;3-按钮',
  `PERM_CODE` VARCHAR(20) DEFAULT NULL COMMENT '权限编码',
  `PERM_LEVEL` TINYINT(5) NOT NULL COMMENT '访问级别: 1-公开权限;2-公共权限;3-受控权限',
  `COMPONENT_NAME` VARCHAR(50) DEFAULT NULL COMMENT '组件名称',
  `COMPONENT_PATH` VARCHAR(100) DEFAULT NULL COMMENT '组件路径',
  `ROUTER_PATH` VARCHAR(1000) DEFAULT NULL COMMENT '前端路由地址',
  `ICON` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '权限图标（只有目录和菜单有）',
  `SORT` INT(11) NOT NULL DEFAULT 99999 COMMENT '排序字段',
  `CACHE` TINYINT(1) DEFAULT NULL COMMENT '是否缓存',
  `HIDDEN` TINYINT(1) DEFAULT NULL COMMENT '是否隐藏',
  `I_FRAME` TINYINT(1) DEFAULT NULL COMMENT '是否外链',
  `ENABLED` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
  `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_PERM_NAME` (`PERM_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限表';

/*Table structure for table `role_permission_relation` */
DROP TABLE IF EXISTS `role_permission_relation`;
CREATE TABLE `role_permission_relation` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `ROLE_ID` BIGINT(64) NOT NULL COMMENT '角色编号',
  `PERM_ID` BIGINT(64) NOT NULL COMMENT '权限编号',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_ROLE_PERM` (`ROLE_ID`, `PERM_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色权限关联表';

/*Table structure for table `router` */
DROP TABLE IF EXISTS `router`;
CREATE TABLE `router` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `NAME` VARCHAR(25) NOT NULL COMMENT '路由名称',
  `CODE` VARCHAR(100) DEFAULT NULL COMMENT '权限编码',
  `DEFAULT_LEVEL` TINYINT(1) NOT NULL DEFAULT 3 COMMENT '路由默认访问级别: 1-公开权限;2-公共权限;3-受控权限',
  `REMARK` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '备注',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='路由表';

/*Table structure for table `interface` */
DROP TABLE IF EXISTS `interface`;
CREATE TABLE `interface` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `CODE` VARCHAR(50) DEFAULT NULL COMMENT '权限编码',
  `URI` VARCHAR(100) NOT NULL COMMENT '路由链接',
  `METHOD` VARCHAR(50) DEFAULT NULL COMMENT '路由请求方式',
  `TYPE` TINYINT(1) NOT NULL COMMENT '接口类型',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_URL_METHOD` (`URI`, `METHOD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='接口表';

/*Table structure for table `dict` */
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `NAME` VARCHAR(50) NOT NULL COMMENT '字典名称',
  `KEY` VARCHAR(50) DEFAULT NULL COMMENT '字典KEY',
  `VALUE` TEXT DEFAULT NULL COMMENT '字典Value',
  `PID` BIGINT(64) DEFAULT NULL COMMENT '父编号',
  `TYPE` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '类型: 1-组;2-项',
  `SORT` INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `DESC` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '字典描述',
  `LOCKED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否允许编辑,删除',
  `UNIQUE` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'key是否唯一',
  `ENABLED` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典表';

/*Data for the table `dict` */
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (1, '系统参数', null, null, null, 1, 1, '系统参数', 1, 1, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (2, '路由白名单', null, null, null, 1, 2, '路由白名单', 1, 1, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3, '路由黑名单', null, null, null, 1, 3, '路由黑名单', 1, 1, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (4, '/error', 'routerWhiteList', '/error', 2, 2, 1, '错误处理', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (5, '/v2/api-docs', 'routerWhiteList', '/v2/api-docs', 2, 2, 2, 'swagger', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (6, '/configuration/ui', 'routerWhiteList', '/configuration/ui', 2, 2, 3, 'swagger', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (7, '/swagger-resources', 'routerWhiteList', '/swagger-resources', 2, 2, 4, 'swagger', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (8, '/configuration/security', 'routerWhiteList', '/configuration/security', 2, 2, 5, 'swagger', 0, 0, 1, 0, 1, 'SYSTEM', '2021-05-17 01:48:00', '2021-05-17', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (9, '/api/process', 'routerWhiteList', '/api/process', 2, 2, 6, '登录请求', 0, 0, 1, 0, 1, 'SYSTEM', '2021-06-23 16:48:00', '2021-06-23', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (10, '日历类型', null, null, null, 1, 4, '日历类型', 1, 1, 1, 0, 1, 'SYSTEM', '2022-04-12 19:48:00', '2022-04-12', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (11, '节假日', 'calendarType', '0', 9, 2, 1, '', 1, 0, 1, 0, 1, 'SYSTEM', '2022-04-12 19:48:00', '2022-04-12', '', '');
INSERT INTO dict (ID, NAME, `KEY`, VALUE, PID, TYPE, SORT, `DESC`, LOCKED, `UNIQUE`, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (12, '工作日', 'calendarType', '1', 9, 2, 2, '', 1, 0, 1, 0, 1, 'SYSTEM', '2022-04-12 19:48:00', '2022-04-12', '', '');

/*Table structure for table `log` */
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `LOG_TYPE` TINYINT(1) NOT NULL COMMENT '日志类型',
  `OPERATE_USER` VARCHAR(50) NOT NULL COMMENT '操作用户',
  `EVENT_TIME` VARCHAR(50) NOT NULL COMMENT '操作时间或异常发生时间',
  `DESCRIPTION` VARCHAR(50) NOT NULL COMMENT '操作描述',
  `OPERATE_METHOD` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '操作方法',
  `OPERATE_PARAM` TEXT DEFAULT NULL COMMENT '操作参数',
  `REQUEST_IP` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '请求IP',
  `REQUEST_URI` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '请求URI',
  `REQUEST_METHOD` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '请求Method',
  `ADDRESS` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '请求地址',
  `BROWSER` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '浏览器',
  `COST_TIME` INT(11) NOT NULL COMMENT '请求耗时',
  `EXCEPTION_DETAIL` TEXT DEFAULT NULL COMMENT '异常信息',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='日志表';

/*Table structure for table `file` */
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `FILENAME` VARCHAR(250) NOT NULL COMMENT '文件名',
  `ORIGINAL_FILENAME` VARCHAR(250) NOT NULL COMMENT '原文件名称',
  `SIZE` VARCHAR(50) NOT NULL COMMENT '文件大小',
  `FILE_PATH` VARCHAR(1500) NOT NULL DEFAULT '' COMMENT '文件路径',
  `DOWNLOAD_URL` VARCHAR(1500) NOT NULL DEFAULT '' COMMENT '下载链接',
  `MD5` VARCHAR(200) NOT NULL COMMENT '备注',
  `FILE_TYPE` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '文件类型',
  `STORAGE_TYPE` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '存储类型',
  `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文件表';

/*Table structure for table `file_relation` */
DROP TABLE IF EXISTS `file_relation`;
CREATE TABLE `file_relation` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `FILE_ID` BIGINT(64) NOT NULL COMMENT '文件编号',
  `BUSINESS_ID` BIGINT(64) NOT NULL COMMENT '业务编号',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_ROLE_PERM` (`FILE_ID`, `BUSINESS_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文件与业务对象关联表';

/*Table structure for table `calendar` */
DROP TABLE IF EXISTS `calendar`;
CREATE TABLE `calendar` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `DATE` VARCHAR(25) NOT NULL COMMENT '日期（yyyy-MM-dd）',
  `FLAG` VARCHAR(10) NOT NULL COMMENT '标识',
  `REMARK` VARCHAR(127) NOT NULL DEFAULT '' COMMENT '备注',
  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `KEY_DATE` (`DATE`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='日历表';

/*Table structure for table `user_pass_log` */
DROP TABLE IF EXISTS `user_pass_log`;
CREATE TABLE `user_pass_log` (
  `ID` BIGINT(64) UNSIGNED NOT NULL,
  `USER_ID` BIGINT(64) NOT NULL COMMENT '用户编号',
  `PASSWORD` VARCHAR(60) NOT NULL COMMENT '密码',
  `UPDATE_TIME` VARCHAR(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户密码更新日志表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
