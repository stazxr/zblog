package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.domain.entity.ArticleTag;
import com.github.stazxr.zblog.mapper.ArticleTagMapper;
import com.github.stazxr.zblog.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签业务实现层
 *
 * @author SunTao
 * @since 2021-01-17
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
