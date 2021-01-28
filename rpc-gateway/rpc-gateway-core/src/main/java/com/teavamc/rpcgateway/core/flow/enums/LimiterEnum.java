package com.teavamc.rpcgateway.core.flow.enums;

/**
 * 限流器枚举
 * @Package com.teavamc.rpcgateway.core.flow.enums
 * @date 2021/1/28 下午3:03
 */
public enum LimiterEnum {

    TOKEN_BUCKET(1,"TOKEN_BUCKET"),
    COUNT(2,"COUNT"),
    LEAKY_BUCKET(3,"LEAKY_BUCKET"),
    SLIDING_WINDOW(4,"SLIDING_WINDOW");

    private final Integer code;

    private final String name;

    LimiterEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }


    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public static LimiterEnum getLimiterByCode(Integer code){
        for (LimiterEnum iEnum:LimiterEnum.values()){
            if (code.equals(iEnum.getCode())){
                return iEnum;
            }
        }
        return null;
    }


}
