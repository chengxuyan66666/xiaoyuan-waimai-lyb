package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    Page<Orders> select(Orders orders);

    Page<Orders> selectByAdmin(OrdersPageQueryDTO ordersPageQueryDTO);


    @Select("select * from orders where id=#{id}")
    Orders selectById(Long id);

    @Select("select * from orders where status=#{status} and order_time<#{time}")
    List<Orders> selectByStatusAndTimeTL(Integer status, LocalDateTime time);

    /**
     * 各个状态的订单数量统计
     * @param status
     * @return
     */

    Integer selectByStatus(Integer status,LocalDateTime begin);

    /**
     * 计算机营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 统计每天订单数量
     * @param map
     */
    Integer countByMap(Map map);

    /**
     * 获取给定时间范围内销量排名前十
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getTop10(LocalDateTime begin, LocalDateTime end);


}
