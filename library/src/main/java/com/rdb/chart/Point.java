package com.rdb.chart;

/**
 * Created by DB on 2016/12/19 0019.
 */

public class Point implements ChartAnimatable {


    ChartFloatValue x;
    ChartFloatValue y;
    String text;

    public Point() {
        x = new ChartFloatValue();
        y = new ChartFloatValue();
    }

    public Point(float x, float y) {
        this.x = new ChartFloatValue(x);
        this.y = new ChartFloatValue(y);
    }

    public void movePointTo(float toXValue, float toYValue, boolean animateTo) {
        this.x.moveTo(toXValue, animateTo);
        this.y.moveTo(toYValue, animateTo);
    }

    public float getCurX() {
        return x.getCurValue();
    }

    public float getCurY() {
        return this.y.getCurValue();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        x.updateAnimatorValue(animatorValue);
        y.updateAnimatorValue(animatorValue);
        return true;
    }
}
