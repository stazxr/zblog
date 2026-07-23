-- ----------------------------
-- Table structure for audit_record
-- ----------------------------
DROP TABLE IF EXISTS `audit_record`;
CREATE TABLE `audit_record` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UID` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `OID` VARCHAR(64) DEFAULT NULL COMMENT '业务对象ID',
  `SCENE` VARCHAR(32) NOT NULL COMMENT '审核场景 COMMENT/MESSAGE/...',
  `ORIGINAL_CONTENT` TEXT COMMENT '原始内容',
  `FINAL_CONTENT` TEXT COMMENT '最终内容',
  `DECISION` VARCHAR(16) NOT NULL COMMENT '审核结果',
  `HIT_WORDS` JSON COMMENT '命中关键词',
  `REASON` VARCHAR(1024) COMMENT '审核原因',
  `TRACES` JSON COMMENT '处理器执行轨迹',
  `COST_MS` BIGINT DEFAULT 0 COMMENT '总耗时(ms)',
  `CREATE_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`),
  KEY `idx_audit_record_uid` (`UID`),
  KEY `idx_audit_record_oid` (`OID`),
  KEY `idx_audit_record_scene` (`SCENE`),
  KEY `idx_audit_record_decision` (`DECISION`),
  KEY `idx_audit_record_create_time` (`CREATE_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容审核记录表';

-- ----------------------------
-- Table structure for sensitive_word
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `WORD` VARCHAR(255) NOT NULL COMMENT '敏感词',
  `TYPE` VARCHAR(50) DEFAULT 'DEFAULT' COMMENT '词类型',
  `LEVEL` TINYINT DEFAULT 1 COMMENT '风险等级：1-低 2-中 3-高',
  `STATUS` TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
  `REMARK` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  `CREATE_USER` BIGINT NOT NULL COMMENT '创建用户',
  `CREATE_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_USER` BIGINT DEFAULT NULL COMMENT '更新用户',
  `UPDATE_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_sensitive_word` (`WORD`),
  KEY `idx_sensitive_word_status` (`STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='敏感词库表';
