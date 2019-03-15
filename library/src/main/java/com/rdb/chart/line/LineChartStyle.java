package com.rdb.chart.line;

import com.rdb.chart.axis.AxisChartStyle;

/**
 * Created by DB on 2016/12/20.
 */

public class LineChartStyle extends AxisChartStyle {


    private boolean filled;//是否填充折现下方区域
    private float pointRadius = 2f;//点的半径

    public LineChartStyle() {
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
    }

    @Override
    public void setStartFromOrigin(boolean show) {
        super.setStartFromOrigin(show);
    }
}
