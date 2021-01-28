package com.teavamc.rpcgateway.core.flow.entity;

import com.teavamc.rpcgateway.core.flow.enums.LimiterEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 频控配置
 * @Package com.teavamc.rpcgateway.core.flow.entity
 * @date 2021/1/28 下午2:14
 */
@Data
public class FlowControlConfig implements Serializable {

    private static final long serialVersionUID = -7561574415662210537L;
    /**
     * 配置 id
     */
    private Long id;

    /**
     *  API 的 Id
     */
    private Long apiId;

    /**
     * API 名称
     */
    private String apiName;

    /**
     * 调用方的 ip
     */
    private String ip;

    /**
     * 限流的维度, 等于是 key
     * APP_ID / CLIENT_ID
     */
    private String dimensionName;

    /**
     * 限流的值
     */
    private String dimensionValue;

    /**
     * 限流类型
     */
    private Integer LimiterType;

    /**
     * 限流的单位 秒/分钟/小时/天
     */
    private Integer timeUnit;

    /**
     * 限流的阈值
     * 对于计数器 - 单位时间内的限制数
     * 对于令牌桶 - 单位时间内桶的最大值
     */
    private Long value;

}
