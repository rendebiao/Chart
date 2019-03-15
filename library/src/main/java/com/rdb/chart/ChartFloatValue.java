package com.rdb.chart;

/**
 * Created by DB on 2016/12/21.
 */

public class ChartFloatValue implements ChartAnimatable {

    float curValue;
    float fromValue;
    float toValue;

    public ChartFloatValue() {
    }

    public ChartFloatValue(float curValue) {
        this.curValue = curValue;
    }

    public void moveTo(float toValue, boolean animateToValue) {
        if (animateToValue) {
            this.toValue = toValue;
            this.fromValue = curValue;
        } else {
            this.fromValue = this.curValue = this.toValue = toValue;
        }
    }

    public float getCurValue() {
        return curValue;
    }

    public float getFromValue() {
        return fromValue;
    }

    public float getToValue() {
        return toValue;
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        if (fromValue != toValue) {
            curValue = fromValue + (toValue - fromValue) * animatorValue;
            return true;
        }
        return false;
    }
}
