package com.github.stazxr.zblog.mask;

import com.github.stazxr.zblog.mask.core.FieldMask;

public class MaskSubVo {
    private String username;

    @FieldMask(type = MaskType.QQ_NUMBER)
    private String qq;

    public MaskSubVo(String username, String qq) {
        this.username = username;
        this.qq = qq;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
