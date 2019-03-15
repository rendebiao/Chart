package com.rdb.chart;

import android.graphics.Color;

/**
 * Created by DB on 2016/12/21.
 */

public class ChartColor implements ChartAnimatable {


    ChartIntValue alpha;
    ChartIntValue red;
    ChartIntValue green;
    ChartIntValue blue;

    public ChartColor() {
        alpha = new ChartIntValue(0);
        red = new ChartIntValue(0);
        green = new ChartIntValue(0);
        blue = new ChartIntValue(0);
    }

    public ChartColor(int color) {
        alpha = new ChartIntValue(Color.alpha(color));
        red = new ChartIntValue(Color.red(color));
        green = new ChartIntValue(Color.green(color));
        blue = new ChartIntValue(Color.blue(color));
    }

    public void moveTo(int toColor, boolean animateTo) {
        alpha.moveTo(Color.alpha(toColor), animateTo);
        red.moveTo(Color.red(toColor), animateTo);
        green.moveTo(Color.green(toColor), animateTo);
        blue.moveTo(Color.blue(toColor), animateTo);
    }

    public int getColor() {
        return Color.argb(alpha.getCurValue(), red.getCurValue(), green.getCurValue(), blue.getCurValue());
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        alpha.updateAnimatorValue(animatorValue);
        red.updateAnimatorValue(animatorValue);
        green.updateAnimatorValue(animatorValue);
        blue.updateAnimatorValue(animatorValue);
        return true;
    }
}
