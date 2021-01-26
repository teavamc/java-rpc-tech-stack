package com.teavamc.rpcdatadao.api.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Package com.teavamc.rpcdatadao.api.model.request
 * @date 2021/1/26 下午3:02
 */
@Data
public class GetResourceAndLockSomeAWhileRequest implements Serializable {

    private static final long serialVersionUID = -2763318784544650332L;

    // lock key
    private String key;

}
