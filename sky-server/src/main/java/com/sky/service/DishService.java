package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * 菜品类相关业务
 */

public interface DishService {
    /**
     * 新增菜品及其口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据菜品id查询菜品和对应的口味信息
     * @param id
     */
    DishVO getById(Long id);

    /**
     * 修改菜品及其相关口味
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 根据菜品中的分类id查询菜品
     * @param categoryId
     */
    List<Dish> list(long categoryId);

    /**
     * 根据菜品中的分类id查询菜品及口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavors(Dish dish);
}
