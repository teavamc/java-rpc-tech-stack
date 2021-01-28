package com.teavamc.rpcgateway.core.flow.enums;

/**
 * 流控限流器的作用单位
 *
 * @Package com.teavamc.rpcgateway.core.flow.entity
 * @date 2021/1/28 下午4:14
 */
public enum LimiterTimeUnitEnum {
    second,
    min,
    hour,
    day;

    public static LimiterTimeUnitEnum valueOf(Integer value) {
        if (null == value) {
            return min;
        }
        switch (value) {
            case 0:
                return second;
            case 1:
                return min;
            case 2:
                return hour;
            case 3:
                return day;
            default:
                return min;
        }
    }
}
