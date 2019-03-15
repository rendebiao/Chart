package com.rdb.chart;

/**
 * Created by DB on 2016/12/19 0019.
 */

public class ChartPoint extends Point {

    ChartColor color;

    public ChartPoint() {
        super();
        color = new ChartColor();
    }

    public ChartPoint(float x, float y, int color) {
        super(x, y);
        this.color = new ChartColor(color);
    }

    public void moveColorTo(int toColor, boolean animateTo) {
        color.moveTo(toColor, animateTo);
    }

    public int getColor() {
        return color.getColor();
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        super.updateAnimatorValue(animatorValue);
        color.updateAnimatorValue(animatorValue);
        return true;
    }
}
