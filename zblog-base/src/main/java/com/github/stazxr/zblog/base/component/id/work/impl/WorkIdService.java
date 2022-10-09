package com.github.stazxr.zblog.base.component.id.work.impl;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.base.component.id.impl.BaseWorkIdIdGeneratorImpl;
import com.github.stazxr.zblog.base.component.id.work.model.WorkIdCache;
import com.github.stazxr.zblog.base.component.id.work.model.WorkResult;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.enums.DictType;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.net.LocalHostUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * workId generate
 *
 * @author SunTao
 * @since 2021-12-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkIdService {
    /**
     * key-value 存储主键
     */
    private static final String DICT_KEY = "guidWorkId";

    private static final long MAX_WORK_ID = (1 << BaseWorkIdIdGeneratorImpl.WORKER_ID_BITS) - 1;

    /**
     * 默认最近使用的workId是0
     */
    private static final Long DEFAULT_LAST_WORK_ID = 0L;

    private final DictService dictService;

    public WorkIdService(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 获取机器ID
     *
     * @return workId
     * @throws UnknownHostException failed get hostIp
     * @throws SocketException if an I/O error occurs.
     */
    public WorkResult generateWorkId() throws UnknownHostException, SocketException {
        String ipAddr = LocalHostUtils.getLocalIp();
        WorkResult result = new WorkResult();
        result.setWorkIp(ipAddr);

        Dict dict = dictService.getById(BaseConst.DictId.WORK_ID);
        if (dict == null) {
            // 不存在机器ID描述信息
            insertDictInfo(ipAddr);
            result.setWorkId(DEFAULT_LAST_WORK_ID);
            return result;
        }

        WorkIdCache workIdCache = JSON.parseObject(dict.getValue(), WorkIdCache.class);
        Map<String, Long> workIdsMap = workIdCache.getWorkIdsCache();

        if (workIdsMap != null && workIdsMap.containsKey(ipAddr)) {
            // 已经保存了当前ip，直接返回对应的workId
            result.setWorkId(workIdsMap.get(ipAddr));
            return result;
        }

        if (workIdsMap == null) {
            workIdsMap = new HashMap<>(16);
        }

        // 刷新机器保存信息
        long newLastId = workIdCache.getLastWorkId() + 1;
        if (newLastId > MAX_WORK_ID) {
            throw new ServiceException(ResultCode.ID_OVER_MAX);
        }
        workIdCache.setLastWorkId(newLastId);
        workIdsMap.put(ipAddr, newLastId);
        workIdCache.setWorkIdsCache(workIdsMap);
        dict.setValue(JSON.toJSONString(workIdCache));
        dictService.updateById(dict);
        result.setWorkId(newLastId);
        return result;
    }

    private void insertDictInfo(String ipAddr) {
        WorkIdCache workIdCache = new WorkIdCache();
        Map<String, Long> workIdsMap = new HashMap<>(1);
        workIdsMap.put(ipAddr, DEFAULT_LAST_WORK_ID);
        workIdCache.setWorkIdsCache(workIdsMap);
        workIdCache.setLastWorkId(DEFAULT_LAST_WORK_ID);

        Dict newDict = new Dict();
        newDict.setId(BaseConst.DictId.WORK_ID);
        newDict.setPid(BaseConst.DictId.SYS);
        newDict.setType(DictType.ITEM.getValue());
        newDict.setName("机器ID信息");
        newDict.setKey(DICT_KEY);
        newDict.setValue(JSON.toJSONString(workIdCache));
        newDict.setDesc("系统参数：存储机器ID信息");
        newDict.setLocked(true);
        newDict.setEnabled(true);
        dictService.save(newDict);
    }
}
