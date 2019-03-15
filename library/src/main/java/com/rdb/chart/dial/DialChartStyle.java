package com.rdb.chart.dial;

import com.rdb.chart.ChartStyle;

/**
 * Created by DB on 2016/12/20.
 */

public class DialChartStyle extends ChartStyle {


    private int dialColor = DEFAULT_COLOR;
    private float ringWidth = 8f;//圆环的宽度
    private int valueDecimal = 0;//数值保留小数位数
    private float smallScaleWidth = 0.5f;//小刻度的宽度
    private float largeScaleWidth = 1.5f;//大刻度的宽度
    private float smallScaleHeight = 2f;//小刻度的长度
    private float largeScaleHeight = 4f;//大刻度的长度
    private float scaleTextSize = 7f;//刻度文本大小
    private float valueTextSize = 12f;//当前值文本大小
    private float unitTextSize = 8f;//单位文本大小
    private float nameTextSize = 8f;//名称文本大小
    private int smallScaleCount = 4;//每个大刻度区间小刻度的数量
    private int largeScaleCount = 11;//大刻度的数量

    public DialChartStyle() {
    }

    public int getDialColor() {
        return dialColor;
    }

    public void setDialColor(int dialColor) {
        this.dialColor = dialColor;
    }

    public float getRingWidth() {
        return ringWidth;
    }

    public void setRingWidth(float ringWidth) {
        this.ringWidth = ringWidth;
    }

    public int getValueDecimal() {
        return valueDecimal;
    }

    public void setValueDecimal(int valueDecimal) {
        this.valueDecimal = valueDecimal;
    }

    public float getSmallScaleWidth() {
        return smallScaleWidth;
    }

    public void setSmallScaleWidth(float smallScaleWidth) {
        this.smallScaleWidth = smallScaleWidth;
    }

    public float getLargeScaleWidth() {
        return largeScaleWidth;
    }

    public void setLargeScaleWidth(float largeScaleWidth) {
        this.largeScaleWidth = largeScaleWidth;
    }

    public int getSmallScaleCount() {
        return smallScaleCount;
    }

    public void setSmallScaleCount(int smallScaleCount) {
        this.smallScaleCount = smallScaleCount;
    }

    public int getLargeScaleCount() {
        return largeScaleCount;
    }

    public void setLargeScaleCount(int largeScaleCount) {
        this.largeScaleCount = largeScaleCount;
    }

    public float getScaleTextSize() {
        return scaleTextSize;
    }

    public void setScaleTextSize(float scaleTextSize) {
        this.scaleTextSize = scaleTextSize;
    }

    public float getSmallScaleHeight() {
        return smallScaleHeight;
    }

    public void setSmallScaleHeight(float smallScaleHeight) {
        this.smallScaleHeight = smallScaleHeight;
    }

    public float getLargeScaleHeight() {
        return largeScaleHeight;
    }

    public void setLargeScaleHeight(float largeScaleHeight) {
        this.largeScaleHeight = largeScaleHeight;
    }

    public float getValueTextSize() {
        return valueTextSize;
    }

    public void setValueTextSize(float valueTextSize) {
        this.valueTextSize = valueTextSize;
    }

    public float getUnitTextSize() {
        return unitTextSize;
    }

    public void setUnitTextSize(float unitTextSize) {
        this.unitTextSize = unitTextSize;
    }

    public float getNameTextSize() {
        return nameTextSize;
    }

    public void setNameTextSize(float nameTextSize) {
        this.nameTextSize = nameTextSize;
    }
}
