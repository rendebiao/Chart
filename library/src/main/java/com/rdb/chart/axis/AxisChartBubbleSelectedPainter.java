package com.rdb.chart.axis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.rdb.chart.ChartPainter;

import java.text.NumberFormat;

/**
 * Created by DB on 2016/12/22.
 */

public class AxisChartBubbleSelectedPainter extends ChartPainter<AxisChart> {

    float padding;
    float yOffset;
    float cornerRadios;
    float arrowWidth;
    float arrowHeight;
    Path path;
    RectF rectF;
    RectF tempRectF;
    Paint selectBgPaint;
    Paint selectValuePaint;
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public AxisChartBubbleSelectedPainter(Context context, int backgroundColor, int textColor) {
        float density = context.getResources().getDisplayMetrics().density;
        numberFormat.setGroupingUsed(false);
        path = new Path();
        rectF = new RectF();
        tempRectF = new RectF();
        padding = 4 * density;
        yOffset = 4 * density;
        arrowWidth = 4 * density;
        arrowHeight = 4 * density;
        cornerRadios = 3 * density;
        selectBgPaint = new Paint();
        selectBgPaint.setColor(backgroundColor);
        selectBgPaint.setAntiAlias(true);
        selectBgPaint.setStyle(Paint.Style.FILL);
        selectValuePaint = new Paint();
        selectValuePaint.setColor(textColor);
        selectValuePaint.setTextSize(8 * density);
        selectValuePaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas, AxisChart chart) {
        numberFormat.setMaximumFractionDigits(((AxisChartStyle) chart.getStyle()).getYAxisValueDecimal());
        int selectPosition = chart.getSelectPosition();
        ChartAxis axis = chart.getSelectPositionAxis();
        if (selectPosition >= 0 && axis != null) {
            float value = chart.getValue(0, selectPosition);
            float y = chart.getYByValue(value);
            String valueText = value + "";
            float valueWidth = selectValuePaint.measureText(valueText);
            float centerX = axis.getPoint().getCurX() - chart.getDragDistance();
            valueWidth += (padding * 2);
            float rowHeight = selectValuePaint.getTextSize() * 2;
            boolean alignLeft = centerX - valueWidth > chart.getCenterRect().left || centerX - chart.getCenterRect().left > chart.getCenterRect().right - centerX;
            float leftOffset = alignLeft ? -valueWidth : 0;
            float topOffset = Math.max(chart.getCenterRect().top - (y - rowHeight - arrowHeight - yOffset), 0);
            rectF.set(centerX + leftOffset, y - rowHeight - arrowHeight - yOffset + topOffset, centerX + leftOffset + valueWidth, y - arrowHeight - yOffset + topOffset);
            canvas.save();
            canvas.clipRect(chart.getCenterRect());
            path.reset();
            path.moveTo(rectF.left + cornerRadios, rectF.top);
            path.lineTo(rectF.right - cornerRadios, rectF.top);
            tempRectF.set(rectF.right - 2 * cornerRadios, rectF.top, rectF.right, rectF.top + 2 * cornerRadios);
            path.arcTo(tempRectF, 270, 90);
            if (alignLeft) {
                path.lineTo(rectF.right, rectF.bottom + arrowHeight);
                path.lineTo(rectF.right - arrowWidth, rectF.bottom);
                path.lineTo(rectF.left + cornerRadios, rectF.bottom);
                tempRectF.set(rectF.left, rectF.bottom - 2 * cornerRadios, rectF.left + 2 * cornerRadios, rectF.bottom);
                path.arcTo(tempRectF, 90, 90);
            } else {
                path.lineTo(rectF.right, rectF.bottom - cornerRadios);
                tempRectF.set(rectF.right - 2 * cornerRadios, rectF.bottom - 2 * cornerRadios, rectF.right, rectF.bottom);
                path.arcTo(tempRectF, 0, 90);
                path.lineTo(rectF.left + arrowWidth, rectF.bottom);
                path.lineTo(rectF.left, rectF.bottom + arrowHeight);
            }
            path.lineTo(rectF.left, rectF.top + cornerRadios);
            tempRectF.set(rectF.left, rectF.top, rectF.left + 2 * cornerRadios, rectF.top + 2 * cornerRadios);
            path.arcTo(tempRectF, 180, 90);
            path.close();
            canvas.drawPath(path, selectBgPaint);
            canvas.drawText(valueText, rectF.left + padding, rectF.bottom - rowHeight / 2 + selectValuePaint.getTextSize() / 2, selectValuePaint);
            canvas.restore();
            path.reset();
        }
    }
}
