SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE /*!32312 IF EXISTS*/ `props`;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `props` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `props`;

-- ----------------------------
-- Table structure for sys_props
-- ----------------------------
DROP TABLE IF EXISTS `sys_props`;
CREATE TABLE `sys_props`  (
  `KEY` varchar(150) NOT NULL COMMENT 'props key',
  `VALUE` varchar(2000) NOT NULL COMMENT 'props value',
  `GROUP` varchar(30) NOT NULL COMMENT 'props 所属组',
  `REMARK` varchar(200) DEFAULT NULL COMMENT '备注',
  `DELETED` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
  INDEX `KEY_INDEX`(`KEY`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_props
-- ----------------------------
INSERT INTO `sys_props` VALUES ('zblog.db.DbDriver', 'com.mysql.cj.jdbc.Driver', 'zblog.service', '数据库驱动', 0);
INSERT INTO `sys_props` VALUES ('zblog.db.DbUrl', 'jdbc:mysql://127.0.0.1:3306/zblog?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai', 'zblog.service', '数据库链接', 0);
INSERT INTO `sys_props` VALUES ('zblog.db.DbUser', 'root', 'zblog.service', '数据库用户', 0);
INSERT INTO `sys_props` VALUES ('zblog.db.DbPass', 'root', 'zblog.service', '数据库密码', 0);
INSERT INTO `sys_props` VALUES ('zblog.globalKey.PrivateKey', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIjQiAeF7xn0yKbtJe/iwuita3jRhKiK8i7cr+tlYe0no/39nG8DW78vM6hqKcae5cRwLQPdFbiqfYVRnla05EjkL0X32/bNYctRhQuxJ8vfK+czfNDV1RxVEApZ90dNW/PhBRp3bV3cZlCaBLgKwHSIL9c9+B2ttKJVgj0ji1E3AgMBAAECgYEAhmp6ewm3dATu7jGoSBq96+QV5snST9TTQ/9GRzuADoQXtn7oppjiFt/FHP3QtXd/vpOHZnhb0uaOE+GwcveeIB9a6qAq59VYKKY3OaG6farGp/j7YXKY5T3KXM+Sio03t87dm8DUM1RD6vG74kpv43FAq6wDRMh1TuLrakXiSiECQQC/X7H31UVukeRZLWQ/8CSqWsAc3CCQJHlWCf1HSqJ9YZ1jKfILN5JUMecazpMHiPqyQb+eTNhtIymy8CuRTFdVAkEAtwQt5xlhKuchIs4zMvx2YJUW/tKVmBO/nKlayTv1XXJ4LlkzBUxYf+PhJh/Q24M/wFEOfgmDEg7PWzvd0NsuWwJBAKAvMYAIe/15EHG0lN0P3SDapUOC3Z7JaOajpeUui6P3OQ3v5jGNamYe/xppHpiIB499iS6vk9iGPjkQ3ubXx6ECQFoychkCNhQ54ufPbgZDzGefMr0VtepEWVvxTy/8H9oVL0vQZEfJL5igC3eJmJqG3eV/TUAqmfFNYT5ai+CCFR8CQCXAh3WrakRNUdnlGhIpzopmsD+SFeAOD8mpIsVfTWJgoFvWIBAicSZ9dm6uP8DwwXfEjeaPnZc5bq7/cV/T7Ww=', 'zblog.service', '默认私钥', 0);
INSERT INTO `sys_props` VALUES ('zblog.globalKey.PublicKey', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI0IgHhe8Z9Mim7SXv4sLorWt40YSoivIu3K/rZWHtJ6P9/ZxvA1u/LzOoainGnuXEcC0D3RW4qn2FUZ5WtORI5C9F99v2zWHLUYULsSfL3yvnM3zQ1dUcVRAKWfdHTVvz4QUad21d3GZQmgS4CsB0iC/XPfgdrbSiVYI9I4tRNwIDAQAB', 'zblog.service', '默认公钥', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.host', 'smtp.qq.com', 'zblog.service', '邮箱服务器', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.port', '25', 'zblog.service', '邮箱服务器端口', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.username', 'zblog@qq.com', 'zblog.service', '邮箱用户', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.password', '', 'zblog.service', '邮箱授权码（非登录密码）', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.subject', 'ZBLOG', 'zblog.service', '邮箱发送主体（名称不规范可能导致邮件发送到对方的垃圾箱中）', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.database', '0', 'zblog.service', '指定 Redis 使用的数据库索引，Redis 默认有 16 个数据库，索引从 0 到 15', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.host', '127.0.0.1', 'zblog.service', 'Redis 服务器地址', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.port', '6379', 'zblog.service', 'Redis 服务器的端口号', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.password', 'redisPwd', 'zblog.service', 'Redis 服务器的密码', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.connect.timeout', '60000ms', 'zblog.service', '连接超时时间，单位为毫秒，用于定义客户端连接到 Redis 服务器的最大等待时间', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.minIdle', '2', 'zblog.service', '连接池中保持的最小空闲连接数', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.maxIdle', '10', 'zblog.service', '连接池中允许的最大空闲连接数', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.maxActive', '10', 'zblog.service', '连接池中允许的最大活动连接数，如果活动连接数超过此值，将会有新的连接请求被阻塞或抛出异常', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.maxWait', '30000ms', 'zblog.service', '从连接池中获取连接时的最大等待时间，单位为毫秒。如果超过此时间仍未获取到连接，将会抛出异常', 0);
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.timeBetweenEvictionRuns', '-1ms', 'zblog.service', '连接池中连接的清理周期，单位为毫秒。用于定义多长时间执行一次空闲连接的检查和清理，以保持连接池的健康', 0);

SET FOREIGN_KEY_CHECKS = 1;
