package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime outTime = LocalDateTime.now().plusMinutes(-15);
        // 获取已经过期的orders集合
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, outTime);
        if(list != null && !list.isEmpty()){
            log.info("本次发现 {} 单超时订单，开始处理", list.size());
            for (Orders orders : list) {
                log.info("取消订单：orderId={}, orderNumber={}", orders.getId(), orders.getNumber());
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason(MessageConstant.ORDER_OUT_TIME);
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
            log.info("本次共处理 {} 单超时订单", list.size());
        }else{
            log.info("本次未发现超时订单");
        }
    }

    /**
     * 处理状态一直为派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单：{}", LocalDateTime.now());
        // 处理12点之前处于派送中的订单
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusHours(-1));
        if(list != null && !list.isEmpty()){
            log.info("本次发现 {} 单还处于派送中的订单，开始处理", list.size());
            for (Orders orders : list) {
                log.info("取消订单：orderId={}, orderNumber={}", orders.getId(), orders.getNumber());
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
            log.info("本次共处理 {} 单还处于派送中的订单", list.size());
        }else{
            log.info("本次未发现还处于派送中的订单");
        }
    }

}
