package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Dict;

import java.util.Map;

/**
 * 字典服务层
 *
 * @author SunTao
 * @since 2021-02-20
 */
public interface DictService extends IService<Dict> {
    /**
     * 根据key查找字典项列表
     *
     * @param key Key
     * @return Map<name, value>，一般用于渲染select options
     */
    Map<String, String> selectItems(String key);
}
