package com.sky.service;

import com.sky.vo.*;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 获取营业额统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end);

    /**
     *
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderReport(LocalDate begin, LocalDate end);

    /**
     * top10统计
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end);
}
