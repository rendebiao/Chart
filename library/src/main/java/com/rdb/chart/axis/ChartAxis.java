package com.rdb.chart.axis;

import com.rdb.chart.ChartAnimatable;
import com.rdb.chart.ChartLine;
import com.rdb.chart.ChartPoint;

/**
 * Created by DB on 2016/12/22.
 */

public class ChartAxis implements ChartAnimatable {

    String text;
    float value;
    float textWidth;
    float textHeight;
    ChartLine line = new ChartLine();
    ChartPoint point = new ChartPoint();

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ChartLine getLine() {
        return line;
    }

    public ChartPoint getPoint() {
        return point;
    }

    public void moveLineTo(float toStartX, float toStartY, float toEndX, float toEndY, boolean animateTo) {
        line.setLength(0);
        line.addPoint(toStartX, toStartY, animateTo);
        line.addPoint(toEndX, toEndY, animateTo);
    }

    public void moveLineColor(int color, boolean animator) {
        line.moveColorTo(color, animator);
    }

    public void movePointTo(float toX, float toY, boolean animateTo) {
        point.movePointTo(toX, toY, animateTo);
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        line.updateAnimatorValue(animatorValue);
        point.updateAnimatorValue(animatorValue);
        return true;
    }
}
