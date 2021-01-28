package com.teavamc.rpcdatadao.service.test;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 测试日期的工具类
 * @Package com.teavamc.rpcdatadao.service.test
 * @date 2021/1/27 上午11:03
 */
@Slf4j
public class TestLocalTime {

    public static void main(String[] args) throws ParseException {
        // 获取目标区间的开始与结束日期
        boolean res = isEffectiveDate("2020-12-20","2021-03-03");
        System.out.println(res);
        return;
    }

    /**
     * 判断当前日期是否在生效日期的范围内
     * @param startDateStr 开始日期
     * @param endDateStr 结束日期
     * @return
     * @throws ParseException
     */
    public static boolean isEffectiveDate(String startDateStr, String endDateStr) throws ParseException {
        // 当前时间
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 开始时间 结束时间 字符串转 Date
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        // 当前时间是否在开始时间之后, 当前时间是否在结束时间之前
        return nowDate.after(startDate) && nowDate.before(endDate);
    }


}
