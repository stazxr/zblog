package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.exception.ServiceException;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.error.SequenceErrorCode;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 序号控制器
 *
 * @author SunTao
 * @since 2026-09-16
 */
@RestController
@RequestMapping("/api/sequence")
@Api(value = "SequenceController", tags = { "序号控制器" })
public class SequenceController {
    /**
     * 生成唯一序列
     *
     * @return Long
     */
    @GetMapping("/getId")
    @ApiOperation(value = "生成唯一序列")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "生成唯一序列", code = "SEQUA001", level = RouterLevel.PUBLIC)
    public Long getId() {
        try {
            return SequenceUtils.getId();
        } catch (Exception e) {
            throw new ServiceException(SequenceErrorCode.SSEQUA000, e);
        }
    }

    /**
     * 生成唯一序列列表
     *
     * @param count 需要生成的序列数量
     * @return List<Long>
     */
    @GetMapping("/getIds")
    @ApiOperation(value = "生成唯一序列列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "count", value = "序列数量", required = true, dataTypeClass = Integer.class)
    })
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "生成唯一序列列表", code = "SEQUA002", level = RouterLevel.PUBLIC)
    public List<Long> getIds(@RequestParam("count") Integer count) {
        Integer tmpCount = count;
        if (tmpCount == null) {
            tmpCount = 1;
        }
        try {
            return SequenceUtils.getIdList(tmpCount);
        } catch (Exception e) {
            throw new ServiceException(SequenceErrorCode.SSEQUA000, e);
        }
    }
}
