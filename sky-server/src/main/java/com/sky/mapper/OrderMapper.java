package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 插入数据
     * @param orders
     */
    void insert(Orders orders);

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

    /**
     * 分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查根据id查询order
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 根据状态情况查询订单数量
     * @param status
     * @return
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    /**
     * 根据状态还有下单时间查询订单是否过期
     * @param status
     * @param outTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{outTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime outTime);

    /**
     * 根据日期集合查询营业额集合
     * @param begin
     * @param end
     * @param status
     * @return
     */
    Double sumTurnoverByDate(
            @Param("begin") LocalDateTime begin,
            @Param("end") LocalDateTime end,
            @Param("status") Integer status
    );

    /**
     * 统计end之前的订单总数
     * @param end
     * @return
     */
    @Select("select count(id) from orders where order_time <= #{end}")
    Integer getTotalAmount(LocalDateTime end);

    /**
     * 统计每天的订单总数
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("select count(id) from orders where order_time >= #{beginTime} and order_time <= #{endTime}")
    Integer getDayAmount(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 统计end之前有效的订单总数
     * @param end
     * @return
     */
    @Select("select count(orders.id) from orders where order_time <= #{end} and status = 5")
    Integer getValidAmount(LocalDateTime end);

    /**
     * 统计每天的有效订单数
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("select count(id) from orders where order_time >= #{beginTime} and order_time <= #{endTime} and status = 5")
    Integer getDayValidAmount(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 根据动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
