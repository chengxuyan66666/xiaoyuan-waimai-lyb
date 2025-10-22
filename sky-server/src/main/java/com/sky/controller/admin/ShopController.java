package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Api(tags = "营业状态接口")
@Slf4j
@RestController
@RequestMapping("/admin/shop")
public class ShopController {
    public  final static String KEY="SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置营业状态
     * @return
     */
    @ApiOperation("设置营业状态")
    @PutMapping("/{status}")
    public Result getStatus(@PathVariable Integer status){
        log.info("设置营业状态{}",status==1?"营业中":"打烊中");

        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }
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
