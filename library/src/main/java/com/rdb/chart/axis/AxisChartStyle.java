package com.rdb.chart.axis;

import com.rdb.chart.ChartStyle;
import com.rdb.chart.IndicatorAlign;
import com.rdb.chart.line.LineType;

/**
 * Created by DB on 2016/12/20.
 */

public abstract class AxisChartStyle extends ChartStyle {

    private int axisLineColor = DEFAULT_COLOR;//X Y轴的颜色
    private int gridLineColor = DEFAULT_COLOR;//横向 纵向网格线颜色
    private int xAxisTextColor = DEFAULT_COLOR;//X轴文本颜色
    private int yAxisTextColor = DEFAULT_COLOR;//Y轴文本颜色
    private int yAxisValueDecimal = 0;//Y轴数值保留小数位数
    private float axisLineWidth = 1.5f;//X Y轴的宽度
    private float gridLineWidth = 1;//横向 纵向网格线的宽度
    private float xAxisTextSize = 8;//X轴文本大小
    private float yAxisTextSize = 8;//Y轴文本大小
    private float indicatorTextSize = 8;//指示文本大小
    private boolean startFromOrigin = true;//X轴是否在原点显示数据
    private LineType horLineType = LineType.DASH;//横向网格线
    private LineType verLineType = LineType.DASH;//纵向网格线
    private boolean showXAxis = true;//是否显示X轴
    private boolean showYAxis = true;//是否显示Y轴
    private boolean showXAxisScale = true;//是否显示X轴的分隔刻度
    private boolean showXAxisText = true;//是否显示X轴文本
    private boolean showYAxisText = true;//是否显示Y轴文本
    private boolean horMatch = true;
    private boolean showValue = false;
    private IndicatorAlign indicatorAlign = IndicatorAlign.BOTTOM_CENTER;//指示文本位置

    public int getAxisLineColor() {
        return axisLineColor;
    }

    public void setAxisLineColor(int axisLineColor) {
        this.axisLineColor = axisLineColor;
    }

    public int getGridLineColor() {
        return gridLineColor;
    }

    public void setGridLineColor(int gridLineColor) {
        this.gridLineColor = gridLineColor;
    }

    public int getXAxisTextColor() {
        return xAxisTextColor;
    }

    public void setXAxisTextColor(int xAxisTextColor) {
        this.xAxisTextColor = xAxisTextColor;
    }

    public int getYAxisTextColor() {
        return yAxisTextColor;
    }

    public void setYAxisTextColor(int yAxisTextColor) {
        this.yAxisTextColor = yAxisTextColor;
    }

    public int getYAxisValueDecimal() {
        return yAxisValueDecimal;
    }

    public void setYAxisValueDecimal(int yAxisValueDecimal) {
        this.yAxisValueDecimal = yAxisValueDecimal;
    }

    public float getXAxisTextSize() {
        return xAxisTextSize;
    }

    public void setXAxisTextSize(float xAxisTextSize) {
        this.xAxisTextSize = xAxisTextSize;
    }

    public float getYAxisTextSize() {
        return yAxisTextSize;
    }

    public void setYAxisTextSize(float yAxisTextSize) {
        this.yAxisTextSize = yAxisTextSize;
    }

    public LineType getHorLineType() {
        return horLineType;
    }

    public void setHorLineType(LineType horLineType) {
        this.horLineType = horLineType;
    }

    public LineType getVerLineType() {
        return verLineType;
    }

    public void setVerLineType(LineType verLineType) {
        this.verLineType = verLineType;
    }

    public boolean isShowXAxis() {
        return showXAxis;
    }

    public void setShowXAxis(boolean showXAxis) {
        this.showXAxis = showXAxis;
    }

    public boolean isShowYAxis() {
        return showYAxis;
    }

    public void setShowYAxis(boolean showYAxis) {
        this.showYAxis = showYAxis;
    }

    public boolean isShowXAxisScale() {
        return showXAxisScale;
    }

    public void setShowXAxisScale(boolean showXAxisScale) {
        this.showXAxisScale = showXAxisScale;
    }

    public boolean isShowXAxisText() {
        return showXAxisText;
    }

    public void setShowXAxisText(boolean showXAxisText) {
        this.showXAxisText = showXAxisText;
    }

    public boolean isShowYAxisText() {
        return showYAxisText;
    }

    public void setShowYAxisText(boolean showYAxisText) {
        this.showYAxisText = showYAxisText;
    }

    public float getAxisLineWidth() {
        return axisLineWidth;
    }

    public void setAxisLineWidth(float axisLineWidth) {
        this.axisLineWidth = axisLineWidth;
    }

    public float getGridLineWidth() {
        return gridLineWidth;
    }

    public void setGridLineWidth(float gridLineWidth) {
        this.gridLineWidth = gridLineWidth;
    }

    public IndicatorAlign getIndicatorAlign() {
        return indicatorAlign;
    }

    public void setIndicatorAlign(IndicatorAlign indicatorAlign) {
        this.indicatorAlign = indicatorAlign;
    }

    public float getIndicatorTextSize() {
        return indicatorTextSize;
    }

    public void setIndicatorTextSize(float indicatorTextSize) {
        this.indicatorTextSize = indicatorTextSize;
    }

    public boolean isStartFromOrigin() {
        return startFromOrigin;
    }

    protected void setStartFromOrigin(boolean startFromOrigin) {
        this.startFromOrigin = startFromOrigin;
    }

    public boolean isHorMatch() {
        return horMatch;
    }

    public void setHorMatch(boolean horMatch) {
        this.horMatch = horMatch;
    }

    public boolean isShowValue() {
        return showValue;
    }

    public void setShowValue(boolean showValue) {
        this.showValue = showValue;
    }
}
