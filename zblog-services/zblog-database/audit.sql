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
  KEY `idx_uid` (`UID`),
  KEY `idx_oid` (`OID`),
  KEY `idx_scene` (`SCENE`),
  KEY `idx_decision` (`DECISION`),
  KEY `idx_create_time` (`CREATE_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容审核记录表';
