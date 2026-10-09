package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入数据
     * @param detailList
     */
    void insertBatch(List<OrderDetail> detailList);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);

    /**
     * 统计已完成订单中菜品销量top10
     * @param beginTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> getDishSales(LocalDateTime beginTime, LocalDateTime endTime);
}
