package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api("种类管理")
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("修改分类")
    @PutMapping()
    public Result change(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类{}",categoryDTO);
        categoryService.change(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param  categoryPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询{}", categoryPageQueryDTO);
        PageResult pageResult=categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用、禁用分类")
    @PostMapping("/status/{status}")
    public Result startOrStop (@PathVariable Integer status,Long id)
    {
        log.info("启用、禁用分类,状态 {},id:{}",status,id);
        categoryService.startOrStop(status,id);
        return Result.success();
    }
    @ApiOperation("新增分类")
    @PostMapping()
    public Result add(@RequestBody CategoryDTO categoryDTO)
    {
        log.info("新增分类{}",categoryDTO);
        categoryService.add(categoryDTO);
        return Result.success();
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @ApiOperation("根据id删除分类")
    @DeleteMapping
    public Result delect(Long id)
    {
        log.info("根据id删除分类{}",id);
        categoryService.delect(id);
        return Result.success();
    }


    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result findType( Integer type)
    {
        log.info("根据类型查询分类{}", type);
        List<Category> categoryList=categoryService.findType(type);
        return Result.success(categoryList);
    }

}
