package com.github.stazxr.zblog.bas.exception.code;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;

/**
 * 错误码接口
 *
 * <p>所有业务模块错误码枚举必须实现该接口</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public interface ErrorCode {
    /**
     * 兜底技术异常错误码
     */
    String DEFAULT_SYSTEM_ERROR_CODE = "SBASEA000";

    /**
     * 错误码（唯一且符合统一规范）
     *
     * <p>错误码由以下几部分组成：</p>
     *
     * <ul>
     *   <li>
     *     <b>第一部分：错误码类型（1 位）</b>
     *     <ul>
     *       <li><b>E</b>：业务异常（可预期、语义类错误）</li>
     *       <li><b>S</b>：技术异常（系统级、不可预期错误）</li>
     *     </ul>
     *   </li>
     *
     *   <li>
     *     <b>第二部分：错误码子类型（2 位，可选）</b>
     *     <ul>
     *       <li>范围：<code>AA - ZZ</code></li>
     *       <li>示例：数据重复DD、权限相关PE、参数相关PA...</li>
     *     </ul>
     *   </li>
     *
     *   <li>
     *     <b>第三部分：系统 / 模块标识（4 位）</b>
     *     <ul>
     *       <li>范围：<code>AAAA - ZZZZ</code></li>
     *       <li>示例：<code>USER</code>、<code>ROLE</code>、<code>AUTH</code></li>
     *     </ul>
     *   </li>
     *
     *   <li>
     *     <b>第四部分：错误码分组（1 位）</b>
     *     <ul>
     *       <li>范围：<code>A - Z</code></li>
     *       <li>用于错误分组或场景区分</li>
     *     </ul>
     *   </li>
     *
     *   <li>
     *     <b>第五部分：错误码序号（3 位）</b>
     *     <ul>
     *       <li>范围：<code>000 - 999</code></li>
     *       <li>同一分组内递增</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p><b>示例：</b></p>
     * <pre>
     * SUSERA001  系统级 · 用户模块 · A 组 · 第 001 号错误
     * EAUTHB003  业务级 · 认证模块 · B 组 · 第 003 号错误
     * </pre>
     *
     * <p>错误码需通过 {@link ErrorCodeRule} 校验后方可使用</p>
     *
     * @see ErrorCodeRule
     * @return 错误码字符串
     */
    String getCode();

    /**
     * 国际化 key，用于获取对应消息
     *
     * @return i18n key
     */
    String getI18nKey();

    /**
     * 获取国际化消息
     */
    default String getMessage(Object... args) {
        return I18nUtils.getMessage(getI18nKey(), args, getI18nKey());
    }
}
