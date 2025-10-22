package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping
    @CacheEvict(cacheNames ="setmeal",key = "#setmealDTO.categoryId")
    public Result add(@RequestBody SetmealDTO setmealDTO)
    {
        log.info("新增套餐{}",setmealDTO);
        setmealService.add(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询套餐")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO)
    {
       log.info("分页查询套餐{}",setmealPageQueryDTO);
       PageResult p=setmealService.page(setmealPageQueryDTO);
       return Result.success(p);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @ApiOperation("删除套餐")
    @DeleteMapping
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    public Result delect(@RequestParam List<Long> ids)
    {
          log.info("删除套餐{}",ids);
          setmealService.delete(ids);
          return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @ApiOperation("根据id查询套餐")
    @GetMapping("{id}")
    public Result<SetmealVO> findById(@PathVariable Long id){
        log.info("根据id查询套餐{}",id);
        SetmealVO setmealVO=setmealService.findById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @ApiOperation("修改套餐")
    @PutMapping
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 起售停售
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("起售停售")
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    public Result startOrStop(@PathVariable Integer status,Long id)
    {
        log.info("修该售卖状态{},套餐id{}",status,id);
        setmealService.startOrStop(status,id);
        return Result.success();
    }
}
