package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * ID生成管理器
 *
 * @author SunTao
 * @since 2021-12-19
 */
@RestController
@RequestMapping("/api/id")
@Api(value = "IdController", tags = { "ID控制器" })
public class IdController {
    /**
     * 生成唯一ID
     *
     * @return 唯一ID
     */
    @IgnoredLog
    @GetMapping("/getId")
    @ApiOperation(value = "生成唯一序列")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "生成唯一序列", code = "getId", level = BaseConst.PermLevel.PUBLIC)
    public Result getId() {
        try {
            return Result.success().data(GenerateIdUtils.getId());
        } catch (Exception e) {
            throw new ServiceException(ResultCode.ID_EXCEPTION, e);
        }
    }

    /**
     * 生成ID列表
     *
     * @param count 需要生成的序列数量
     * @return 唯一ID列表
     */
    @IgnoredLog
    @GetMapping("/getIds")
    @ApiOperation(value = "批量生成序列")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "count", value = "数量", required = true, dataTypeClass = Integer.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "批量生成序列", code = "getIds", level = BaseConst.PermLevel.PUBLIC)
    public Result getIds(@RequestParam("count") Integer count) {
        Integer tmpCount = count;
        if (tmpCount == null || tmpCount == 0) {
            tmpCount = 1;
        }
        try {
            return Result.success().data(GenerateIdUtils.getIdList(tmpCount));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.ID_EXCEPTION, e);
        }
    }
}
