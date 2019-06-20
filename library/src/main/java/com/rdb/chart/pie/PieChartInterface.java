package com.rdb.chart.pie;

/**
 * Created by DB on 2016/12/19 0019.
 */

public interface PieChartInterface {

    int getCount();

    float getValue(int position);

    int getColor(int position);

    int getSelectedColor(int position);

    String getText(int position);
}
