package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.converter.DictConverter;
import com.github.stazxr.zblog.base.domain.bo.NameValue;
import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.enums.DictType;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 字典管理业务实现层
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
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return IPage<DictVo>
     */
    @Override
    public IPage<DictVo> queryDictListByPage(DictQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getDictName())) {
            queryDto.setDictName(queryDto.getDictName().trim());
        }
        // 分页查询
        Page<DictVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return dictMapper.selectDictList(page, queryDto);
    }

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return List<DictVo>
     */
    @Override
    public List<DictVo> queryChildList(Long pid) {
        Assert.notNull(pid, ExpMessageCode.of("valid.dict.pid.required"));
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
        Assert.notNull(dictId, ExpMessageCode.of("valid.common.id.required"));
        DictVo dictVo = dictMapper.selectDictDetail(dictId);
        Assert.notNull(dictVo, ExpMessageCode.of("valid.common.data.notFound"));
        return dictVo;
    }

    /**
     * 新增字典
     *
     * @param dictDto 字典
     */
    @Override
    public void addDict(DictDto dictDto) {
        // 获取字典信息
        Dict dict = dictConverter.dtoToEntity(dictDto);
        // 新增时，不允许传入 DictId
        Assert.isNull(dict.getId(), ExpMessageCode.of("valid.common.add.idNotAllowed"));
        // 字典信息检查
        checkDict(dict);
        // 新增字典
        Assert.affectOneRow(dictMapper.insert(dict), ExpMessageCode.of("result.common.add.failed"));
    }

    /**
     * 编辑字典
     *
     * @param dictDto 字典
     */
    @Override
    public void editDict(DictDto dictDto) {
        // 获取字典信息
        Dict dict = dictConverter.dtoToEntity(dictDto);
        // 判断字典是否存在
        Dict dbDict = dictMapper.selectById(dict.getId());
        Assert.notNull(dbDict, ExpMessageCode.of("valid.common.data.notFound"));
        // 字典信息检查
        checkDict(dict);
        // 编辑字典
        Assert.affectOneRow(dictMapper.updateById(dict), ExpMessageCode.of("result.common.edit.failed"));
    }

    /**
     * 删除字典
     *
     * @param dictId 字典序列
     */
    @Override
    public void deleteDict(Long dictId) {
        // 判断字典是否存在
        DictVo dictVo = dictMapper.selectDictDetail(dictId);
        Assert.notNull(dictVo, ExpMessageCode.of("valid.common.data.notFound"));
        // 存在子节点无法被删除
        Assert.failIfTrue(dictVo.getHasChildren(), ExpMessageCode.of("valid.dict.delete.withChildren"));
        // 删除字典
        Assert.affectOneRow(dictMapper.deleteById(dictId), ExpMessageCode.of("result.common.delete.failed"));
    }

    /**
     * 根据字典KEY查询配置信息列表
     *
     * @param dictKey 字典KEY
     * @return List<NameValue>
     */
    @Override
    public List<NameValue> queryConfListByDictKey(String dictKey) {
        Assert.notBlank(dictKey, ExpMessageCode.of("valid.dict.dictKey.required"));
        return dictMapper.selectNameValuesByDictKey(dictKey);
    }

    /**
     * 根据字典KEY查询配置信息
     *
     * @param dictKey 字典KEY
     * @return VALUE
     */
    @Override
    public String queryDictValueByDictKey(String dictKey) {
        Assert.notBlank(dictKey, ExpMessageCode.of("valid.dict.dictKey.required"));
        return dictMapper.selectDictValueByDictKey(dictKey);
    }

    private void checkDict(Dict dict) {
        // 字典类型校验
        Assert.notNull(DictType.of(dict.getDictType()), ExpMessageCode.of( "valid.dict.dictType.invalid"));

        // 检查其他数据
        if (DictType.GROUP.getValue().equals(dict.getDictType())) {
            // 组
            dict.setPid(null);
            dict.setDictKey(null);
            dict.setDictValue(null);
            dict.setEnabled(true);
        } else {
            // 项
            Assert.notNull(dict.getPid(), ExpMessageCode.of("valid.dict.pid.required"));
            Dict parentDict = dictMapper.selectById(dict.getPid());
            Assert.notNull(parentDict, ExpMessageCode.of("valid.dict.parent.notExist"));
            boolean parentIsGroup = DictType.GROUP.getValue().equals(parentDict.getDictType());
            Assert.failIfFalse(parentIsGroup, ExpMessageCode.of("valid.dict.parent.type"));
            Assert.notNull(dict.getEnabled(), ExpMessageCode.of("valid.dict.enabled.required"));
            Assert.notBlank(dict.getDictKey(), ExpMessageCode.of("valid.dict.dictKey.required"));
        }
    }
}
