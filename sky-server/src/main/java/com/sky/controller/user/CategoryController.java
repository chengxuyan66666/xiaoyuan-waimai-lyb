package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api("c端分类接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @ApiOperation("查询分类")
    @GetMapping("/list")
    public Result findCategory(Integer type){
        log.info("c端查询分类{}",type);
        List<Category> categoryList=categoryService.findType(type);
        return Result.success(categoryList);
    }
}
