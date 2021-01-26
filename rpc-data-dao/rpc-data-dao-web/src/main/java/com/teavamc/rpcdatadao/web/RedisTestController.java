package com.teavamc.rpcdatadao.web;

import com.teavamc.rpcdatadao.api.model.request.GetResourceAndLockSomeAWhileRequest;
import com.teavamc.rpcdatadao.service.service.RedisTestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Package com.teavamc.rpcdatadao.web
 * @date 2021/1/26 下午2:20
 */
@RestController
@RequestMapping(value = "redisDemo")
public class RedisTestController {

    @Resource
    private RedisTestService redisTestService;

    // 模拟用户请求, 请求之后需要加锁, 实现一段时间内不能重复请求
    @PostMapping(value = "/getResourceAndLock")
    public String  GetResourceAndLockSomeAWhile(@RequestBody GetResourceAndLockSomeAWhileRequest request){
        if (redisTestService.GetResourceAndLockSomeAWhile(request.getKey())){
            return "成功";
        }else{
            return "失败";
        }
    }
}
