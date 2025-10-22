package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceImpl implements WorkspaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 查询今日运营数据
     *
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        //总订单树
        Integer totalOrders = orderMapper.countByMap(map);
        //新增用户
        Integer newUsers = userMapper.countByMap(map);
        //营业额
        map.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.sumByMap(map);
        turnover=turnover==null?0.0:turnover;
        //有效订单
        Integer validOrderCount = orderMapper.countByMap(map);
        //订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrders != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrders;
        }
        // 	平均客单价

        Double unitPrice = 0.0;
        if (validOrderCount != 0) {
            unitPrice = turnover / validOrderCount;
        }
        return BusinessDataVO.builder()
                .turnover(turnover)
                .newUsers(newUsers)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .validOrderCount(validOrderCount)
                .build();
    }

    /**
     * 查询订单管理数据
     * @return
     */
    //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
    public OrderOverViewVO getOverviewOrders() {
        LocalDateTime begin=LocalDateTime.now().with(LocalTime.MIN);
        Integer  allOrders=orderMapper.selectByStatus(null,begin);
       Integer  waitingOrders= orderMapper.selectByStatus(Orders.TO_BE_CONFIRMED,begin);
       Integer   deliveredOrders= orderMapper.selectByStatus(Orders.CONFIRMED,begin);
       Integer  completedOrders= orderMapper.selectByStatus(Orders.COMPLETED,begin);
       Integer  cancelledOrders= orderMapper.selectByStatus(Orders.CANCELLED,begin);
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }

    /**
     * 查询菜品总览
     * @return
     */
    public DishOverViewVO getOverviewDishes() {
       Integer sold=dishMapper.countByStatus(StatusConstant.ENABLE);
       Integer discontinued=dishMapper.countByStatus(StatusConstant.DISABLE);
       return DishOverViewVO.builder()
               .discontinued(discontinued)
               .sold(sold)
               .build();
    }

    /**
     * 查询套餐总览
     * @return
     */
    public SetmealOverViewVO getOverviewSetmeals() {
        Integer sold=setmealMapper.countByStatus(StatusConstant.ENABLE);
        Integer discontinued=setmealMapper.countByStatus(StatusConstant.DISABLE);
        return SetmealOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }
}
