package com.github.stazxr.muses.utils.mask;

import com.github.stazxr.muses.utils.mask.core.FieldMask;

import java.util.List;
import java.util.Map;

public class MaskVo {
    private String username;

    @FieldMask(type = MaskType.PASSWORD)
    private String password;

    private String email;

    private MaskSubVo subVo;

    @FieldMask
    private Map<String, Object> map;

    @FieldMask
    private List<Object> list;

    public MaskVo(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MaskSubVo getSubVo() {
        return subVo;
    }

    public void setSubVo(MaskSubVo subVo) {
        this.subVo = subVo;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}
