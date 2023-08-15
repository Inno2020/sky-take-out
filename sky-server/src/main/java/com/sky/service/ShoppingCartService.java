package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 根据id查询用户购物车
     * @param currentId
     * @return
     */
    List<ShoppingCart> list(Long currentId);


    /**
     * 清空购物车
     */
    void clean();


    /**
     * 删除购物车中内容
     * @param shoppingCartDTO
     */
    void delete(ShoppingCartDTO shoppingCartDTO);
}
