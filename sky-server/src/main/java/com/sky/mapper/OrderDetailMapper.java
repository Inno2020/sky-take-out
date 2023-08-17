package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单明细数据
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 根据id查询订单详情
     * @param ordersId
     * @return
     */
    @Select("select * from order_detail where order_id = #{ordersId}")
    List<OrderDetail> getById(Long ordersId);


    /**
     * 根据订单号查询订单详情
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id =#{id}")
    List<OrderDetail> getByOrderId(Long id);
}
