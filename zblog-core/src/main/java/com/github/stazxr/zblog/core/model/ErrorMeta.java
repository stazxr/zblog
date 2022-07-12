package com.github.stazxr.zblog.core.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 封装错误元信息
 *
 * @author SunTao
 * @since 2022-07-11
 */
@Getter
public class ErrorMeta {
    /**
     * 发生错误的异常名称
     */
    private final String expName;

    /**
     * 错误描述
     */
    private final String expMsg;

    /**
     * 额外信息
     */
    @Setter
    private String remark;

    /**
     * 错误发生的具体地方
     */
    private String expAddress;

    public ErrorMeta(Throwable e) {
        this(e, "");
    }

    public ErrorMeta(Throwable e, String remark) {
        expName = e.getClass().getName();
        expMsg = e.getMessage();
        this.remark = remark;

        // 定位错误信息
        setExpAddress(e);
    }

    private void setExpAddress(Throwable e) {
        try {
            expAddress = e.getStackTrace()[0].toString();
        } catch (Exception e1) {
            expAddress = "错误定位失败：" + e1.getClass().getName().concat(":").concat(e1.getMessage());
        }
    }
}
