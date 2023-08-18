package com.sky.Task;


import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void ProcessTimeoutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        // 超过15分钟的订单
        List<Orders> list= orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        // 遍历该订单
        if(list != null && list.size() > 0) {
            for(Orders orders : list) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单已超时");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理派送中订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void ProcessDeliveryOrder() {
        log.info("定时处理一直派送中订单：{}", LocalDateTime.now());

        // 超过15分钟的订单
        List<Orders> list= orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));

        // 遍历该订单
        if(list != null && list.size() > 0) {
            for(Orders orders : list) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
