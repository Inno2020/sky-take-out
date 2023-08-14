package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 根据id查询套餐id
     * @param id
     * @return
     */
    List<Long> getSetmealIdsByDishIds(Long id);

    /**
     * 批量插入菜品
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);


    /**
     * 根据id查询套餐名
     * @param id
     * @return
     */
    @Select("select name from category where id = #{id}")
    String getCategoryById(Long id);

    /**
     * 根据setmealid查询dish
     * @param setmeal
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{id}")
    List<Dish> getBySetmealId(Setmeal setmeal);

    /**
     * 删除setmeal_dish表中数据
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);
}
