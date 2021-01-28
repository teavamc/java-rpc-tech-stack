package com.teavamc.rpcgateway.core.utils;

import com.teavamc.rpcgateway.core.flow.enums.LimiterTimeUnitEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Package com.teavamc.rpcgateway.core.utils
 * @date 2021/1/28 下午4:14
 */
public class TimeUtil {

    public static long getTimeDistance(String baseTime, LimiterTimeUnitEnum type) {
        long nowTime = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long result = 0;
        try {
            Date d1 = df.parse(baseTime);
            long diff = nowTime - d1.getTime();
            switch (type) {
                case second:
                    result = diff / 1000;
                    break;
                case min:
                    result = diff / (60 * 1000);
                    break;
                case hour:
                    result = diff / (60 * 60 * 1000);
                    break;
                case day:
                    result = diff / (60 * 60 * 24 * 1000);
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static long getTimeDistance(LimiterTimeUnitEnum type) {
        long nowTime = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long result = 0;
        try {
            Date d1 = df.parse("1970-01-01 00:00:00");
            long diff = nowTime - d1.getTime();
            switch (type) {
                case second:
                    result = diff / 1000;
                    break;
                case min:
                    result = diff / (60 * 1000);
                    break;
                case hour:
                    result = diff / (60 * 60 * 1000);
                    break;
                case day:
                    result = diff / (60 * 60 * 24 * 1000);
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static long getTimeDistanceExpired(LimiterTimeUnitEnum type) {
        long nowTime = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long result = 0;
        try {
            Date d1 = df.parse("1970-01-01 00:00:00");
            long diff = nowTime - d1.getTime();
            switch (type) {
                case second:
                    result = diff / 1000 - 1;
                    break;
                case min:
                    result = diff / (60 * 1000) - 1;
                    break;
                case hour:
                    result = diff / (60 * 60 * 1000) - 1;
                    break;
                case day:
                    result = diff / (60 * 60 * 24 * 1000) - 1;
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public static long getMillisecond(LimiterTimeUnitEnum type) {
        long result = 0;
        switch (type) {
            case second:
                result = 1000;
                break;
            case min:
                result = 60000;
                break;
            case hour:
                result = 3600000;
                break;
            case day:
                result = 86400000;
                break;
            default:
                break;
        }
        return result;
    }
}
