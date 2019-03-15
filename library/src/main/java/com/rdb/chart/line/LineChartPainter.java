package com.rdb.chart.line;

import android.graphics.Canvas;

/**
 * Created by DB on 2016/12/22.
 */

public abstract class LineChartPainter {
    protected abstract void onDraw(Canvas canvas, LineChart lineChart, LineChartAdapter adapter);
}
