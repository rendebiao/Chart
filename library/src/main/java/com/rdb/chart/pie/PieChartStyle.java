package com.rdb.chart.pie;

import com.rdb.chart.ChartStyle;

/**
 * Created by DB on 2016/12/20.
 */

public class PieChartStyle extends ChartStyle {

    private int valueDecimal = 0;//数值保留小数位数
    private float textSize = 7f;//文本大小
    private float indicatorLineLength1 = 4f;//指示线长度
    private float indicatorLineLength2 = 10f;//指示线长度
    private float textMargin = 6f;//间隔

    public PieChartStyle() {
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getValueDecimal() {
        return valueDecimal;
    }

    public void setValueDecimal(int valueDecimal) {
        this.valueDecimal = valueDecimal;
    }

    public float getIndicatorLineLength1() {
        return indicatorLineLength1;
    }

    public void setIndicatorLineLength1(float indicatorLineLength1) {
        this.indicatorLineLength1 = indicatorLineLength1;
    }

    public float getIndicatorLineLength2() {
        return indicatorLineLength2;
    }

    public void setIndicatorLineLength2(float indicatorLineLength2) {
        this.indicatorLineLength2 = indicatorLineLength2;
    }

    public float getTextMargin() {
        return textMargin;
    }

    public void setTextMargin(float textMargin) {
        this.textMargin = textMargin;
    }
}
