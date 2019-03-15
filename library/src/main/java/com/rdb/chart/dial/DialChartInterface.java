package com.rdb.chart.dial;

/**
 * Created by DB on 2016/12/19 0019.
 */

public interface DialChartInterface {

    float getMinValue();

    float getMaxValue();

    float getCurValue();

    float getTotalDegree();

    boolean showScaleText(int largeScalePosition);
}
