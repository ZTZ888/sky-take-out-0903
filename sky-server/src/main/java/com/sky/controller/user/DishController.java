package com.sky.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.constant.RedisConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "C端-菜品浏览接口")
@RestController("userDishContorller")
@RequestMapping("/user/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) throws JsonProcessingException {
        // key:dish_+分类id
        String key = RedisConstant.DISH + categoryId;
        // 查询缓存，看数据在redis中是否存在
        log.info("正在查询分类:{}",key);
        String json = (String) redisTemplate.opsForValue().get(key);
        // 如果存在，直接返回
        if(json != null){
            log.info("在缓存中查询到了");
            List<DishVO> cacheList = objectMapper.readValue(json, new TypeReference<List<DishVO>>() {
            });
            return Result.success(cacheList);
        }
        // 如果不存在，查询数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<DishVO> list = dishService.listWithFlavors(dish);
        // 如果数据库无数据，返回错误
        if(list == null || list.isEmpty()){
            return Result.error("查询的数据不存在");
        }
        // 如果数据库中有数据，先返回数据，再存入缓存
        String listJson = objectMapper.writeValueAsString(list);
        log.info("在数据库中查到了，正在写入缓存...");
        redisTemplate.opsForValue().set(key, listJson);
        return Result.success(list);
    }
}
