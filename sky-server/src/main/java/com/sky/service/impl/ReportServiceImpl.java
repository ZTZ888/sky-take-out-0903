package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 获取营业额数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = new ArrayList<>();
        List<Double> turnOverList = new ArrayList<>();

        LocalDate day = begin;
        while (!day.isAfter(end)) {
            dateList.add(day);

            LocalDateTime beginTime = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(day, LocalTime.MAX);

            Double turnover = orderMapper.sumTurnoverByDate(beginTime, endTime, Orders.COMPLETED);
            turnOverList.add(turnover == null ? 0.0 : turnover);

            day = day.plusDays(1);
        }

        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(StringUtils.join(dateList, ","));
        turnoverReportVO.setTurnoverList(StringUtils.join(turnOverList, ","));
        return turnoverReportVO;
    }

    /**
     * 统计指定日期内的用户数量
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();
        List<Integer> addList = new ArrayList<>();
        LocalDate day = begin;
        while(!day.isAfter(end)){
            LocalDateTime r = LocalDateTime.of(day,LocalTime.MAX);

            Integer amountAdd = 0;
            Integer userAmount = userMapper.getAmount(r);
            if(amountList.isEmpty()){
                amountAdd = userAmount;
            }else{
                amountAdd = userAmount - amountList.get(amountList.size() - 1);
            }

            amountList.add(userAmount);
            addList.add(amountAdd);
            dateList.add(day);

            day = day.plusDays(1);
        }
        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setDateList(StringUtils.join(dateList, ","));
        userReportVO.setTotalUserList(StringUtils.join(amountList, ","));
        userReportVO.setNewUserList(StringUtils.join(addList, ","));
        return userReportVO;
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> dayAmountList = new ArrayList<>();
        List<Integer> dayValidAmountList = new ArrayList<>();

        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MAX);
        Integer orderTotal = orderMapper.getTotalAmount(endDateTime);
        Integer orderValid = orderMapper.getValidAmount(endDateTime);
        Double orderCompletionRate = orderTotal == 0?0:(double) orderValid / orderTotal;

        LocalDate day = begin;
        while(!day.isAfter(end)){
            dateList.add(day);

            LocalDateTime beginTime = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(day, LocalTime.MAX);
            Integer dayAmount = orderMapper.getDayAmount(beginTime, endTime);
            Integer dayValidAmount = orderMapper.getDayValidAmount(beginTime, endTime);
            dayAmountList.add(dayAmount);
            dayValidAmountList.add(dayValidAmount);

            day = day.plusDays(1);
        }

        OrderReportVO orderReportVO = new OrderReportVO();
        orderReportVO.setDateList(StringUtils.join(dateList, ","));
        orderReportVO.setTotalOrderCount(orderTotal);
        orderReportVO.setValidOrderCount(orderValid);
        orderReportVO.setOrderCompletionRate(orderCompletionRate);
        orderReportVO.setOrderCountList(StringUtils.join(dayAmountList, ","));
        orderReportVO.setValidOrderCountList(StringUtils.join(dayValidAmountList, ","));

        return orderReportVO;
    }

    /**
     * top10统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 名称 + 销量
        List<GoodsSalesDTO> goodsList = orderDetailMapper.getDishSales(beginTime, endTime);

        List<String> nameList = new ArrayList<>();
        List<Integer> salesList = new ArrayList<>();

        for (GoodsSalesDTO dto : goodsList) {
            String name = dto.getName();
            nameList.add(name);
            Integer sale = dto.getNumber();
            salesList.add(sale);
        }

        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList(StringUtils.join(nameList, ","));
        salesTop10ReportVO.setNumberList(StringUtils.join(salesList, ","));

        return salesTop10ReportVO;
    }


}
