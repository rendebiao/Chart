package com.rdb.chart.line;

/**
 * Created by DB on 2016/12/19 0019.
 */

public interface LineChartInterface {

    PointType getPointType(int linePosition);

    LineType getLineType(int linePosition);

    int getLineColor(int linePosition);
}
