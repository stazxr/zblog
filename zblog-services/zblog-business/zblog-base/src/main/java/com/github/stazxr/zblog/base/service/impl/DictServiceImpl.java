package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.base.converter.DictConverter;
import com.github.stazxr.zblog.base.domain.bo.NameValue;
import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.enums.DictType;
import com.github.stazxr.zblog.base.domain.error.DictErrorCode;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
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
        DictVo dictVo = dictMapper.selectDictDetail(dictId);
        return ThrowUtils.requireNonNull(dictVo, BaseErrorCode.ECOREA001);
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
        ThrowUtils.when(dict.getId() != null).system(BaseErrorCode.SCOREB001);
        // 字典信息检查
        checkDict(dict);
        // 新增字典
        ThrowUtils.when(!save(dict)).system(BaseErrorCode.SCOREA001);
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
        ThrowUtils.throwIfNull(dbDict, BaseErrorCode.ECOREA001);
        // 字典信息检查
        checkDict(dict);
        // 编辑字典
        ThrowUtils.when(!updateById(dict)).system(BaseErrorCode.SCOREA002);
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
        ThrowUtils.throwIfNull(dictVo, BaseErrorCode.ECOREA001);
        // 存在子节点无法被删除
        ThrowUtils.throwIf(dictVo.getHasChildren(), DictErrorCode.EDICTA004);
        // 删除字典
        ThrowUtils.when(!removeById(dictId)).system(BaseErrorCode.SCOREA003);
    }

    /**
     * 根据字典KEY查询配置信息列表
     *
     * @param dictKey 字典KEY
     * @return List<NameValue>
     */
    @Override
    public List<NameValue> queryConfListByDictKey(String dictKey) {
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
        return dictMapper.selectDictValueByDictKey(dictKey);
    }

    private void checkDict(Dict dict) {
        // 字典类型校验
        ThrowUtils.when(DictType.of(dict.getDictType()) == null).system(BaseErrorCode.SCOREB002);

        // 检查其他数据
        if (DictType.GROUP.getValue().equals(dict.getDictType())) {
            // 组
            dict.setPid(null);
            dict.setDictKey(null);
            dict.setDictValue(null);
            dict.setEnabled(true);
        } else {
            // 项
            ThrowUtils.when(dict.getPid() == null).system(BaseErrorCode.SCOREB000);
            Dict parentDict = dictMapper.selectById(dict.getPid());
            ThrowUtils.throwIfNull(parentDict, DictErrorCode.EDICTA002);
            boolean parentIsGroup = DictType.GROUP.getValue().equals(parentDict.getDictType());
            ThrowUtils.throwIf(!parentIsGroup, DictErrorCode.EDICTA003);
            ThrowUtils.throwIfNull(dict.getEnabled(), DictErrorCode.EDICTA001);
            ThrowUtils.throwIfBlank(dict.getDictKey(), DictErrorCode.EDICTA000);
        }
    }
}
