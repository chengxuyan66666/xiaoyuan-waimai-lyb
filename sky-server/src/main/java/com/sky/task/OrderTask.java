package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

//@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOutOrder(){
       LocalDateTime time= java.time.LocalDateTime.now().plusMinutes(-15);
       log.info("处理超时未支付订单{}",LocalDateTime.now());
        List<Orders> ordersList=orderMapper.selectByStatusAndTimeTL(Orders.PENDING_PAYMENT,time);
        if(ordersList !=null && ordersList.size()>0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单支付时间超时，自动取消订单");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
    //@Scheduled(cron = "30 * * * * *")
    @Scheduled(cron = "0 0 1 * * ?")//凌晨一点执行
    public void processDeliveryOrder(){
        LocalDateTime time= java.time.LocalDateTime.now().plusMinutes(-60);
        log.info("处理正在派送中的订单{}",LocalDateTime.now());
        List<Orders> ordersList=orderMapper.selectByStatusAndTimeTL(Orders.DELIVERY_IN_PROGRESS,time);
        if(ordersList !=null && ordersList.size()>0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
