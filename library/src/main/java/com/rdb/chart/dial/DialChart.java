package com.rdb.chart.dial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import com.rdb.chart.Chart;
import com.rdb.chart.ChartFloatValue;
import com.rdb.chart.ChartUtils;

/**
 * Created by DB on 2016/12/21.
 */

public class DialChart extends Chart<DialChartAdapter, DialChartStyle> implements DialChartInterface {

    private float minValue;
    private float maxValue;
    private float curValue;
    private float totalDegree;
    private Paint dialPaint;
    private Paint handPaint;
    private Paint scalePaint;
    private Paint scaleTextPaint;
    private Paint valueTextPaint;
    private Paint unitTextPaint;
    private Paint nameTextPaint;
    private float degreeRatio;
    private float startDegree;
    private ChartFloatValue chartValue;
    private Path drawPath = new Path();
    private OnDialChartDrawListener dialChartDrawListener;
    private SparseBooleanArray scaleTextVisibles = new SparseBooleanArray();

    public DialChart(Context context) {
        this(context, null);
    }

    public DialChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dialPaint = new Paint();
        dialPaint.setAntiAlias(true);
        dialPaint.setStyle(Paint.Style.STROKE);
        handPaint = new Paint();
        handPaint.setAntiAlias(true);
        handPaint.setStyle(Paint.Style.FILL);
        scalePaint = new Paint();
        scalePaint.setAntiAlias(true);
        scalePaint.setStyle(Paint.Style.STROKE);
        scaleTextPaint = new Paint();
        scaleTextPaint.setAntiAlias(true);
        valueTextPaint = new Paint();
        valueTextPaint.setAntiAlias(true);
        unitTextPaint = new Paint();
        unitTextPaint.setAntiAlias(true);
        nameTextPaint = new Paint();
        unitTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onDrawChart(Canvas canvas, RectF chartRect) {
        float width = Math.min(chartRect.width(), chartRect.height());
        float radius = (width - dpToPx(style.getRingWidth())) / 2;
        float centerX = chartRect.width() / 2;
        float centerY = chartRect.height() / 2;
        dialPaint.setStrokeWidth(dpToPx(style.getRingWidth()));
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius), 270 - totalDegree / 2 - 1, totalDegree + 2, false, dialPaint);
        int count = style.getSmallScaleCount() * (style.getLargeScaleCount() - 1) + style.getLargeScaleCount();
        float perDegree = totalDegree / (count - 1);
        int largePosition = 0;
        for (int j = 0; j < count; j++) {
            boolean isLarge = j % (style.getSmallScaleCount() + 1) == 0;
            float height = isLarge ? dpToPx(style.getLargeScaleHeight()) : dpToPx(style.getSmallScaleHeight());
            float degree = 270 - totalDegree / 2 + j * perDegree;
            float fromX = ChartUtils.getCirclePointX(centerX, radius - dpToPx(style.getRingWidth()) / 2, degree);
            float fromY = ChartUtils.getCirclePointY(centerY, radius - dpToPx(style.getRingWidth()) / 2, degree);
            float toX = ChartUtils.getCirclePointX(centerX, radius - dpToPx(style.getRingWidth()) / 2 - height, degree);
            float toY = ChartUtils.getCirclePointY(centerY, radius - dpToPx(style.getRingWidth()) / 2 - height, degree);
            scalePaint.setStrokeWidth(isLarge ? dpToPx(style.getLargeScaleWidth()) : dpToPx(style.getSmallScaleWidth()));
            canvas.drawLine(fromX, fromY, toX, toY, scalePaint);
            if (isLarge) {
                if (scaleTextVisibles.get(j / (style.getSmallScaleCount() + 1))) {
                    float x = ChartUtils.getCirclePointX(centerX, radius * 0.75f, degree);
                    float y = ChartUtils.getCirclePointY(centerY, radius * 0.75f, degree);
                    String value = ChartUtils.getText(minValue + (maxValue - minValue) * largePosition / (style.getLargeScaleCount() - 1), style.getValueDecimal());
                    float textWidth = scaleTextPaint.measureText(value);
                    canvas.drawText(value, x - textWidth / 2, y + scaleTextPaint.getTextSize() / 2, scaleTextPaint);
                }
                largePosition++;
            }
        }
        float degree = startDegree + (chartValue.getCurValue() - minValue) * degreeRatio;
        float handTopX = ChartUtils.getCirclePointX(centerX, radius * 3 / 5, degree);
        float handTopY = ChartUtils.getCirclePointY(centerY, radius * 3 / 5, degree);
        float handLeftX = ChartUtils.getCirclePointX(centerX, radius * 1 / 24, degree - 90);
        float handLeftY = ChartUtils.getCirclePointY(centerY, radius * 1 / 24, degree - 90);
        float handRightX = ChartUtils.getCirclePointX(centerX, radius * 1 / 24, degree + 90);
        float handRightY = ChartUtils.getCirclePointY(centerY, radius * 1 / 24, degree + 90);
        float handBottomX = ChartUtils.getCirclePointX(centerX, radius * 1 / 16, degree - 180);
        float handBottomY = ChartUtils.getCirclePointY(centerY, radius * 1 / 16, degree - 180);
        drawPath.reset();
        drawPath.moveTo(handBottomX, handBottomY);
        drawPath.lineTo(handLeftX, handLeftY);
        drawPath.lineTo(handTopX, handTopY);
        drawPath.lineTo(handRightX, handRightY);
        drawPath.close();
        canvas.drawPath(drawPath, handPaint);
        String valueText = ChartUtils.getText(chartValue.getCurValue(), style.getValueDecimal());
        float valueTextWidth = valueTextPaint.measureText(valueText);
        float unitTextWidth = unitTextPaint.measureText(valueUnit);
        float nameTextWidth = nameTextPaint.measureText(chartName);
        canvas.drawText(valueText, centerX - (valueTextWidth + unitTextWidth) / 2, centerY + 1.5f * valueTextPaint.getTextSize(), valueTextPaint);
        canvas.drawText(valueUnit, centerX - (valueTextWidth + unitTextWidth) / 2 + valueTextWidth, centerY + 1.5f * valueTextPaint.getTextSize(), unitTextPaint);
        canvas.drawText(chartName, centerX - nameTextWidth / 2, centerY + 1.5f * valueTextPaint.getTextSize() + 1.2f * nameTextPaint.getTextSize(), nameTextPaint);
        if (dialChartDrawListener != null) {
            dialChartDrawListener.onDraw(this, canvas);
        }
    }

    @Override
    protected void onDatasetChanged(RectF chartRect, DialChartAdapter adapter) {
        super.onDatasetChanged(chartRect, adapter);
        minValue = adapter.getMinValue();
        maxValue = adapter.getMaxValue();
        curValue = adapter.getCurValue();
        totalDegree = adapter.getTotalDegree();
        dialPaint.setColor(style.getDialColor());
        handPaint.setColor(style.getDialColor());
        scalePaint.setColor(style.getDialColor());
        scaleTextPaint.setColor(style.getDialColor());
        valueTextPaint.setColor(style.getDialColor());
        scaleTextPaint.setTextSize(dpToPx(style.getScaleTextSize()));
        valueTextPaint.setTextSize(dpToPx(style.getValueTextSize()));
        unitTextPaint.setColor(style.getDialColor());
        unitTextPaint.setTextSize(dpToPx(style.getUnitTextSize()));
        nameTextPaint.setColor(style.getDialColor());
        nameTextPaint.setTextSize(dpToPx(style.getNameTextSize()));
        for (int i = 0; i < style.getLargeScaleCount(); i++) {
            scaleTextVisibles.put(i, adapter.showScaleText(i));
        }
        degreeRatio = totalDegree / (maxValue - minValue);
        startDegree = 270 - totalDegree / 2;
        if (chartValue == null) {
            chartValue = new ChartFloatValue();
            chartValue.moveTo(minValue, false);
        }
        chartValue.moveTo(curValue, true);
    }

    @Override
    protected void onAnimationUpdate(float animatorValue) {
        super.onAnimationUpdate(animatorValue);
        chartValue.updateAnimatorValue(animatorValue);
    }


    public void setDialChartDrawListener(OnDialChartDrawListener dialChartDrawListener) {
        this.dialChartDrawListener = dialChartDrawListener;
    }

    @Override
    public float getMinValue() {
        return minValue;
    }

    @Override
    public float getMaxValue() {
        return maxValue;
    }

    @Override
    public float getCurValue() {
        return curValue;
    }

    @Override
    public float getTotalDegree() {
        return totalDegree;
    }

    @Override
    public boolean showScaleText(int largeScalePosition) {
        return false;
    }
}
