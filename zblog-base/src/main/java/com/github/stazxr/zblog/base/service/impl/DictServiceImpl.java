package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 字典业务实现层
 *
 * @author SunTao
 * @since 2021-02-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Resource
    private DictMapper dictMapper;

    /**
     * 根据key查找字典项列表
     *
     * @param key Key
     * @return Map<name, value>，一般用于渲染select options
     */
    @Override
    public Map<String, String> selectItems(String key) {
        Assert.notBlank(key, "查询参数不能为空");
        List<Dict> dicts = dictMapper.selectItems(key);

        // return map
        Map<String, String> options = new LinkedHashMap<>();
        if (dicts.size() > 0) {
            dicts.forEach(dict -> options.put(dict.getName(), dict.getValue()));
        }
        return options;
    }
}
