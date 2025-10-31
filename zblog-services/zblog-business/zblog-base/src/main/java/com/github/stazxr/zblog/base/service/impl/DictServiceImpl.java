package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<DictVo>
     */
    @Override
    public PageInfo<DictVo> queryDictListByPage(DictQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getDictName())) {
            queryDto.setDictName(queryDto.getDictName().trim());
        }

        // 分页查询
        try (Page<DictVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<DictVo> dataList = dictMapper.selectDictList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return List<DictVo>
     */
    @Override
    public List<DictVo> queryChildList(Long pid) {
        Assert.notNull(pid, ExpMessageCode.of("valid.dict.pid.NotNull"));
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
        Assert.notNull(dictId, ExpMessageCode.of("valid.dict.id.NotNull"));
        DictVo dictVo = dictMapper.selectDictDetail(dictId);
        Assert.notNull(dictVo, ExpMessageCode.of("valid.dict.not.exist"));
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
        Assert.isNull(dict.getId(), ExpMessageCode.of("valid.dict.add.idIsNull"));
        // 字典信息检查
        checkDict(dict);
        // 新增字典
        Assert.isTrue(dictMapper.insert(dict) != 1, ExpMessageCode.of("result.dict.add.failed"));
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
        Assert.notNull(dbDict, ExpMessageCode.of("valid.dict.not.exist"));
        // 字典信息检查
        checkDict(dict);
        // 编辑字典
        Assert.isTrue(dictMapper.updateById(dict) != 1, ExpMessageCode.of("result.dict.edit.failed"));
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
        Assert.notNull(dictVo, ExpMessageCode.of("valid.dict.not.exist"));
        // 存在子节点无法被删除
        Assert.isTrue(dictVo.getHasChildren(), ExpMessageCode.of("valid.dict.deleteWithChildren"));
        // 删除字典
        Assert.isTrue(dictMapper.deleteById(dictId) != 1, ExpMessageCode.of("result.dict.delete.failed"));
    }

    /**
     * 根据字典KEY查询配置信息列表
     *
     * @param dictKey 字典KEY
     * @return List<NameValue>
     */
    @Override
    public List<NameValue> queryConfListByDictKey(String dictKey) {
        Assert.notBlank(dictKey, ExpMessageCode.of("valid.dict.dictKey.NotBlank"));
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
        Assert.notBlank(dictKey, ExpMessageCode.of("valid.dict.dictKey.NotBlank"));
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
            Assert.notNull(dict.getPid(), ExpMessageCode.of("valid.dict.pid.NotNull"));
            Dict parentDict = dictMapper.selectById(dict.getPid());
            Assert.notNull(parentDict, ExpMessageCode.of("valid.dict.parent.notExist"));
            boolean parentIsGroup = DictType.GROUP.getValue().equals(parentDict.getDictType());
            Assert.isTrue(!parentIsGroup, ExpMessageCode.of("valid.dict.parentIsNotGroup"));
            Assert.notNull(dict.getEnabled(), ExpMessageCode.of("valid.dict.enabled.NotNull"));
            Assert.notBlank(dict.getDictKey(), ExpMessageCode.of("valid.dict.dictKey.NotBlank"));
        }
    }
}
