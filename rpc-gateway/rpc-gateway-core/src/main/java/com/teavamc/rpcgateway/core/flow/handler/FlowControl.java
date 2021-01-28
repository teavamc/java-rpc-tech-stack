package com.teavamc.rpcgateway.core.flow.handler;

import com.teavamc.rpcgateway.core.flow.entity.FlowControlConfig;

/**
 * 流量控制的接口
 * @Package com.teavamc.rpcgateway.core.flow.handler
 * @date 2021/1/28 上午11:47
 */
public interface FlowControl {

    /**
     * 是否过滤
     * @param flowControlConfig
     * @return
     */
    boolean doFilter(FlowControlConfig flowControlConfig);

}
