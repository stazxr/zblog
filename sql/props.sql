/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : props

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 23/07/2024 00:11:32
*/

CREATE DATABASE IF NOT EXISTS `props` DEFAULT CHARACTER SET utf8mb4;

USE `props`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_props
-- ----------------------------
DROP TABLE IF EXISTS `sys_props`;
CREATE TABLE `sys_props`  (
  `KEY` VARCHAR(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'props key',
  `VALUE` VARCHAR(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'props value',
  `GROUP` VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'props 所属组',
  `REMARK` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
  INDEX `KEY_INDEX`(`KEY`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_props
-- ----------------------------
INSERT INTO `sys_props` VALUES ('DbDriver', 'com.mysql.jdbc.Driver', 'zblog.service', '', 0);
INSERT INTO `sys_props` VALUES ('DbUrl', 'jdbc:mysql://127.0.0.1:3306/zblog?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai', 'zblog.service', '', 0);
INSERT INTO `sys_props` VALUES ('DbUser', 'root', 'zblog.service', '', 0);
INSERT INTO `sys_props` VALUES ('DbPass', 'root', 'zblog.service', '', 0);

SET FOREIGN_KEY_CHECKS = 1;
