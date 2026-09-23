/************************ 以下暂未归档 *************************/
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3541113268745535488, 3539398025698869248, '说说管理', 1, null, 3, null, null, 'talk', 'talk', 53, null, 0, 0, 1, 0, 1, 'admin', '2022-12-12 14:39:53', '2022-12-12', 'admin', '2022-12-12 15:08:11');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3541113561432457216, 3541113268745535488, '发布说说', 2, 'addOrEditTalk', 3, 'AddOrEditTalk', 'admin/web/talk/addOrEditTalk', 'add', 'add-talk', 520, 0, 0, 0, 1, 0, 1, 'admin', '2022-12-12 14:41:03', '2022-12-12', 'admin', '2022-12-12 14:53:25');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3541113767582498816, 3541113268745535488, '说说列表', 2, 'queryTalkListByPage', 3, 'Talk', 'admin/web/talk/index', 'index', 'talk-list', 521, 0, 0, 0, 1, 0, 1, 'admin', '2022-12-12 14:41:52', '2022-12-12', 'admin', '2022-12-12 14:56:24');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3541260016868655104, 3541113767582498816, '查询说说详情', 3, 'queryTalkDetail', 3, null, null, null, '', 5210, null, null, null, 1, 1, 1, 'admin', '2022-12-13 00:23:01', '2022-12-13', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3541260107939577856, 3541113767582498816, '删除说说', 3, 'deleteTalk', 3, null, null, null, '', 5211, null, null, null, 1, 0, 1, 'admin', '2022-12-13 00:23:23', '2022-12-13', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3541838012717465600, 3542255832743804928, '相册列表', 2, 'pageAlbumList', 3, 'Album', 'admin/web/album/index', 'list', 'album', 541, 1, 0, 0, 1, 0, 1, 'admin', '2022-12-14 14:39:46', '2022-12-14', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3542255832743804928, 3539398025698869248, '相册管理', 1, null, 3, null, null, 'album', 'album-manage', 54, null, 0, 0, 1, 0, 1, 'admin', '2022-12-15 18:20:02', '2022-12-15', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3542329017187172352, 3542255832743804928, '照片回收站', 2, 'pageDeletePhotoList', 3, 'AlbumRecycle', 'admin/web/album/recycle', 'recycle', 'album-delete', 543, 1, 1, 0, 1, 0, 1, 'admin', '2022-12-15 23:10:50', '2022-12-15', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3542330282931650560, 3542255832743804928, '照片管理', 2, 'pagePhotoList', 2, 'AlbumPhoto', 'admin/web/album/photo', 'photo', 'album-photo', 542, 0, 1, 0, 1, 0, 1, 'admin', '2022-12-15 23:15:52', '2022-12-15', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3542545336939577344, 3541838012717465600, '查询相册详情', 3, 'queryAlbumDetail', 2, null, null, null, '', 5411, null, null, null, 1, 1, 1, 'admin', '2022-12-16 13:30:25', '2022-12-16', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3542545467374043136, 3541838012717465600, '新增或编辑相册', 3, 'addOrEditAlbum', 3, null, null, null, '', 5412, null, null, null, 1, 0, 1, 'admin', '2022-12-16 13:30:56', '2022-12-16', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3542545624714969088, 3541838012717465600, '删除相册', 3, 'deleteAlbum', 3, null, null, null, '', 5413, null, null, null, 1, 0, 1, 'admin', '2022-12-16 13:31:34', '2022-12-16', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3569517455757807616, 3542330282931650560, '上传相册照片', 3, 'saveAlbumPhoto', 3, null, null, null, '', 5420, null, null, null, 1, 0, 1, 'suntao', '2023-02-28 23:47:59', '2023-02-28', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3569731791310823424, 3542330282931650560, '移动相册照片', 3, 'moveAlbumPhoto', 3, null, null, null, '', 5422, null, null, null, 1, 0, 1, 'suntao', '2023-03-01 13:59:41', '2023-03-01', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3569731943798939648, 3542330282931650560, '删除相册照片', 3, 'deleteAlbumPhoto', 3, null, null, null, '', 5421, null, null, null, 1, 0, 1, 'suntao', '2023-03-01 14:00:17', '2023-03-01', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3570406762722893824, 3542329017187172352, '永久删除相册照片', 3, 'deleteAlbumPhotoForever', 3, null, null, null, '', 5431, null, null, null, 1, 0, 1, 'suntao', '2023-03-03 10:41:47', '2023-03-03', '', '');
INSERT INTO permission (ID, PID, PERM_NAME, PERM_TYPE, PERM_CODE, PERM_LEVEL, COMPONENT_NAME, COMPONENT_PATH, ROUTER_PATH, ICON, SORT, CACHE, HIDDEN, ENABLED, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3570406875776163840, 3542329017187172352, '恢复相册照片', 3, 'recoverAlbumPhoto', 3, null, null, null, '', 5432, null, null, null, 1, 0, 1, 'suntao', '2023-03-03 10:42:14', '2023-03-03', '', '');


/*Table structure for table `article_content_draft_record` */
DROP TABLE IF EXISTS `article_content_draft_record`;
CREATE TABLE `article_content_draft_record` (
                                                `ID` BIGINT(64) UNSIGNED NOT NULL,
                                                `ARTICLE_ID` BIGINT(64) NOT NULL COMMENT '文章编号',
                                                `REMARK` VARCHAR(250) NOT NULL DEFAULT '' COMMENT '文章概要',
                                                `COUNT` VARCHAR(200) DEFAULT NULL COMMENT '文章字数',
                                                `CONTENT` TEXT NOT NULL COMMENT '文章内容: 65535 / 16,777,215',
                                                `SAVE_TIME` VARCHAR(50) NOT NULL COMMENT '自动保存时间',
                                                PRIMARY KEY (`ID`) USING BTREE,
                                                KEY `INDEX_KEY_ARTICLE_ID` (`ARTICLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文章内容草稿记录表';

CREATE TABLE article_content_draft_record (
                                              `ID` BIGINT UNSIGNED NOT NULL,
                                              `ARTICLE_ID` BIGINT NOT NULL COMMENT '文章编号',
                                              `REMARK` VARCHAR(250) NOT NULL DEFAULT '' COMMENT '文章概要',
                                              `WORD_COUNT` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章字数',
                                              `CONTENT` MEDIUMTEXT NOT NULL COMMENT '文章内容',
                                              `SAVE_TIME` DATETIME NOT NULL COMMENT '自动保存时间',
                                              PRIMARY KEY (ID),
                                              KEY idx_article_id (ARTICLE_ID),
                                              KEY idx_article_save_time (ARTICLE_ID, SAVE_TIME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章内容草稿记录表';

/*Table structure for table `article_auto_publish_timing` */
DROP TABLE IF EXISTS `article_auto_publish_timing`;
CREATE TABLE `article_auto_publish_timing` (
                                               `ID` BIGINT(64) UNSIGNED NOT NULL,
                                               `ARTICLE_ID` BIGINT(64) NOT NULL COMMENT '文章编号',
                                               `PUBLISH_TIME` DATETIME NOT NULL COMMENT '消息发布时间: yyyy-MM-dd HH:mm',
                                               `PRODUCER_TIME` DATETIME NOT NULL COMMENT '消息生产时间: yyyy-MM-dd HH:mm:ss',
                                               `CONSUMER_TIME` DATETIME NOT NULL DEFAULT '' COMMENT '消息消费时间: yyyy-MM-dd HH:mm:ss',
                                               `CONSUMED` TINYINT(1) DEFAULT 0 COMMENT '消息是否被消费',
                                               `VALID` TINYINT(1) DEFAULT 1 COMMENT '消息是否有效',
                                               `DESCRIPTION` VARCHAR(100) DEFAULT NULL DEFAULT '' COMMENT '备注',
                                               PRIMARY KEY (`ID`) USING BTREE,
                                               KEY `INDEX_KEY_ARTICLE_ID` (`ARTICLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文章自动发布状态记录表';

DROP TABLE IF EXISTS `article_access_user`;
CREATE TABLE `article_access_user` (
                                       `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                       `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                       `USER_ID` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
                                       `EXPIRE_TIME` DATETIME DEFAULT NULL COMMENT '授权过期时间，NULL表示永久有效',
                                       `STATUS` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '授权状态：0-失效；1-有效',
                                       `REMARK` VARCHAR(500) DEFAULT NULL COMMENT '授权备注',
                                       `CREATE_USER` BIGINT DEFAULT NULL COMMENT '授权人',
                                       `CREATE_TIME` DATETIME NOT NULL COMMENT '授权时间',
                                       `UPDATE_USER` BIGINT DEFAULT NULL COMMENT '最后修改人',
                                       `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '最后修改时间',
                                       PRIMARY KEY (`ID`) USING BTREE,
                                       UNIQUE KEY `uk_article_access_user` (`ARTICLE_ID`, `USER_ID`),
                                       KEY `idx_article_access_user_user` (`USER_ID`),
                                       KEY `idx_article_access_user_expire` (`EXPIRE_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章指定用户访问权限表';

DROP TABLE IF EXISTS `article_access_verify`;
CREATE TABLE `article_access_verify` (
                                         `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                         `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                         `VERIFY_TYPE` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '验证方式：1-公众号关注验证码',
                                         `VERIFY_CODE` VARCHAR(100) DEFAULT NULL COMMENT '访问验证码',
                                         `VERIFY_HINT` VARCHAR(500) DEFAULT NULL COMMENT '验证提示',
                                         `EXPIRE_TIME` DATETIME DEFAULT NULL COMMENT '验证码过期时间',
                                         `ENABLED` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
                                         `VERSION` INT NOT NULL DEFAULT 1 COMMENT '乐观锁',
                                         `CREATE_USER` BIGINT DEFAULT NULL COMMENT '创建人',
                                         `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                                         `UPDATE_USER` BIGINT DEFAULT NULL COMMENT '更新人',
                                         `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`ID`) USING BTREE,
                                         UNIQUE KEY `uk_article_access_verify_article` (`ARTICLE_ID`),
                                         KEY `idx_article_access_verify_type` (`VERIFY_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章访问验证配置表';

DROP TABLE IF EXISTS `article_access_verify_record`;
CREATE TABLE `article_access_verify_record` (
                                                `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                                `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                                `USER_ID` BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID',
                                                `OPEN_ID` VARCHAR(100) DEFAULT NULL COMMENT '微信公众号OpenID',
                                                `VERIFY_CODE` VARCHAR(20) DEFAULT NULL COMMENT '本次验证码',
                                                `VERIFY_STATUS` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '验证状态：0-待验证；1-验证成功；2-验证失败；3-已过期',
                                                `ERROR_COUNT` INT NOT NULL DEFAULT 0 COMMENT '验证失败次数',
                                                `EXPIRE_TIME` DATETIME NOT NULL COMMENT '验证码过期时间',
                                                `VERIFY_TIME` DATETIME DEFAULT NULL COMMENT '验证成功时间',
                                                `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                                                PRIMARY KEY (`ID`) USING BTREE,
                                                KEY `idx_article_verify_record_article` (`ARTICLE_ID`),
                                                KEY `idx_article_verify_record_user` (`USER_ID`),
                                                KEY `idx_article_verify_record_open_id` (`OPEN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章访问验证记录表';

DROP TABLE IF EXISTS `article_pay_config`;
CREATE TABLE `article_pay_config` (
                                      `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                      `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                      `PRICE` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '文章售价',
                                      `CURRENCY` VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
                                      `CONTENT_TYPE` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '付费内容：1-全文',
                                      `VALID_DAYS` INT DEFAULT NULL COMMENT '购买有效期，NULL表示永久有效',
                                      `SALE_STATUS` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '销售状态：0-下架；1-销售中',
                                      `VERSION` INT NOT NULL DEFAULT 1 COMMENT '乐观锁',
                                      `CREATE_USER` BIGINT DEFAULT NULL COMMENT '创建人',
                                      `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                                      `UPDATE_USER` BIGINT DEFAULT NULL COMMENT '更新人',
                                      `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
                                      PRIMARY KEY (`ID`) USING BTREE,
                                      UNIQUE KEY `uk_article_pay_config_article` (`ARTICLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章付费配置表';

DROP TABLE IF EXISTS `article_order`;
CREATE TABLE `article_order` (
                                 `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                 `ORDER_NO` VARCHAR(64) NOT NULL COMMENT '订单号',
                                 `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                 `USER_ID` BIGINT UNSIGNED NOT NULL COMMENT '购买用户ID',
                                 `PRICE` DECIMAL(10,2) NOT NULL COMMENT '订单金额',
                                 `PAY_AMOUNT` DECIMAL(10,2) NOT NULL COMMENT '实际支付金额',
                                 `CURRENCY` VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
                                 `ORDER_STATUS` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付；1-已支付；2-已关闭；3-已退款',
                                 `PAY_CHANNEL` VARCHAR(30) DEFAULT NULL COMMENT '支付渠道',
                                 `PAY_TIME` DATETIME DEFAULT NULL COMMENT '支付时间',
                                 `EXPIRE_TIME` DATETIME DEFAULT NULL COMMENT '订单过期时间',
                                 `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                                 `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`ID`) USING BTREE,
                                 UNIQUE KEY `uk_article_order_no` (`ORDER_NO`),
                                 KEY `idx_article_order_article` (`ARTICLE_ID`),
                                 KEY `idx_article_order_user` (`USER_ID`),
                                 KEY `idx_article_order_status` (`ORDER_STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章购买订单表';

DROP TABLE IF EXISTS `article_access_grant`;
CREATE TABLE `article_access_grant` (
                                        `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                        `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                        `USER_ID` BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID',
                                        `VISITOR_ID` VARCHAR(64) DEFAULT NULL COMMENT '访客ID',
                                        `GRANT_TYPE` TINYINT(2) NOT NULL COMMENT '授权方式：1-密码；2-公众号验证；3-付费',
                                        `SOURCE_ID` BIGINT UNSIGNED DEFAULT NULL COMMENT '授权来源ID，如订单ID、验证记录ID',
                                        `GRANT_TIME` DATETIME NOT NULL COMMENT '授权时间',
                                        `EXPIRE_TIME` DATETIME DEFAULT NULL COMMENT '授权过期时间，NULL表示永久',
                                        `STATUS` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '授权状态：0-失效；1-有效',
                                        `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                                        PRIMARY KEY (`ID`) USING BTREE,
                                        UNIQUE KEY `uk_article_access_grant_user` (`ARTICLE_ID`, `USER_ID`, `GRANT_TYPE`),
                                        KEY `idx_article_access_grant_visitor` (`ARTICLE_ID`, `VISITOR_ID`, `GRANT_TYPE`),
                                        KEY `idx_article_access_grant_expire` (`EXPIRE_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章访问授权表';

article
   │
   ├── ARTICLE_PERM
   └── ACCESS_PASSWORD
          │
          ├── article_access_verify       公众号验证配置
          ├── article_access_verify_record 公众号验证记录
          ├── article_pay_config          付费配置
          ├── article_order               购买订单
          └── article_access_grant        访问授权

DROP TABLE IF EXISTS `article_top`;
CREATE TABLE `article_top` (
                               `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                               `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                               `SORT` INT NOT NULL DEFAULT 99999 COMMENT '置顶排序',
                               `STATUS` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-停用；1-启用',
                               `START_TIME` DATETIME DEFAULT NULL COMMENT '开始置顶时间',
                               `END_TIME` DATETIME DEFAULT NULL COMMENT '结束置顶时间',
                               `CREATE_USER` BIGINT DEFAULT NULL COMMENT '创建人',
                               `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                               `UPDATE_USER` BIGINT DEFAULT NULL COMMENT '更新人',
                               `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`ID`) USING BTREE,
                               UNIQUE KEY `uk_article_top_article` (`ARTICLE_ID`),
                               KEY `idx_article_top_sort` (`SORT`),
                               KEY `idx_article_top_status_time` (`STATUS`, `START_TIME`, `END_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章置顶运营表';

DROP TABLE IF EXISTS `article_recommend`;
CREATE TABLE `article_recommend` (
                                     `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                     `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                     `SORT` INT NOT NULL DEFAULT 99999 COMMENT '推荐排序',
                                     `STATUS` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-停用；1-启用',
                                     `START_TIME` DATETIME DEFAULT NULL COMMENT '开始推荐时间',
                                     `END_TIME` DATETIME DEFAULT NULL COMMENT '结束推荐时间',
                                     `CREATE_USER` BIGINT DEFAULT NULL COMMENT '创建人',
                                     `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                                     `UPDATE_USER` BIGINT DEFAULT NULL COMMENT '更新人',
                                     `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`ID`) USING BTREE,
                                     UNIQUE KEY `uk_article_recommend_article` (`ARTICLE_ID`),
                                     KEY `idx_article_recommend_sort` (`SORT`),
                                     KEY `idx_article_recommend_status_time` (`STATUS`, `START_TIME`, `END_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章推荐运营表';

DROP TABLE IF EXISTS `article_operation`;
CREATE TABLE `article_operation` (
                                     `ID` BIGINT UNSIGNED NOT NULL COMMENT '主键',
                                     `ARTICLE_ID` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
                                     `OPERATION_TYPE` TINYINT NOT NULL COMMENT '运营类型：1-置顶；2-推荐；3-热门；4-精选',
                                     `SORT` INT NOT NULL DEFAULT 99999 COMMENT '运营排序',
                                     `STATUS` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-停用；1-启用',
                                     `START_TIME` DATETIME DEFAULT NULL COMMENT '开始时间',
                                     `END_TIME` DATETIME DEFAULT NULL COMMENT '结束时间',
                                     `CREATE_USER` BIGINT DEFAULT NULL COMMENT '创建人',
                                     `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间',
                                     `UPDATE_USER` BIGINT DEFAULT NULL COMMENT '更新人',
                                     `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`ID`) USING BTREE,
                                     UNIQUE KEY `uk_article_operation` (`ARTICLE_ID`, `OPERATION_TYPE`),
                                     KEY `idx_article_operation_type_status` (`OPERATION_TYPE`, `STATUS`),
                                     KEY `idx_article_operation_sort` (`OPERATION_TYPE`, `SORT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文章运营表';

运营管理
├── 文章运营
│   ├── 置顶
│   ├── 推荐
│   ├── 热门
│   └── 精选
├── 首页配置
└── ...

DROP TABLE IF EXISTS `article_auto_publish_timing`;
CREATE TABLE `article_auto_publish_timing` (
                                               `ID` BIGINT UNSIGNED NOT NULL,
                                               `ARTICLE_ID` BIGINT NOT NULL,

                                               `PUBLISH_TIME` DATETIME NOT NULL,
                                               `STATUS` TINYINT DEFAULT 0 COMMENT '0待发布 1已发布 2失败',

                                               `RETRY_COUNT` INT DEFAULT 0,
                                               `ERROR_MSG` VARCHAR(500),

                                               `CREATE_TIME` DATETIME NOT NULL,
                                               `UPDATE_TIME` DATETIME,

                                               PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时发布';

CREATE TABLE article_content_version (
                                         ID BIGINT UNSIGNED NOT NULL,
                                         ARTICLE_ID BIGINT UNSIGNED NOT NULL,
                                         VERSION INT UNSIGNED NOT NULL,
                                         TITLE VARCHAR(150) NOT NULL,
                                         SUMMARY VARCHAR(250),
                                         CONTENT_MD MEDIUMTEXT NOT NULL,
                                         CONTENT_HTML MEDIUMTEXT,
                                         WORDS_COUNT INT UNSIGNED NOT NULL DEFAULT 0,
                                         CREATE_USER_ID BIGINT,
                                         CREATE_TIME DATETIME NOT NULL,

                                         PRIMARY KEY (ID),
                                         UNIQUE KEY uk_article_version (ARTICLE_ID, VERSION),
                                         KEY idx_article_id (ARTICLE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
    COMMENT='文章内容版本表';






/*Table structure for table `article_column` */
DROP TABLE IF EXISTS `article_column`;
CREATE TABLE `article_column` (
                                  `ID` BIGINT(64) UNSIGNED NOT NULL,
                                  `NAME` VARCHAR(50) NOT NULL COMMENT '专栏名称',
                                  `IMAGE_URL` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '专栏预览图',
                                  `DESC` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '专栏描述',
                                  `SORT` INT(11) NOT NULL DEFAULT 99999 COMMENT '排序字段',
                                  `PAGE_SHOW` TINYINT(1) DEFAULT 0 COMMENT '是否首页展示',
                                  `ENABLED` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
                                  `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                                  `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                                  `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                                  `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                                  `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                                  `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                                  PRIMARY KEY (`ID`) USING BTREE,
                                  KEY `INDEX_KEY_NAME` (`NAME`),
                                  KEY `INDEX_KEY_PAGE_SHOW` (`PAGE_SHOW`),
                                  KEY `INDEX_KEY_ENABLED` (`ENABLED`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文章专栏表';

/*Table structure for table `article_column_relation` */
DROP TABLE IF EXISTS `article_column_relation`;
CREATE TABLE `article_column_relation` (
                                           `ID` BIGINT(64) UNSIGNED NOT NULL,
                                           `COLUMN_ID` BIGINT(64) NOT NULL COMMENT '专栏编号',
                                           `ARTICLE_ID` BIGINT(64) NOT NULL COMMENT '文章编号',
                                           `ARTICLE_TITLE` VARCHAR(100) NOT NULL COMMENT '文章标题',
                                           `SORT` INT(11) NOT NULL DEFAULT 99999 COMMENT '排序字段',
                                           `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                                           `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                                           `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                                           `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                                           `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                                           `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                                           PRIMARY KEY (`ID`) USING BTREE,
                                           UNIQUE KEY `KEY_ARTICLE_COLUMN` (`ARTICLE_ID`, `COLUMN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文章专栏关联表';

/*Table structure for table `talk` */
DROP TABLE IF EXISTS `talk`;
CREATE TABLE `talk` (
                        `ID` BIGINT(64) UNSIGNED NOT NULL,
                        `CONTENT` TEXT NOT NULL COMMENT '说说内容',
                        `IMAGES` TEXT COMMENT '图片列表',
                        `STATUS` INT(2) NOT NULL COMMENT '说说状态',
                        `IS_TOP` TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
                        `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
                        `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                        `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                        `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                        `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                        `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                        `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                        PRIMARY KEY (`ID`) USING BTREE,
                        KEY `INDEX_KEY_IS_TOP` (`IS_TOP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='说说';

/*Data for the table `talk` */
INSERT INTO talk (ID, CONTENT, IMAGES, STATUS, IS_TOP, DELETED, VERSION, CREATE_USER, CREATE_TIME, CREATE_DATE, UPDATE_USER, UPDATE_TIME) VALUES (3550278505713369088, '声明：博客框架为SpringBoot2.5 + Vue2，大家有Bug和优化提议欢迎提Issue，也可以加入QQ群（760210629）进行交流。项目前端借鉴了《风、宇个人博客》和《ELADMIN》两个开源项目的内容，感谢两个项目的作者！<img src="https://suntaoblog.oss-cn-beijing.aliyuncs.com/emoji/dacall.jpg" width="24" height="24" alt="[打call]" style="margin: 0 1px; vertical-align: text-bottom">', '', 1, 1, 0, 1, 'admin', '2023-01-06 21:39:16', '2023-01-06', 'admin', '2023-01-06 22:11:01');

/*Table structure for table `album` */
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album` (
                         `ID` BIGINT(64) UNSIGNED NOT NULL,
                         `ALBUM_NAME` VARCHAR(20) NOT NULL COMMENT '相册名称',
                         `ALBUM_DESC` VARCHAR(50) NOT NULL COMMENT '相册描述',
                         `ALBUM_COVER` VARCHAR(500) NOT NULL COMMENT '相册封面',
                         `STATUS` INT(2) NOT NULL COMMENT '相册状态',
                         `DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
                         `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                         `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                         `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                         `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                         `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                         `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                         PRIMARY KEY (`ID`) USING BTREE,
                         KEY `KEY_STATUS` (`STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='相册';

/*Table structure for table `album_photo` */
DROP TABLE IF EXISTS `album_photo`;
CREATE TABLE `album_photo` (
                               `ID` BIGINT(64) UNSIGNED NOT NULL,
                               `ALBUM_ID` BIGINT(64) NOT NULL COMMENT '所属相册',
                               `FILE_ID` BIGINT(64) NOT NULL COMMENT '图片ID',
                               `PHOTO_NAME` VARCHAR(25) NOT NULL COMMENT '照片名称',
                               `PHOTO_DESC` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '照片描述',
                               `PHOTO_LINK` VARCHAR(500) NOT NULL COMMENT '照片地址',
                               `IS_DELETED` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
                               `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                               `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                               `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                               `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                               `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                               `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                               PRIMARY KEY (`ID`) USING BTREE,
                               KEY `KEY_DELETED` (`IS_DELETED`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='照片';








/*Table structure for table `talk_like` */
DROP TABLE IF EXISTS `talk_like`;
CREATE TABLE `talk_like` (
                             `ID` BIGINT(64) UNSIGNED NOT NULL,
                             `USER_ID` BIGINT(64) NOT NULL COMMENT '点赞用户',
                             `TALK_ID` BIGINT(64) NULL DEFAULT NULL COMMENT '点赞说说',
                             `IP_ADDRESS` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '点赞用户IP',
                             `IP_SOURCE` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '点赞用户来源',
                             `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                             `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                             `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                             `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                             `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                             `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                             PRIMARY KEY (`ID`) USING BTREE,
                             UNIQUE KEY `KEY_USER_ID_TALK_ID` (`USER_ID`, `TALK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='说说点赞信息';

/*Table structure for table `article_like` */
DROP TABLE IF EXISTS `article_like`;
CREATE TABLE `article_like` (
                                `ID` BIGINT(64) UNSIGNED NOT NULL,
                                `USER_ID` BIGINT(64) NOT NULL COMMENT '点赞用户',
                                `ARTICLE_ID` BIGINT(64) NULL DEFAULT NULL COMMENT '点赞文章',
                                `IP_ADDRESS` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '点赞用户IP',
                                `IP_SOURCE` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '点赞用户来源',
                                `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                                `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                                `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                                `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                                `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                                `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                                PRIMARY KEY (`ID`) USING BTREE,
                                UNIQUE KEY `KEY_USER_ID_ARTICLE_ID` (`USER_ID`, `ARTICLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文章点赞记录';

/*Table structure for table `article_view` */
DROP TABLE IF EXISTS `article_view`;
CREATE TABLE `article_view` (
                                `ID` BIGINT(64) UNSIGNED NOT NULL,
                                `ARTICLE_ID` BIGINT(64) NOT NULL COMMENT '文章编号',
                                `ACCESS_IP` VARCHAR(200) NOT NULL COMMENT '访问IP',
                                `ACCESS_ADDRESS` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '访问地址',
                                `ACCESS_TIME` VARCHAR(50) NOT NULL COMMENT '访问时间',
                                `VERSION` INT(11) NOT NULL DEFAULT 1 COMMENT '乐观锁',
                                `CREATE_USER` VARCHAR(20) NOT NULL COMMENT '创建用户',
                                `CREATE_TIME` VARCHAR(20) NOT NULL COMMENT '创建时间',
                                `CREATE_DATE` VARCHAR(20) NOT NULL COMMENT '创建日期',
                                `UPDATE_USER` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新用户',
                                `UPDATE_TIME` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '更新时间',
                                PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文章浏览记录';
