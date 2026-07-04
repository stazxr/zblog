package com.github.stazxr.zblog.audit.record;

import com.github.stazxr.zblog.audit.mapper.AuditRecordMapper;
import com.github.stazxr.zblog.audit.model.AuditRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 异步审计记录写入器
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
@Component
public class AuditRecordAsyncWriter {
    private static final Logger log = LoggerFactory.getLogger(AuditRecordAsyncWriter.class);

    private final BlockingQueue<AuditRecord> queue = new LinkedBlockingQueue<>(10000);

    private final AuditRecordMapper auditRecordMapper;

    public AuditRecordAsyncWriter(AuditRecordMapper auditRecordMapper) {
        this.auditRecordMapper = auditRecordMapper;

        Thread t = new Thread(this::consume);
        t.setName("audit-record-writer");
        t.setDaemon(true);
        t.start();
    }

    /**
     * 写入队列
     */
    @SuppressWarnings("all")
    public void write(AuditRecord record) {
        queue.offer(record);
    }

    /**
     * 消费线程
     */
    private void consume() {
        while (true) {
            try {
                AuditRecord record = queue.take();
                persist(record);
            } catch (Exception e) {
                log.error("audit record async writer error", e);
            }
        }
    }

    private void persist(AuditRecord record) {
        auditRecordMapper.insert(record);
    }
}