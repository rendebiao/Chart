package com.rdb.chart.axis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.rdb.chart.ChartPainter;
import com.rdb.chart.ChartUtils;
import com.rdb.chart.column.ColumnChart;
import com.rdb.chart.line.LineChart;
import com.rdb.chart.line.PointType;

import java.text.NumberFormat;

/**
 * Created by DB on 2016/12/22.
 */

public class AxisCharTableSelectedPainter extends ChartPainter<AxisChart> {

    float padding;
    float xOffset;
    float nameSpace;
    float indicatorSize;
    int selectLineColor = Color.parseColor("#dddddd");
    int selectTextColor = Color.parseColor("#666666");
    int selectRowBgColor = Color.parseColor("#efeff0");
    int selectTitleBgColor = Color.parseColor("#87909a");
    Paint selectBgPaint;
    Paint selectLinePaint;
    Paint selectTitlePaint;
    Paint selectNamePaint;
    Paint selectValuePaint;
    Paint selectGridPaint;
    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public AxisCharTableSelectedPainter(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        numberFormat.setGroupingUsed(false);
        padding = 8 * density;
        xOffset = 10 * density;
        nameSpace = 4 * density;
        indicatorSize = 4 * density;
        selectBgPaint = new Paint();
        selectBgPaint.setAntiAlias(true);
        selectLinePaint = new Paint();
        selectLinePaint.setAntiAlias(true);
        selectLinePaint.setStrokeWidth(1);
        selectLinePaint.setColor(selectLineColor);
        selectTitlePaint = new Paint();
        selectTitlePaint.setColor(Color.WHITE);
        selectTitlePaint.setTextSize(density * 10);
        selectTitlePaint.setAntiAlias(true);
        selectNamePaint = new Paint();
        selectNamePaint.setColor(selectTextColor);
        selectNamePaint.setTextSize(density * 10);
        selectNamePaint.setAntiAlias(true);
        selectValuePaint = new Paint();
        selectValuePaint.setColor(selectTextColor);
        selectValuePaint.setTextSize(density * 10);
        selectValuePaint.setAntiAlias(true);
        selectGridPaint = new Paint();
        selectGridPaint.setColor(Color.GRAY);
        selectGridPaint.setStyle(Paint.Style.STROKE);
        selectGridPaint.setStrokeWidth(1);
        selectGridPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas, AxisChart chart) {
        int selectPosition = chart.getSelectPosition();
        numberFormat.setMaximumFractionDigits(((AxisChartStyle) chart.getStyle()).getYAxisValueDecimal());
        ChartAxis axis = chart.getSelectPositionAxis();
        if (selectPosition >= 0 && axis != null) {
            float nameWidth = 0;
            float valueWidth = 0;
            boolean showName = chart.getGroupCount() > 1;
            float centerX = axis.getPoint().getCurX() - chart.getDragDistance();
            float titleWidth = selectTitlePaint.measureText(axis.getText()) + padding * 2;
            for (int i = 0; i < chart.getGroupCount(); i++) {
                if (showName) {
                    nameWidth = Math.max(selectNamePaint.measureText(chart.getGroupName(i)), nameWidth);
                }
                valueWidth = Math.max(selectValuePaint.measureText(numberFormat.format(chart.getValue(i, selectPosition))), valueWidth);
            }
            if (showName) {
                nameWidth += (padding * 2 + nameSpace + indicatorSize);
            }
            valueWidth += (padding * 2);
            float rowWidth = Math.max(nameWidth + valueWidth, titleWidth);
            float rowHeight = selectNamePaint.getTextSize() * 2;
            boolean alignRight = centerX + rowWidth + xOffset < chart.getCenterRect().right || centerX - chart.getCenterRect().left < chart.getCenterRect().right - centerX;
            float startOffset = alignRight ? xOffset : -rowWidth - xOffset;
            canvas.save();
            canvas.clipRect(chart.getCenterRect());
            canvas.drawLine(centerX, chart.getCenterRect().top, centerX, chart.getCenterRect().bottom, selectLinePaint);
            selectBgPaint.setColor(selectTitleBgColor);
            canvas.drawRect(centerX + startOffset, chart.getCenterRect().top, centerX + startOffset + rowWidth, chart.getCenterRect().top + rowHeight, selectBgPaint);
            selectBgPaint.setColor(selectRowBgColor);
            canvas.drawRect(centerX + startOffset, chart.getCenterRect().top + rowHeight, centerX + startOffset + rowWidth, chart.getCenterRect().top + rowHeight * (chart.getGroupCount() + 1), selectBgPaint);
            canvas.drawText(axis.getText(), centerX + startOffset + padding, chart.getCenterRect().top + rowHeight / 2 + selectTitlePaint.getTextSize() / 2, selectTitlePaint);
            if (showName) {
                canvas.drawLine(centerX + startOffset + nameWidth, chart.getCenterRect().top + rowHeight, centerX + startOffset + nameWidth, chart.getCenterRect().top + rowHeight * (chart.getGroupCount() + 1), selectGridPaint);
            }
            PointType type;
            for (int i = 0; i < chart.getGroupCount(); i++) {
                canvas.drawLine(centerX + startOffset, chart.getCenterRect().top + (i + 1) * rowHeight, centerX + startOffset + rowWidth, chart.getCenterRect().top + (i + 1) * rowHeight, selectGridPaint);
                if (showName) {
                    if (chart instanceof LineChart) {
                        selectBgPaint.setColor(((LineChart) chart).getLineColor(i));
                    } else if (chart instanceof ColumnChart) {
                        selectBgPaint.setColor(((ColumnChart) chart).getColumnColor(i, selectPosition));
                    }
                    type = (chart instanceof LineChart) ? ((LineChart) chart).getPointType(i) : PointType.SQUARE;
                    ChartUtils.drawPoint(canvas, selectBgPaint, type, centerX + startOffset + padding + indicatorSize / 2, chart.getCenterRect().top + (i + 1) * rowHeight + rowHeight / 2, indicatorSize / 2);
                    canvas.drawText(chart.getGroupName(i), centerX + startOffset + padding + nameSpace + indicatorSize, chart.getCenterRect().top + (i + 1) * rowHeight + rowHeight / 2 + selectTitlePaint.getTextSize() / 2, selectNamePaint);
                }
                canvas.drawText(numberFormat.format(chart.getValue(i, selectPosition)), centerX + startOffset + nameWidth + padding, chart.getCenterRect().top + (i + 1) * rowHeight + rowHeight / 2 + selectValuePaint.getTextSize() / 2, selectValuePaint);
            }
            canvas.restore();
        }
    }
}
