package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        // 向setmeal表中添加数据
        // TODO 如何将status插入
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.builder().status(StatusConstant.ENABLE).build();
        setmealMapper.save(setmeal);

        //获取生成的套餐id
        Long setmealId = setmealDTO.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        // 获得套餐基本信息
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        // 获得套餐类别信息
        String categroy = setmealDishMapper.getCategroyById(setmeal.getCategoryId());
        setmealVO.setCategoryName(categroy);
        return setmealVO;
    }


    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新setmeal表中的数据
        setmealMapper.update(setmeal);
        // 更新setmeal_dish表中的数据
        Long setmealId = setmealDTO.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 批量删除套惨
     * @param ids
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 删除套餐前先判断是否有套餐是否正在起售，起售则不能删除
        for(Long id : ids) {
            // 根据id查询是否起售
            Setmeal setmeal = new Setmeal();
            setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.DISABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        for(Long id : ids) {
            // 删除setmeal表中数据
            setmealMapper.deleteById(id);
            // 删除setmeal_dish表中数据
            setmealDishMapper.deleteBySetmealId(id);
        }
    }


    /**
     * 套餐起售、停售
     * @param status
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        // 将其封装为setmeal对象
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        // 如果要切换为起售状态，判断其套餐内是否包含停售的菜品，如果存在则不能起售
        if(status == StatusConstant.ENABLE) {
            // 判断是否存在停售的菜品，根据id进行查询
            List<Dish> list = setmealDishMapper.getBySetmealId(setmeal);
            // 遍历list判断是否有状态为禁售的菜品
            if(list != null && list.size() > 0) {
                list.forEach(dish -> {
                    if(StatusConstant.ENABLE == dish.getStatus()) {
                        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }

        }
        setmealMapper.changeStatus(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
