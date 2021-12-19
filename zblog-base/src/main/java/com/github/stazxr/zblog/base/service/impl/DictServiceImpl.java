package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.entity.Dict;
import com.github.stazxr.zblog.base.enums.DictType;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
//
//    /**
//     * 根据Key获取Value值
//     *
//     * @param key Key
//     * @return Value
//     */
//    @Override
//    public Dict selectUniqueDict(String key) {
//        Assert.notNull(key, "dick key must not be null");
//        String searchKey = key.toUpperCase().trim();
//        return queryWrapper().eq(Dict::getEnabled, Boolean.TRUE).eq(Dict::getKey, searchKey).one();
//    }
//
//
//    /**
//     * 根据 key 查找字典信息
//     *
//     * @param key 字典 key
//     * @return 字典信息
//     */
//    @Override
//    public Dict selectDictGroup(String key) {
//        Assert.notNull(key, "dick key must not be null");
//        String searchKey = key.toUpperCase().trim();
//        return queryWrapper()
//                .eq(Dict::getType, DictType.GROUP.value())
//                .eq(Dict::getEnabled, Boolean.TRUE)
//                .eq(Dict::getKey, searchKey)
//                .one();
//    }
//
//    /**
//     * 根据Key获取Value值
//     *
//     * @param key Key
//     * @return Value
//     */
//    @Override
//    public String value(String key) {
//        Dict variable = get(key);
//        if (variable == null) {
//            put(key, "");
//        }
//        variable = get(key);
//        return variable == null ? null : variable.getValue();
//    }
//
//    /**
//     * 根据 key 查找字典项列表
//     *
//     * @param key Key
//     * @return Map<name, value>，一般用于渲染select options
//     */
//    @Override
//    public Map<String, String> selectDictMap(String key) {
//        Assert.notNull(key, "dick key must not be null");
//        List<Dict> dictList = queryWrapper().select(Dict::getName, Dict::getValue)
//                .eq(Dict::getType, DictType.ITEM.value())
//                .eq(Dict::getKey, key.toUpperCase().trim())
//                .eq(Dict::getKey, key.toUpperCase().trim())
//                .orderByAsc(Dict::getOrder)
//                .list();
//
//        // list to map
//        Map<String, String> options = new LinkedHashMap<>();
//        dictList.forEach(dd -> options.put(dd.getName(), dd.getValue()));
//        return options;
//    }
//
//
//
//    /**
//     * 根据Key修改值
//     *
//     * @param key   KEY
//     * @param value VALUE
//     * @return boolean
//     */
//    @Override
//    public boolean put(String key, String value) {
//        String key2 = key.toUpperCase().trim();
//        Dict variable = get(key2);
//        if (variable == null) {
//            variable = new Dict();
//            variable.setKey(key2);
//            variable.setValue(value);
//            variable.setName(key2);
//            variable.setPid(null);
//            variable.setOrder(0);
//            variable.setLocked(false);
//            variable.setType(DictType.GROUP.value());
//            variable.setDesc("");
//            return save(variable);
//        } else {
//            variable.setValue(value.trim());
//            return updateById(variable);
//        }
//    }
//
//    /**
//     * 根据Key修改值
//     *
//     * @param records 存储的记录
//     * @return boolean
//     */
//    @Override
//    public boolean putMap(Map<String, String> records) {
//        for (Map.Entry<String, String> entry : records.entrySet()) {
//            if (!put(entry.getKey(), entry.getValue())) {
//                throw new ServiceException("字典put值失败");
//            }
//        }
//        return true;
//    }

    private LambdaQueryChainWrapper<Dict> queryWrapper() {
        return new LambdaQueryChainWrapper<>(dictMapper);
    }
}
