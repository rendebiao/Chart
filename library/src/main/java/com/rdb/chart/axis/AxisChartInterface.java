package com.rdb.chart.axis;

import com.rdb.chart.ChartIndicator;

import java.util.List;

/**
 * Created by DB on 2016/12/19 0019.
 */

public interface AxisChartInterface {

    int getMaxPageCount();

    int getXAxisTextCount();

    int getYAxisValueCount();

    String getXAxisText(int position);

    float getYAxisMinValue();

    float getYAxisMaxValue();

    int getGroupCount();

    float getValue(int groupPosition, int xAxisPosition);

    String getGroupName(int groupPosition);

    List<ChartIndicator> getChartIndicators();
}
