package com.thirty.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 基础图表VO类
 * @param <X> X轴数据类型
 * @param <Y> Y轴数据类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseChartVO<X, Y> {
    private List<X> x;
    private List<Y> y;
}
