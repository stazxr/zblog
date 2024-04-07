package com.github.stazxr.easycloud.id.core.service;

import com.github.stazxr.easycloud.id.IdGenerator;
import com.github.stazxr.easycloud.id.IdGeneratorAware;
import com.github.stazxr.easycloud.id.core.service.IdGeneratorService;

/**
 * 序号生成服务默认实现
 *
 * @author SunTao
 * @since 2024-04-07
 */
public class EasyIdDefaultServiceImpl implements IdGeneratorAware, IdGeneratorService {
    private IdGenerator idGenerator;

    /**
     * 设置ID生成器
     *
     * @param idGenerator IdGenerator的实现类
     */
    @Override
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * 生成一个序号
     *
     * @return 序号
     */
    @Override
    public Long generateId() {
        if (idGenerator == null) {
            throw new RuntimeException("idGenerator is null.");
        }
        return idGenerator.nextId();
    }
}
