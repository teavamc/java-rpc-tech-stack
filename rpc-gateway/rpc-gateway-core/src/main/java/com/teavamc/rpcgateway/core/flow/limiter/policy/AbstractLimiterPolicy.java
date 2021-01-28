package com.teavamc.rpcgateway.core.flow.limiter.policy;

import com.teavamc.rpcgateway.core.flow.entity.FlowControlConfig;
import com.teavamc.rpcgateway.core.flow.enums.LimiterEnum;
import org.springframework.stereotype.Component;

/**
 * @Package com.teavamc.rpcgateway.core.flow.limiter.policy
 * @date 2021/1/28 下午4:28
 */
public abstract class AbstractLimiterPolicy implements  LimiterPolicy{

    @Override
    public String[] toParams() {
        return new String[0];
    }


}
