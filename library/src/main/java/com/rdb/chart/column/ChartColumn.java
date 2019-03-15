package com.rdb.chart.column;

import com.rdb.chart.ChartAnimatable;
import com.rdb.chart.ChartColor;
import com.rdb.chart.ChartFloatValue;

/**
 * Created by DB on 2016/12/19 0019.
 */

public class ChartColumn implements ChartAnimatable {

    String text;
    ChartColor color = new ChartColor();
    ChartColor selectedColor = new ChartColor();
    ChartFloatValue left = new ChartFloatValue();
    ChartFloatValue top = new ChartFloatValue();
    ChartFloatValue right = new ChartFloatValue();
    ChartFloatValue bottom = new ChartFloatValue();

    public ChartColumn() {
    }

    public ChartColumn(float left, float top, float right, float bottom, int color, int selectedColor) {
        this.left.moveTo(left, false);
        this.top.moveTo(top, false);
        this.right.moveTo(right, false);
        this.bottom.moveTo(bottom, false);
        this.color.moveTo(color, false);
        this.selectedColor.moveTo(selectedColor, false);
    }

    public void moveTo(float toLeft, boolean animateToLeft, float toTop, boolean animateToTop, float toRight, boolean animateToRight, float toBottom, boolean animateToBottom) {
        this.left.moveTo(toLeft, animateToLeft);
        this.top.moveTo(toTop, animateToTop);
        this.right.moveTo(toRight, animateToRight);
        this.bottom.moveTo(toBottom, animateToBottom);
    }

    public void moveColorTo(int toColor, boolean animateTo) {
        this.color.moveTo(toColor, animateTo);
    }

    public void moveSelectedColorTo(int toColor, boolean animateTo) {
        this.selectedColor.moveTo(toColor, animateTo);
    }

    public float getLeft() {
        return left.getCurValue();
    }

    public float getTop() {
        return top.getCurValue();
    }

    public float getRight() {
        return right.getCurValue();
    }

    public float getBottom() {
        return bottom.getCurValue();
    }

    public int getColor() {
        return color.getColor();
    }

    public int getSelectedColor() {
        return selectedColor.getColor();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        color.updateAnimatorValue(animatorValue);
        selectedColor.updateAnimatorValue(animatorValue);
        left.updateAnimatorValue(animatorValue);
        top.updateAnimatorValue(animatorValue);
        right.updateAnimatorValue(animatorValue);
        bottom.updateAnimatorValue(animatorValue);
        return true;
    }
}
