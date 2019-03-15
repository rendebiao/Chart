package com.rdb.chart;

import android.graphics.Color;

/**
 * Created by DB on 2016/12/20.
 */

public abstract class ChartStyle implements Cloneable {

    public static int DEFAULT_COLOR = Color.parseColor("#FF4081");
    private int indicatorTextColor = DEFAULT_COLOR;//指示文本颜色
    private float[] dashIntervals = new float[]{4, 4, 4, 4};

    public int getIndicatorTextColor() {
        return indicatorTextColor;
    }

    public void setIndicatorTextColor(int indicatorTextColor) {
        this.indicatorTextColor = indicatorTextColor;
    }

    public float[] getDashIntervals() {
        return dashIntervals;
    }

    public void setDashIntervals(float dashInterval1, float dashInterval2, float dashInterval3, float dashInterval4) {
        this.dashIntervals = new float[]{dashInterval1, dashInterval2, dashInterval3, dashInterval4};
    }

    @Override
    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
