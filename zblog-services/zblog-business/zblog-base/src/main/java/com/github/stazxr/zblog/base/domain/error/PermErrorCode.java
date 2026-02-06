package com.github.stazxr.zblog.base.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 权限错误码定义。
 *
 * @author SunTao
 * @since 2026-02-06
 */
public enum PermErrorCode implements ErrorCode {
    /** 菜单可见不能为空 */
    EPERMA000("PERM_HIDDEN_REQUIRED"),
    /** 菜单缓存不能为空 */
    EPERMA001("PERM_CACHEABLE_REQUIRED"),
    /** 权限路由地址不能为空 */
    EPERMA002("PERM_ROUTERPATH_REQUIRED"),
    /** 路由地址格式不正确 */
    EPERMA003("PERM_ROUTERPATH_PATTERN_INVALID"),
    /** 路由地址已经存在 */
    EPERMA004("PERM_ROUTERPATH_EXISTS"),
    /** 权限外链地址不能为空 */
    EPERMA005("PERM_LINKPATH_REQUIRED"),
    /** 外链地址格式不正确 */
    EPERMA006("PERM_LINKPATH_PATTERN_INVALID"),
    /** 权限编码不能为空 */
    EPERMA007("PERM_PERMCODE_REQUIRED"),
    /** 权限编码已存在 */
    EPERMA008("PERM_PERMCODE_EXISTS"),
    /** 权限组件名称不能为空 */
    EPERMA009("PERM_COMPONENTNAME_REQUIRED"),
    /** 权限组件路径不能为空 */
    EPERMA010("PERM_COMPONENTPATH_REQUIRED"),
    /** 目录的上级只能是目录 */
    EPERMA011("PERM_DIR_PARENT"),
    /** 外链的上级只能是目录 */
    EPERMA012("PERM_LINK_PARENT"),
    /** 菜单的上级只能是目录 */
    EPERMA013("PERM_MENU_PARENT"),
    /** 按钮的上级只能是目录或菜单 */
    EPERMA014("PERM_BTN_PARENT"),
    /** 上级权限不存在 */
    EPERMA015("PERM_PARENT_NOT_EXISTS"),
    /** 权限类型不允许编辑 */
    EPERMA016("PERM_PERMTYPE_EDIT_DISABLED"),
    /** 存在子节点，无法被删除 */
    EPERMA017("PERM_DELETE_WITH_CHILDREN");

    private final String i18nKey;

    PermErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码，唯一且符合规范
     *
     * @return 错误码字符串
     */
    @Override
    public String getCode() {
        return name();
    }

    /**
     * 国际化 key，用于获取对应消息
     *
     * @return i18n key
     */
    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}
