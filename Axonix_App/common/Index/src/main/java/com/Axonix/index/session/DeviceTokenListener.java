package com.Axonix.index.session;

public interface DeviceTokenListener {
    /**
     * 当设备 token 获取到时调用此方法
     * @param token 获取到的 token，若获取失败则为 null
     */
    void onTokenReceived(String token);
}