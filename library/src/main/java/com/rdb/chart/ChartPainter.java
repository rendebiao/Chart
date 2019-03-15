package com.rdb.chart;

import android.graphics.Canvas;

/**
 * Created by DB on 2016/12/22.
 */

public abstract class ChartPainter<T extends Chart> {

    public abstract void onDraw(Canvas canvas, T chart);
}
