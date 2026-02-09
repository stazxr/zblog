package com.github.stazxr.zblog.bas.ratelimit.core;

/**
 * 限流规则
 *
 * @author SunTao
 * @since 2025-02-08
 */
public class RateLimitRule {
    // 时间窗口（秒）
    private int time;

    // 最大请求次数
    private int count;

    // 是否启用 IP 限流
    private boolean enableIp;

    // 是否启用用户限流
    private boolean enableUser;

    // 是否启用接口限流
    private boolean enableApi;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isEnableIp() {
        return enableIp;
    }

    public void setEnableIp(boolean enableIp) {
        this.enableIp = enableIp;
    }

    public boolean isEnableUser() {
        return enableUser;
    }

    public void setEnableUser(boolean enableUser) {
        this.enableUser = enableUser;
    }

    public boolean isEnableApi() {
        return enableApi;
    }

    public void setEnableApi(boolean enableApi) {
        this.enableApi = enableApi;
    }
}
