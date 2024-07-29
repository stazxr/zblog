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
  `VALUE` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'props value',
  `GROUP` VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'props 所属组',
  `REMARK` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
  INDEX `KEY_INDEX`(`KEY`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_props
-- ----------------------------
INSERT INTO `sys_props` VALUES ('DbDriver', 'com.mysql.jdbc.Driver', 'zblog.service', '数据库驱动', 0);
INSERT INTO `sys_props` VALUES ('DbUrl', 'jdbc:mysql://127.0.0.1:3306/zblog?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai', 'zblog.service', '数据库链接', 0);
INSERT INTO `sys_props` VALUES ('DbUser', 'root', 'zblog.service', '数据库用户', 0);
INSERT INTO `sys_props` VALUES ('DbPass', 'root', 'zblog.service', '数据库密码', 0);
INSERT INTO `sys_props` VALUES ('PrivateKey', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIjQiAeF7xn0yKbtJe/iwuita3jRhKiK8i7cr+tlYe0no/39nG8DW78vM6hqKcae5cRwLQPdFbiqfYVRnla05EjkL0X32/bNYctRhQuxJ8vfK+czfNDV1RxVEApZ90dNW/PhBRp3bV3cZlCaBLgKwHSIL9c9+B2ttKJVgj0ji1E3AgMBAAECgYEAhmp6ewm3dATu7jGoSBq96+QV5snST9TTQ/9GRzuADoQXtn7oppjiFt/FHP3QtXd/vpOHZnhb0uaOE+GwcveeIB9a6qAq59VYKKY3OaG6farGp/j7YXKY5T3KXM+Sio03t87dm8DUM1RD6vG74kpv43FAq6wDRMh1TuLrakXiSiECQQC/X7H31UVukeRZLWQ/8CSqWsAc3CCQJHlWCf1HSqJ9YZ1jKfILN5JUMecazpMHiPqyQb+eTNhtIymy8CuRTFdVAkEAtwQt5xlhKuchIs4zMvx2YJUW/tKVmBO/nKlayTv1XXJ4LlkzBUxYf+PhJh/Q24M/wFEOfgmDEg7PWzvd0NsuWwJBAKAvMYAIe/15EHG0lN0P3SDapUOC3Z7JaOajpeUui6P3OQ3v5jGNamYe/xppHpiIB499iS6vk9iGPjkQ3ubXx6ECQFoychkCNhQ54ufPbgZDzGefMr0VtepEWVvxTy/8H9oVL0vQZEfJL5igC3eJmJqG3eV/TUAqmfFNYT5ai+CCFR8CQCXAh3WrakRNUdnlGhIpzopmsD+SFeAOD8mpIsVfTWJgoFvWIBAicSZ9dm6uP8DwwXfEjeaPnZc5bq7/cV/T7Ww=', 'zblog.service', '默认私钥', 0);
INSERT INTO `sys_props` VALUES ('PublicKey', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI0IgHhe8Z9Mim7SXv4sLorWt40YSoivIu3K/rZWHtJ6P9/ZxvA1u/LzOoainGnuXEcC0D3RW4qn2FUZ5WtORI5C9F99v2zWHLUYULsSfL3yvnM3zQ1dUcVRAKWfdHTVvz4QUad21d3GZQmgS4CsB0iC/XPfgdrbSiVYI9I4tRNwIDAQAB', 'zblog.service', '默认公钥', 0);

SET FOREIGN_KEY_CHECKS = 1;
