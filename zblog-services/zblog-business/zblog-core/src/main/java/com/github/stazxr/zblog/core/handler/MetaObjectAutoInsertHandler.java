package com.github.stazxr.zblog.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自定义实现 MetaObjectHandler 元对象处理器接口
 *
 * @author SunTao
 * @since 2021-06-28
 */
@Component
public class MetaObjectAutoInsertHandler implements MetaObjectHandler {
    /**
     * 插入时自动填充创建用户
     */
    private static final String CREATE_USER = "createUser";

    /**
     * 插入时自动填充创建时间
     */
    private static final String CREATE_TIME = "createTime";

    /**
     * 修改时自动填充修改用户
     */
    private static final String UPDATE_USER = "updateUser";

    /**
     * 修改时自动填充修改时间
     */
    private static final String UPDATE_TIME = "updateTime";

    /**
     * 默认日期编码
     */
    private static final String TIME_PATTERN = DateUtils.YMD_HMS_PATTERN;

    @Override
    public void insertFill(MetaObject metaObject) {
        if (getFieldValByName(CREATE_USER, metaObject) == null) {
            this.strictInsertFill(metaObject, CREATE_USER, Long.class, SecurityUtils.getLoginIdWithoutNull());
        }

        if (getFieldValByName(CREATE_TIME, metaObject) == null) {
            this.strictInsertFill(metaObject, CREATE_TIME, String.class, DateUtils.format(new Date(), TIME_PATTERN));
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (getFieldValByName(UPDATE_USER, metaObject) == null) {
            this.strictUpdateFill(metaObject, UPDATE_USER, Long.class, SecurityUtils.getLoginIdWithoutNull());
        }

        if (getFieldValByName(UPDATE_TIME, metaObject) == null) {
            this.strictUpdateFill(metaObject, UPDATE_TIME, String.class, DateUtils.format(new Date(), TIME_PATTERN));
        }
    }
}
