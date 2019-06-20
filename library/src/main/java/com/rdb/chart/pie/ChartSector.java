package com.rdb.chart.pie;

import com.rdb.chart.ChartAnimatable;
import com.rdb.chart.ChartColor;
import com.rdb.chart.ChartFloatValue;

public class ChartSector implements ChartAnimatable {

    private String name;
    private ChartColor color = new ChartColor();
    private ChartColor selectedColor = new ChartColor();
    private ChartFloatValue value = new ChartFloatValue();

    public void moveColorTo(int toColor, boolean animateTo) {
        this.color.moveTo(toColor, animateTo);
    }

    public void moveSelectedColorTo(int toColor, boolean animateTo) {
        this.selectedColor.moveTo(toColor, animateTo);
    }

    public void moveValueTo(float toValue, boolean animateTo) {
        this.value.moveTo(toValue, animateTo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color.getColor();
    }

    public int getSelectedColor() {
        return selectedColor.getColor();
    }

    public float getValue() {
        return value.getCurValue();
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        value.updateAnimatorValue(animatorValue);
        color.updateAnimatorValue(animatorValue);
        selectedColor.updateAnimatorValue(animatorValue);
        return true;
    }
}
