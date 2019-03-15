package com.rdb.chart;

import com.rdb.chart.line.PointType;

/**
 * Created by DB on 2016/12/20.
 */

public class ChartIndicator {
    private int color;
    private String text;
    private IndicatorType type;
    private PointType pointType;

    public ChartIndicator(int color, String text, IndicatorType type, PointType pointType) {
        this.color = color;
        this.text = text;
        this.type = type;
        this.pointType = pointType;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IndicatorType getType() {
        return type;
    }

    public PointType getPointType() {
        return pointType;
    }
}
