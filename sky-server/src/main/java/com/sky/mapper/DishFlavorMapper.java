package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    public void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void delete(Long id);

    /**
     * 根据id集合批量删除口味
     * @param ids
     */
    void deleteByIdList(List<Long> ids);

    /**
     * 根据id查询口味集合
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_flavor.dish_id = #{id}")
    List<DishFlavor> getById(Long id);
}
