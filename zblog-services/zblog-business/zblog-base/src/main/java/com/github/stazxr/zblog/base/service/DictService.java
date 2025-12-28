package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.bo.NameValue;
import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.vo.DictVo;

import java.util.List;

/**
 * 字典管理业务层
 *
 * @author SunTao
 * @since 2021-02-20
 */
public interface DictService extends IService<Dict> {
    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<DictVo>
     */
    PageInfo<DictVo> queryDictListByPage(DictQueryDto queryDto);

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return List<DictVo>
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
     * 根据字典KEY查询配置信息列表
     *
     * @param dictKey 字典KEY
     * @return List<NameValue>
     */
    List<NameValue> queryConfListByDictKey(String dictKey);

    /**
     * 根据字典KEY查询配置信息
     *
     * @param dictKey 字典KEY
     * @return VALUE
     */
    String queryDictValueByDictKey(String dictKey);
}
