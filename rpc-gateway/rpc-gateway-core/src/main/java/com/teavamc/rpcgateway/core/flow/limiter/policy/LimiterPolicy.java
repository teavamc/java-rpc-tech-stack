package com.teavamc.rpcgateway.core.flow.limiter.policy;

import com.teavamc.rpcgateway.core.flow.entity.FlowControlConfig;

/**
 * @Package com.teavamc.rpcgateway.core.limiter
 * @date 2021/1/28 上午10:27
 */
public interface LimiterPolicy {

    /**
     * 转成字符串数组
     * @return
     */
    String[] toParams ();

}
