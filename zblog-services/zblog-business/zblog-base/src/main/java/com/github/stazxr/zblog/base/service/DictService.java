package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.vo.DictVo;

import java.util.List;
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

    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return dictList
     */
    PageInfo<DictVo> queryDictListByPage(DictQueryDto queryDto);

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return dictList
     */
    List<DictVo> queryChildList(Long pid);

    /**
     * 查询字典信息
     *
     * @param dictId 字典序列
     * @return DictVo
     */
    DictVo queryDictDetail(Long dictId);

    /**
     * 新增字典
     *
     * @param dictDto 字典
     */
    void addDict(DictDto dictDto);

    /**
     * 编辑字典
     *
     * @param dictDto 字典
     */
    void editDict(DictDto dictDto);

    /**
     * 删除字典
     *
     * @param dictId 字典序列
     */
    void deleteDict(Long dictId);

    /**
     * 根据KEY查询VALUE
     *
     * @param key 字典KEY
     * @return VALUE
     */
    String querySingleValue(String key);
}
