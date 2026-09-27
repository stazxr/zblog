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
  `VALUE` varchar(2000) DEFAULT NULL COMMENT 'props value',
  `GROUP` varchar(30) NOT NULL COMMENT 'props 所属组',
  `REMARK` varchar(300) DEFAULT NULL COMMENT '备注',
  INDEX `idx_props_key`(`KEY`) USING BTREE,
  UNIQUE KEY `uk_idx_props` (`KEY`, `GROUP`)
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_props
-- ----------------------------
INSERT INTO `sys_props` VALUES ('zblog.security.PrivateKey', 'MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDUPu7F/BhTalpYTESI/jnT+TKyN5M7HrC/jIjRLBZOT1kFz9FcBaJiWgHMmcuCPxVoE64UNNMdx4FvX7lC12ROcincMqUujH+41pPFsS77nntI4HDUviqzk3MtfOovOp/XMX40MhX7twVprXMa/ElS/8AvsooD0nELEsk2Lpr+Qwgwn5jfNF0d+EHJhv2HuPfW8VOXWI7bp2m4US24Hciwt+jv+bLstljSwP9AiY/KV49Ud84PC/Nu4gW1pyLSn/ovG4RgRmhxEJR9Z0H6V8uSosK0XlFMhinyqMsmdxdbqhVTofC6Yi5FSkvYsH1xOvOzhD9ssNaoF0OUIqjhQJlxAgMBAAECggEASANCSHKMXmELXkIiTsjTHhTDGqy4i6qSFau9EBuBRfiuH8avJiXTPsODMMRNxFdbEAD9Y2W467WxOPSliwRByEv73/ZfDTgmbbSAVucTJdRTyBo+rjAHlP5GafykCHo/mWf1hggoZUtnzr9G+rT2u+6CaqyNH1bbfAJXusZ9WB8P46kLvlr7X22NfgJteGiG7uWF4f0EodIax9XMv+KAPQaMpTHa0oP17x+7xtJOn4djXVzpQynzlEYazrp9W1IIEJPvYQpbdkNi6hOUnLJjEps42miOCWkBnUpxQc5Ik6+YXXltYEN4Xh0E7LNnolCJVJlDgm6wIciKwdnpIhZT8QKBgQD0GWshFom9KOdLztByo0EIWnypF3hwLeplfGh9I93PnAWTfosBTJBNHEv1SkL3MEX9dWXFNsjtOztJOZMC4l/Tu7VTY2ejESM+P1upm3K5WfH+/AgWsyF//Q1dMerQOSf2Jq56DEnP34qN2Cv2t6udCMb/o3RwjAwU2x1++4Zb/wKBgQDel/Q7pcYwk7+3K5MWw5xeqTZlPmcol9UYMrElHa0RQ2ggKh2wbIn442JazId3SRZHoLavLoZ1QJKHvwXBd1OXSvdMyqd1GZLg1yM8FJRMsgJ3TUwL4WH1ReuTZGmHE9yoiOUnouUbMK6HsAQVvagSf2vIbJnmW36sXp0Nk2TKjwKBgHvA70lFLevS8wDCB3g3QF9F0PHBTnRBMxbkrezT5D6/MSyH+V1dPcN6VyAy2CSOOs23WTNVBSUQ5IvJPrk1n7Ou9M0kFoTbyWxjnsssXkuOSFwn1sn7Yz6KQt4+0ndiotnu3oJN/JYBFTO4pwFcOQtSSeGNMxlkRzPDqv6X8pRtAoGBALe/kGm8ywJGtTgrzFw6Vdb+sFybSuUDkXFMR1dwS/G4RzhmC+QbdTnz2rlBpYIe3zl5vdSW/3/DMjLEyaePLX3y8Hp/wAS2e70HW5q5EkLNn6OEN4aHIyop8fHWLhbHmpu1hhVWLvJnGWwBLR4VVa0PapYksFasqMD5yYPvbICZAoGBAIeMFMBZHy1RFpzRrUxhQYoxaXbuAnxdj2NTxXS4sTYYNo7CUMQz2pIjxUV5M5126C1ggF8tvIw0GSJcWV1MJC0k4WBdTK69Q2V5WOPMIXnatl8sBMzd8dPQ6eYY8DHuVveB1SNC5k9AAx/FXlWCOXSJ8kmkY34AhC9b7BirETQL', 'zblog.service', '默认私钥');
INSERT INTO `sys_props` VALUES ('zblog.security.PublicKey', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1D7uxfwYU2paWExEiP450/kysjeTOx6wv4yI0SwWTk9ZBc/RXAWiYloBzJnLgj8VaBOuFDTTHceBb1+5QtdkTnIp3DKlLox/uNaTxbEu+557SOBw1L4qs5NzLXzqLzqf1zF+NDIV+7cFaa1zGvxJUv/AL7KKA9JxCxLJNi6a/kMIMJ+Y3zRdHfhByYb9h7j31vFTl1iO26dpuFEtuB3IsLfo7/my7LZY0sD/QImPylePVHfODwvzbuIFtaci0p/6LxuEYEZocRCUfWdB+lfLkqLCtF5RTIYp8qjLJncXW6oVU6HwumIuRUpL2LB9cTrzs4Q/bLDWqBdDlCKo4UCZcQIDAQAB', 'zblog.service', '默认公钥');
INSERT INTO `sys_props` VALUES ('zblog.multipart.maxFileSize', '200MB', 'zblog.service', '限制单个文件上传大小');
INSERT INTO `sys_props` VALUES ('zblog.multipart.maxRequestSize', '200MB', 'zblog.service', '整个 HTTP multipart 请求大小，如多文件，一般 max-request-size >= max-file-size');
INSERT INTO `sys_props` VALUES ('zblog.multipart.fileSizeThreshold', '1MB', 'zblog.service', '上传文件时，没超过这个值，写入内存，超过则写入磁盘');
INSERT INTO `sys_props` VALUES ('zblog.multipart.location', '/tmp', 'zblog.service', '临时文件的存放目录，linux场景下某些系统 /tmp 目录很小，需要重新客户化这个参数');
INSERT INTO `sys_props` VALUES ('zblog.database.driver', 'com.mysql.cj.jdbc.Driver', 'zblog.service', '数据库驱动');
INSERT INTO `sys_props` VALUES ('zblog.database.url', 'jdbc:mysql://127.0.0.1:3306/zblog?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&connectTimeout=5000&socketTimeout=30000', 'zblog.service', '数据库链接');
INSERT INTO `sys_props` VALUES ('zblog.database.username', 'root', 'zblog.service', '数据库用户');
INSERT INTO `sys_props` VALUES ('zblog.database.password', 'root', 'zblog.service', '数据库密码');
INSERT INTO `sys_props` VALUES ('zblog.database.pool.initialSize', '5', 'zblog.service', '数据库连接池的初始连接数，建议 InitialSize ≥ MinIdle');
INSERT INTO `sys_props` VALUES ('zblog.database.pool.minIdle', '5', 'zblog.service', '数据库连接池的最小连接数');
INSERT INTO `sys_props` VALUES ('zblog.database.pool.maxActive', '10', 'zblog.service', '数据库连接池的最大连接数');
INSERT INTO `sys_props` VALUES ('zblog.database.pool.maxWait', '10000', 'zblog.service', '连接池的最大等待时间（毫秒），当连接池已无可用连接时，线程最多等待该时间，超时后仍未获取到连接，则抛出获取连接超时异常');
INSERT INTO `sys_props` VALUES ('zblog.database.slowSqlMills', '3000', 'zblog.service', '慢SQL阙值（毫秒），根据项目情况按需调整');
INSERT INTO `sys_props` VALUES ('zblog.database.druid.page.enabled', 'true', 'zblog.service', '是否启用 Druid Web 监控页面，生产环境建议仅在确有监控需求时开启，并配合 Nginx 做访问控制');
INSERT INTO `sys_props` VALUES ('zblog.database.druid.page.loginUsername', 'admin', 'zblog.service', 'Druid 监控页面登录用户名');
INSERT INTO `sys_props` VALUES ('zblog.database.druid.page.loginPassword', 'admin', 'zblog.service', 'Druid 监控页面登录密码');
INSERT INTO `sys_props` VALUES ('zblog.redis.database', '0', 'zblog.service', '指定 Redis 使用的数据库索引，Redis 默认有 16 个数据库，索引从 0 到 15');
INSERT INTO `sys_props` VALUES ('zblog.redis.host', '127.0.0.1', 'zblog.service', 'Redis 服务器地址');
INSERT INTO `sys_props` VALUES ('zblog.redis.port', '6379', 'zblog.service', 'Redis 服务器的端口号');
INSERT INTO `sys_props` VALUES ('zblog.redis.password', 'requiredPwd', 'zblog.service', 'Redis 服务器的密码');
INSERT INTO `sys_props` VALUES ('zblog.redis.connect.timeout', '3000ms', 'zblog.service', '建立 TCP 连接超时时间，单位为毫秒，用于定义客户端连接到 Redis 服务器的最大等待时间');
INSERT INTO `sys_props` VALUES ('zblog.redis.read.timeout', '5000ms', 'zblog.service', 'Redis 命令执行等待时间');
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.minIdle', '5', 'zblog.service', '连接池中保持的最小空闲连接数');
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.maxIdle', '10', 'zblog.service', '连接池中允许的最大空闲连接数');
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.maxActive', '50', 'zblog.service', '连接池中允许的最大活动连接数，如果活动连接数超过此值，将会有新的连接请求被阻塞或抛出异常');
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.maxWait', '3000ms', 'zblog.service', '从连接池中获取连接时的最大等待时间，单位为毫秒。如果超过此时间仍未获取到连接，将会抛出异常');
INSERT INTO `sys_props` VALUES ('zblog.redis.lettuce.pool.timeBetweenEvictionRuns', '60000ms', 'zblog.service', '连接池中连接的清理周期，单位为毫秒。用于定义多长时间执行一次空闲连接的检查和清理，以保持连接池的健康');
INSERT INTO `sys_props` VALUES ('zblog.email.host', 'smtp.qq.com', 'zblog.service', '邮箱服务器');
INSERT INTO `sys_props` VALUES ('zblog.email.port', '465', 'zblog.service', '邮箱服务器端口');
INSERT INTO `sys_props` VALUES ('zblog.email.username', 'zblog@qq.com', 'zblog.service', '邮箱用户');
INSERT INTO `sys_props` VALUES ('zblog.email.password', '', 'zblog.service', '邮箱授权码（非登录密码）');
INSERT INTO `sys_props` VALUES ('zblog.email.from', 'Z-BLOG', 'zblog.service', '邮箱发件人（名称不规范可能导致邮件发送到对方的垃圾箱中）');
INSERT INTO `sys_props` VALUES ('zblog.bas.mail.from.website.name', 'Z-BLOG', 'zblog.service', '发件人站点名称');
INSERT INTO `sys_props` VALUES ('zblog.bas.mail.from.website.url', 'http://localhost:31945', 'zblog.service', '发件人站点地址');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.coreSize', '5', 'zblog.service', '核心线程数');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.maxSize', '10', 'zblog.service', '最大线程数');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.queueSize', '50', 'zblog.service', '任务队列容量');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.keepAliveSeconds', '60', 'zblog.service', '非核心线程空闲存活时间（秒），超过该时间未执行任务将被回收');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.threadNamePrefix', 'zblog-', 'zblog.service', '线程名称前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.shutdown.waitForTasksToComplete', 'true', 'zblog.service', '应用关闭时是否等待线程池中的任务执行完成，避免异步任务被直接中断');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.shutdown.awaitTerminationSeconds', '30', 'zblog.service', '应用关闭时最多等待任务完成的时间（秒），超时后继续关闭');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.monitor.enabled', 'true', 'zblog.service', '是否开启线程池运行状态监控日志');
INSERT INTO `sys_props` VALUES ('zblog.bas.thread.pool.monitor.interval', '10000', 'zblog.service', '线程池监控日志打印间隔，单位毫秒');
INSERT INTO `sys_props` VALUES ('zblog.bas.cache.type', 'memory', 'zblog.service', '全局缓存类型，支持 memory 或 redis');
INSERT INTO `sys_props` VALUES ('zblog.bas.i18n.cacheTtl', '300000', 'zblog.service', '国际化消息缓存刷新周期，单位毫秒');
INSERT INTO `sys_props` VALUES ('zblog.bas.context.deploy.area', '', 'zblog.service', '部署地域，JVM 参数优先，否则使用数据库配置');
INSERT INTO `sys_props` VALUES ('zblog.bas.context.deploy.center', '', 'zblog.service', '部署机房/可用区，JVM 参数优先，否则使用数据库配置');
INSERT INTO `sys_props` VALUES ('zblog.bas.context.deploy.unit', '0', 'zblog.service', '部署单元，JVM 参数优先，否则使用数据库配置，默认 0');
INSERT INTO `sys_props` VALUES ('zblog.bas.context.deploy.ip', '', 'zblog.service', '部署 IP，JVM 参数优先，否则自动获取本机 IP');
INSERT INTO `sys_props` VALUES ('zblog.bas.sequence.datacenter.id', '0', 'zblog.service', 'Snowflake 数据中心编号');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.storage.type', '1', 'zblog.service', '文件存储类型：1-本地；2-阿里云 OSS；3-七牛云 Kodo；4-腾讯云 COS');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.local.enabled', 'true', 'zblog.service', '是否启用本地文件存储');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.local.fileAccessUrl', 'http://localhost:${server.port}/file/', 'zblog.service', '本地文件访问 URL 前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.local.storagePathPrefix', '/appuser/upload/common', 'zblog.service', '本地文件存储路径前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.oss.enabled', 'false', 'zblog.service', '是否启用阿里云 OSS 对象存储');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.oss.fileAccessUrl', '', 'zblog.service', '阿里云 OSS 文件访问 URL 前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.oss.storagePathPrefix', '', 'zblog.service', '阿里云 OSS 文件存储路径前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.oss.accessKey', '', 'zblog.service', '阿里云 AccessKey ID');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.oss.secretKey', '', 'zblog.service', '阿里云 AccessKey Secret');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.oss.endpoint', '', 'zblog.service', '阿里云 OSS Endpoint');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.oss.bucketName', '', 'zblog.service', '阿里云 OSS 存储桶名称');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.kodo.enabled', 'false', 'zblog.service', '是否启用七牛云 Kodo 对象存储');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.kodo.fileAccessUrl', '', 'zblog.service', '七牛云 Kodo 文件访问 URL');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.kodo.storagePathPrefix', '', 'zblog.service', '七牛云 Kodo 文件存储路径前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.kodo.accessKey', '', 'zblog.service', '七牛云 AccessKey（AK）');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.kodo.secretKey', '', 'zblog.service', '七牛云 SecretKey（SK）');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.kodo.zone', '', 'zblog.service', '七牛云存储区域');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.kodo.zoneName', '', 'zblog.service', '七牛云存储空间名称');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.cos.enabled', 'false', 'zblog.service', '是否启用腾讯云 COS 对象存储');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.cos.fileAccessUrl', '', 'zblog.service', '腾讯云 COS 文件访问 URL 前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.cos.storagePathPrefix', '', 'zblog.service', '腾讯云 COS 文件存储路径前缀');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.cos.accessKey', '', 'zblog.service', '腾讯云访问密钥 ID');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.cos.secretKey', '', 'zblog.service', '腾讯云 COS SecretKey');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.cos.region', '', 'zblog.service', '腾讯云 COS 所在地域');
INSERT INTO `sys_props` VALUES ('zblog.bas.file.cos.bucketName', '', 'zblog.service', '腾讯云 COS 存储桶名称');
INSERT INTO `sys_props` VALUES ('zblog.bas.cors.allowedOriginPatterns', '*', 'zblog.service', 'CORS允许的跨域来源，多个来源使用逗号分隔');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.password.limitedDay', '90', 'zblog.service', '密码有效天数');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.password.historyCount', '-1', 'zblog.service', '新密码不能与最近X次密码重复，-1表示关闭校验');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.enableAdditionalChecks', 'false', 'zblog.service', '是否启用额外登录认证校验');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.lock.maxFailCount', '0', 'zblog.service', '允许登录失败最大次数，0表示不锁定');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.lock.duration', 'PT5M', 'zblog.service', '账号锁定时长，ISO-8601 Duration格式，例如PT5M表示5分钟');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.accessTokenTtl', '1800', 'zblog.service', '访问令牌有效时间（秒）');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.refreshTokenTtl', '604800', 'zblog.service', '刷新令牌有效时间（秒）');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.allowedRenewToken', 'true', 'zblog.service', '是否允许访问令牌续签');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.checkIpChange', 'true', 'zblog.service', '是否检查访问令牌请求IP变化');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cert.alias', 'zblog', 'zblog.service', 'JWT签名证书别名');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cert.keyPassword', '123456', 'zblog.service', 'JWT签名证书密钥密码');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cert.location', 'zblog.jks', 'zblog.service', 'JWT签名证书文件路径');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.claims.issuer', 'zblog', 'zblog.service', 'JWT签发者');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.claims.audience', 'zblog', 'zblog.service', 'JWT接收方');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cookie.domain', 'localhost', 'zblog.service', 'JWT Cookie域名');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cookie.secure', 'false', 'zblog.service', '是否仅允许HTTPS传输JWT Cookie');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cookie.httpOnly', 'true', 'zblog.service', '是否禁止JavaScript访问JWT Cookie');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cookie.sameSite', 'Lax', 'zblog.service', 'JWT Cookie SameSite策略：Strict、Lax、None');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cookie.path', '/api', 'zblog.service', 'Access Token Cookie生效路径');
INSERT INTO `sys_props` VALUES ('zblog.bas.security.jwt.cookie.refreshPath', '/api/auth/refresh', 'zblog.service', 'Refresh Token Cookie生效路径');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.enable', 'true', 'zblog.service', '是否启用接口文档');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.title', 'Z-BLOG 接口文档', 'zblog.service', '接口文档标题');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.description', 'Z-BLOG 是一款基于 MIT 协议的前后端分离博客框架，同时也可用作为前后端分离脚手架，快速进行项目搭建，仓库链接：https://github.com/stazxr/zblog', 'zblog.service', '接口文档描述');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.serverUrl', 'http://localhost:8081', 'zblog.service', '接口服务地址');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.version', '5.0 & P1.0', 'zblog.service', '接口文档版本');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.contact.name', '孙涛', 'zblog.service', '接口文档联系人姓名');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.contact.email', 'stazxr@qq.com', 'zblog.service', '接口文档联系人邮箱');
INSERT INTO `sys_props` VALUES ('zblog.bas.swagger.contact.url', 'https://github.com/stazxr/zblog', 'zblog.service', '接口文档联系人主页');
INSERT INTO `sys_props` VALUES ('zblog.bas.aopLog.enabled', 'true', 'zblog.service', '是否启用切面日志');
INSERT INTO `sys_props` VALUES ('zblog.bas.aopLog.async', 'true', 'zblog.service', '是否开启异步日志写入');
INSERT INTO `sys_props` VALUES ('zblog.bas.aopLog.recordParam', 'true', 'zblog.service', '是否记录接口请求参数');
INSERT INTO `sys_props` VALUES ('zblog.bas.aopLog.recordResult', 'true', 'zblog.service', '是否记录接口返回值');
INSERT INTO `sys_props` VALUES ('zblog.bas.aopLog.maxParamLength', '5000', 'zblog.service', '接口请求参数最大记录长度，超过部分将被截断');
INSERT INTO `sys_props` VALUES ('zblog.bas.aopLog.maxResultLength', '5000', 'zblog.service', '接口返回值最大记录长度，超过部分将被截断');
INSERT INTO `sys_props` VALUES ('zblog.bas.websocket.allowedOriginPatterns', '*', 'zblog.service', 'WebSocket允许的跨域来源，多个来源使用逗号分隔');

INSERT INTO `sys_props` VALUES ('zblog.audit.tms.enabled', 'false', 'zblog.service', '是否启用腾讯云内容审核');
INSERT INTO `sys_props` VALUES ('zblog.audit.tms.accessKey', '', 'zblog.service', '腾讯云 SecretId（访问密钥 ID）');
INSERT INTO `sys_props` VALUES ('zblog.audit.tms.secretKey', '', 'zblog.service', '腾讯云 SecretKey（访问密钥）');
INSERT INTO `sys_props` VALUES ('zblog.audit.tms.region', 'ap-beijing', 'zblog.service', '腾讯云 TMS 所在地域');
INSERT INTO `sys_props` VALUES ('zblog.audit.tms.bizType', '', 'zblog.service', '腾讯云 TMS 接口地址');
INSERT INTO `sys_props` VALUES ('zblog.audit.tms.endpoint', 'tms.tencentcloudapi.com', 'zblog.service', '识别策略编号（默认）');

SET FOREIGN_KEY_CHECKS = 1;
