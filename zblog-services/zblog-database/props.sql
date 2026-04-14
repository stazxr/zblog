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
  INDEX `idx_props_key`(`KEY`) USING BTREE,
  UNIQUE KEY `uk_idx_props` (`KEY`, `GROUP`)
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_props
-- ----------------------------
INSERT INTO `sys_props` VALUES ('zblog.db.DbDriver', 'com.mysql.cj.jdbc.Driver', 'zblog.service', '数据库驱动', 0);
INSERT INTO `sys_props` VALUES ('zblog.db.DbUrl', 'jdbc:mysql://127.0.0.1:3306/zblog?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai', 'zblog.service', '数据库链接', 0);
INSERT INTO `sys_props` VALUES ('zblog.db.DbUser', 'root', 'zblog.service', '数据库用户', 0);
INSERT INTO `sys_props` VALUES ('zblog.db.DbPass', 'root', 'zblog.service', '数据库密码', 0);
INSERT INTO `sys_props` VALUES ('zblog.security.PrivateKey', 'MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDUPu7F/BhTalpYTESI/jnT+TKyN5M7HrC/jIjRLBZOT1kFz9FcBaJiWgHMmcuCPxVoE64UNNMdx4FvX7lC12ROcincMqUujH+41pPFsS77nntI4HDUviqzk3MtfOovOp/XMX40MhX7twVprXMa/ElS/8AvsooD0nELEsk2Lpr+Qwgwn5jfNF0d+EHJhv2HuPfW8VOXWI7bp2m4US24Hciwt+jv+bLstljSwP9AiY/KV49Ud84PC/Nu4gW1pyLSn/ovG4RgRmhxEJR9Z0H6V8uSosK0XlFMhinyqMsmdxdbqhVTofC6Yi5FSkvYsH1xOvOzhD9ssNaoF0OUIqjhQJlxAgMBAAECggEASANCSHKMXmELXkIiTsjTHhTDGqy4i6qSFau9EBuBRfiuH8avJiXTPsODMMRNxFdbEAD9Y2W467WxOPSliwRByEv73/ZfDTgmbbSAVucTJdRTyBo+rjAHlP5GafykCHo/mWf1hggoZUtnzr9G+rT2u+6CaqyNH1bbfAJXusZ9WB8P46kLvlr7X22NfgJteGiG7uWF4f0EodIax9XMv+KAPQaMpTHa0oP17x+7xtJOn4djXVzpQynzlEYazrp9W1IIEJPvYQpbdkNi6hOUnLJjEps42miOCWkBnUpxQc5Ik6+YXXltYEN4Xh0E7LNnolCJVJlDgm6wIciKwdnpIhZT8QKBgQD0GWshFom9KOdLztByo0EIWnypF3hwLeplfGh9I93PnAWTfosBTJBNHEv1SkL3MEX9dWXFNsjtOztJOZMC4l/Tu7VTY2ejESM+P1upm3K5WfH+/AgWsyF//Q1dMerQOSf2Jq56DEnP34qN2Cv2t6udCMb/o3RwjAwU2x1++4Zb/wKBgQDel/Q7pcYwk7+3K5MWw5xeqTZlPmcol9UYMrElHa0RQ2ggKh2wbIn442JazId3SRZHoLavLoZ1QJKHvwXBd1OXSvdMyqd1GZLg1yM8FJRMsgJ3TUwL4WH1ReuTZGmHE9yoiOUnouUbMK6HsAQVvagSf2vIbJnmW36sXp0Nk2TKjwKBgHvA70lFLevS8wDCB3g3QF9F0PHBTnRBMxbkrezT5D6/MSyH+V1dPcN6VyAy2CSOOs23WTNVBSUQ5IvJPrk1n7Ou9M0kFoTbyWxjnsssXkuOSFwn1sn7Yz6KQt4+0ndiotnu3oJN/JYBFTO4pwFcOQtSSeGNMxlkRzPDqv6X8pRtAoGBALe/kGm8ywJGtTgrzFw6Vdb+sFybSuUDkXFMR1dwS/G4RzhmC+QbdTnz2rlBpYIe3zl5vdSW/3/DMjLEyaePLX3y8Hp/wAS2e70HW5q5EkLNn6OEN4aHIyop8fHWLhbHmpu1hhVWLvJnGWwBLR4VVa0PapYksFasqMD5yYPvbICZAoGBAIeMFMBZHy1RFpzRrUxhQYoxaXbuAnxdj2NTxXS4sTYYNo7CUMQz2pIjxUV5M5126C1ggF8tvIw0GSJcWV1MJC0k4WBdTK69Q2V5WOPMIXnatl8sBMzd8dPQ6eYY8DHuVveB1SNC5k9AAx/FXlWCOXSJ8kmkY34AhC9b7BirETQL', 'zblog.service', '默认私钥', 0);
INSERT INTO `sys_props` VALUES ('zblog.security.PublicKey', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1D7uxfwYU2paWExEiP450/kysjeTOx6wv4yI0SwWTk9ZBc/RXAWiYloBzJnLgj8VaBOuFDTTHceBb1+5QtdkTnIp3DKlLox/uNaTxbEu+557SOBw1L4qs5NzLXzqLzqf1zF+NDIV+7cFaa1zGvxJUv/AL7KKA9JxCxLJNi6a/kMIMJ+Y3zRdHfhByYb9h7j31vFTl1iO26dpuFEtuB3IsLfo7/my7LZY0sD/QImPylePVHfODwvzbuIFtaci0p/6LxuEYEZocRCUfWdB+lfLkqLCtF5RTIYp8qjLJncXW6oVU6HwumIuRUpL2LB9cTrzs4Q/bLDWqBdDlCKo4UCZcQIDAQAB', 'zblog.service', '默认公钥', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.host', 'smtp.qq.com', 'zblog.service', '邮箱服务器', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.port', '465', 'zblog.service', '邮箱服务器端口', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.username', 'zblog@qq.com', 'zblog.service', '邮箱用户', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.password', '', 'zblog.service', '邮箱授权码（非登录密码）', 0);
INSERT INTO `sys_props` VALUES ('zblog.email.from', 'Z-BLOG', 'zblog.service', '邮箱发件人（名称不规范可能导致邮件发送到对方的垃圾箱中）', 0);
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
INSERT INTO `sys_props` VALUES ('zblog.file.cos.fileAccessUrl', '', 'zblog.service', '腾讯云 COS 文件访问 URL 前缀', 0);
INSERT INTO `sys_props` VALUES ('zblog.file.cos.storagePathPrefix', '', 'zblog.service', '腾讯云 COS 文件存储路径前缀', 0);
INSERT INTO `sys_props` VALUES ('zblog.file.cos.accessKey', '', 'zblog.service', '腾讯云访问密钥 ID', 0);
INSERT INTO `sys_props` VALUES ('zblog.file.cos.secretKey', '', 'zblog.service', '腾讯云访问密钥 SecretKey', 0);
INSERT INTO `sys_props` VALUES ('zblog.file.cos.region', '', 'zblog.service', '腾讯云 COS 所在地域', 0);
INSERT INTO `sys_props` VALUES ('zblog.file.cos.bucketName', '', 'zblog.service', '腾讯云 COS 存储桶名称', 0);

SET FOREIGN_KEY_CHECKS = 1;
