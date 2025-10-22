package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping( "/admin/workspace")
@Slf4j
@Api("工作台相关接口")
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;
    /**
     * 查询今日运营数据
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("查询今日运营数据")
    public Result<BusinessDataVO> businessData(){
        LocalDateTime begin=LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end=LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        BusinessDataVO businessDataVO=workspaceService.getBusinessData(begin,end);
        log.info("查询今日运营数据{}",businessDataVO);
        return Result.success(businessDataVO);
    }

    /**
     * 查询订单管理数据
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单管理数据")
    public Result<OrderOverViewVO> overviewOrders(){
        OrderOverViewVO orderOverViewVO=workspaceService.getOverviewOrders();
        log.info("查询订单管理数据{}",orderOverViewVO);
        return Result.success(orderOverViewVO);
    }
    /**
     * 查询菜品总览
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("查询菜品总览")
    public Result<DishOverViewVO> overviewDishes(){
       DishOverViewVO dishOverViewVO=workspaceService.getOverviewDishes();

        log.info("查询菜品总览{}",dishOverViewVO);
        return Result.success(dishOverViewVO);
    }

    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("查询套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        SetmealOverViewVO setmealOverViewVO=workspaceService.getOverviewSetmeals();

        log.info("查询套餐总览{}",setmealOverViewVO);
        return Result.success(setmealOverViewVO);
    }

}
