package com.rdb.chart;

/**
 * Created by DB on 2016/12/21.
 */

public class ChartIntValue implements ChartAnimatable {

    int curValue;
    int fromValue;
    int toValue;

    public ChartIntValue() {
    }

    public ChartIntValue(int curValue) {
        this.curValue = curValue;
    }

    public void moveTo(int toValue, boolean animateToValue) {
        if (animateToValue) {
            this.toValue = toValue;
            this.fromValue = curValue;
        } else {
            this.fromValue = this.curValue = this.toValue = toValue;
        }
    }

    public int getCurValue() {
        return curValue;
    }

    public int getFromValue() {
        return fromValue;
    }

    public int getToValue() {
        return toValue;
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        if (fromValue != toValue) {
            curValue = (int) (fromValue + (toValue - fromValue) * animatorValue);
            return true;
        }
        return false;
    }
}
