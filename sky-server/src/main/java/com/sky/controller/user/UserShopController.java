package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = "营业状态接口管理")
@Slf4j
@RestController("userShopController")
@RequestMapping("/user/shop")
public class UserShopController {
    public  final static String KEY="SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询营业状态
     * @return
     */
    @ApiOperation("查询营业状态")
    @GetMapping("/status")
    public Result getStatus(){
       Integer status= (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("查询营业状态{}",status==1?"营业中":"打烊中");
        return Result.success(status);
    }
}
