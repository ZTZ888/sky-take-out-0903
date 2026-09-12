package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.result.Result;

/**
 * 菜品类相关业务
 */

public interface DishService {
    /**
     * 新增菜品及其口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);
}
