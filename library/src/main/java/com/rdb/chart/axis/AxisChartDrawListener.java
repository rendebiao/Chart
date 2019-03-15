package com.rdb.chart.axis;

import android.graphics.Canvas;

/**
 * Created by DB on 2016/12/22.
 */

public abstract class AxisChartDrawListener<T extends AxisChart> {

    public abstract void onDraw(Canvas canvas, T chart, AxisDrawProgress drawProgress);
}
