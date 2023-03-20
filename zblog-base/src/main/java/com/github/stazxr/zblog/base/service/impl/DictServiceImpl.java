package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.converter.DictConverter;
import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.enums.DictType;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 字典业务实现层
 *
 * @author SunTao
 * @since 2021-02-20
 */
@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    private final DictMapper dictMapper;

    private final DictConverter dictConverter;

    /**
     * 根据key查找字典项列表
     *
     * @param key Key
     * @return Map<name, value>，一般用于渲染select options
     */
    @Override
    public Map<String, String> selectItems(String key) {
        Assert.isTrue(StringUtils.isBlank(key), "参数【key】不能为空");
        List<Dict> dicts = dictMapper.selectItems(key);

        // return map
        Map<String, String> options = new LinkedHashMap<>();
        if (dicts.size() > 0) {
            dicts.forEach(dict -> options.put(dict.getName(), dict.getValue()));
        }
        return options;
    }

    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return dictList
     */
    @Override
    public PageInfo<DictVo> queryDictListByPage(DictQueryDto queryDto) {
        queryDto.checkPage();

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(dictMapper.selectDictList(queryDto));
    }

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return dictList
     */
    @Override
    public List<DictVo> queryChildList(Long pid) {
        Assert.notNull(pid, "参数【pid】不能为空");
        return dictMapper.selectChildList(pid);
    }

    /**
     * 查询字典信息
     *
     * @param dictId 字典序列
     * @return DictVo
     */
    @Override
    public DictVo queryDictDetail(Long dictId) {
        Assert.notNull(dictId, "参数【dictId】不能为空");
        DictVo dictVo = dictMapper.selectDictDetail(dictId);
        Assert.notNull(dictVo, "查询字典信息失败，字典【" + dictId + "】不存在");
        return dictVo;
    }

    /**
     * 新增字典
     *
     * @param dictDto 字典
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDict(DictDto dictDto) {
        dictDto.setId(null);
        Dict dict = dictConverter.dtoToEntity(dictDto);
        checkDict(dict);
        Assert.isTrue(dictMapper.insert(dict) != 1, "新增失败");
    }

    /**
     * 编辑字典
     *
     * @param dictDto 字典
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editDict(DictDto dictDto) {
        Assert.notNull(dictDto.getId(), "参数【id】不能为空");
        Dict dict = dictConverter.dtoToEntity(dictDto);
        checkDict(dict);
        Assert.isTrue(dictMapper.updateById(dict) != 1, "修改失败");
    }

    /**
     * 删除字典
     *
     * @param dictId 字典序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(Long dictId) {
        DictVo dictVo = queryDictDetail(dictId);
        DataValidated.isTrue(dictVo.getLocked(), "该字典被锁定，不允许删除");
        DataValidated.isTrue(dictVo.getHasChildren(), "该字典存在子节点，无法被删除");
        Assert.isTrue(dictMapper.deleteById(dictId) != 1, "删除失败");
    }

    /**
     * 根据KEY查询VALUE
     *
     * @param key 字典KEY
     * @return VALUE
     */
    @Override
    public String querySingleValue(String key) {
        Assert.isTrue(StringUtils.isBlank(key), "参数【key】不能为空");
        return dictMapper.selectSingleValue(key);
    }

    private void checkDict(Dict dict) {
        if (dict.getId() != null) {
            Dict dbDict = dictMapper.selectById(dict.getId());
            Assert.notNull(dbDict, "字典【" + dict.getId() + "】不存在");
            DataValidated.isTrue(dbDict.getLocked(), "该字典被锁定，不允许编辑");
        }

        dict.setLocked(Boolean.FALSE);
        Assert.isTrue(StringUtils.isBlank(dict.getName()), "字典名称不能为空");
        Assert.notNull(dict.getSort(), "字典排序不能为空");
        Assert.notNull(dict.getType(), "字典类型不能为空");
        Assert.notNull(DictType.of(dict.getType()), "字典类型不正确，取值范围[1, 2]");
        if (DictType.GROUP.getValue().equals(dict.getType())) {
            dict.setPid(null);
            dict.setKey(null);
            dict.setValue(null);
            dict.setEnabled(true);
        } else {
            Assert.notNull(dict.getPid(), "父字典不能为空");
            Assert.isTrue(StringUtils.isBlank(dict.getKey()), "字典KEY不能为空");
            Assert.notNull(dict.getEnabled(), "字典状态不能为空");
        }
    }
}
