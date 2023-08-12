package com.sky.mapper;

import com.sky.entity.Setmeal;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
