package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.content.ext.domain.dto.SensitiveWordDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.SensitiveWordQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.SensitiveWord;
import com.github.stazxr.zblog.content.ext.domain.error.SensitiveWordErrorCode;
import com.github.stazxr.zblog.content.ext.domain.vo.SensitiveWordVo;
import com.github.stazxr.zblog.content.ext.mapper.SensitiveWordExtMapper;
import com.github.stazxr.zblog.content.ext.service.SensitiveWordService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 敏感词管理业务实现层
 *
 * @author SunTao
 * @since 2026-07-23
 */
@Service
@RequiredArgsConstructor
public class SensitiveWordServiceImpl implements SensitiveWordService {
    private final SensitiveWordExtMapper sensitiveWordExtMapper;

    /**
     * 分页查询敏感词列表
     *
     * @param queryDto 查询参数
     * @return IPage<SensitiveWordVo>
     */
    @Override
    public IPage<SensitiveWordVo> querySensitiveWordListByPage(SensitiveWordQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getWord())) {
            queryDto.setWord(queryDto.getWord().trim());
        }

        // 分页查询
        Page<SensitiveWordVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return sensitiveWordExtMapper.selectSensitiveWordList(page, queryDto);
    }

    /**
     * 新增敏感词
     *
     * @param sensitiveWordDto 敏感词信息
     */
    @Override
    public void addSensitiveWord(SensitiveWordDto sensitiveWordDto) {
        // 获取敏感词信息
        SensitiveWord sensitiveWord = dtoToEntity(sensitiveWordDto);
        // 新增时，不允许传入 SensitiveWordId
        ThrowUtils.when(sensitiveWord.getId() != null).system(BaseErrorCode.SCOREB001);
        // 敏感词信息检查
        checkSensitiveWord(sensitiveWord);
        sensitiveWord.setId(SequenceUtils.getId());
        sensitiveWord.setCreateUser(SecurityUtils.getLoginId());
        sensitiveWord.setCreateTime(LocalDateTime.now());
        // 新增敏感词
        ThrowUtils.when(sensitiveWordExtMapper.insert(sensitiveWord) != 1).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑敏感词
     *
     * @param sensitiveWordDto 敏感词信息
     */
    @Override
    public void editSensitiveWord(SensitiveWordDto sensitiveWordDto) {
        // 获取敏感词信息
        SensitiveWord sensitiveWord = dtoToEntity(sensitiveWordDto);
        // 判断敏感词是否存在
        SensitiveWord dbSensitiveWord = sensitiveWordExtMapper.selectById(sensitiveWord.getId());
        ThrowUtils.throwIfNull(dbSensitiveWord, BaseErrorCode.ECOREA001);
        // 敏感词信息检查
        checkSensitiveWord(sensitiveWord);
        sensitiveWord.setUpdateUser(SecurityUtils.getLoginId());
        sensitiveWord.setUpdateTime(LocalDateTime.now());
        // 编辑敏感词
        ThrowUtils.when(sensitiveWordExtMapper.updateById(sensitiveWord) != 1).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除敏感词
     *
     * @param sensitiveWordId 敏感词id
     */
    @Override
    public void deleteSensitiveWord(Long sensitiveWordId) {
        sensitiveWordExtMapper.deleteById(sensitiveWordId);
    }

    private void checkSensitiveWord(SensitiveWord sensitiveWord) {
        // 敏感词不能重复
        sensitiveWord.setWord(sensitiveWord.getWord().trim());
        ThrowUtils.throwIf(checkSensitiveWordExist(sensitiveWord), SensitiveWordErrorCode.ESEWDA001);
    }

    private boolean checkSensitiveWordExist(SensitiveWord sensitiveWord) {
        if (sensitiveWord.getWord() != null) {
            LambdaQueryWrapper<SensitiveWord> queryWrapper = queryBuild().eq(SensitiveWord::getWord, sensitiveWord.getWord());
            if (sensitiveWord.getId() != null) {
                queryWrapper.ne(SensitiveWord::getId, sensitiveWord.getId());
            }
            return sensitiveWordExtMapper.exists(queryWrapper);
        }
        return false;
    }

    private SensitiveWord dtoToEntity(SensitiveWordDto sensitiveWordDto) {
        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setId(sensitiveWordDto.getId());
        sensitiveWord.setWord(sensitiveWordDto.getWord());
        sensitiveWord.setStatus(sensitiveWordDto.getStatus());
        sensitiveWord.setRemark(sensitiveWordDto.getRemark());
        return sensitiveWord;
    }

    private LambdaQueryWrapper<SensitiveWord> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
