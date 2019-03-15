package com.rdb.chart.line;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.rdb.chart.Chart;
import com.rdb.chart.ChartLine;
import com.rdb.chart.ChartUtils;
import com.rdb.chart.Point;
import com.rdb.chart.axis.AxisChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 2016/12/19 0019.
 */

public class LineChart extends AxisChart<LineChartAdapter, LineChartStyle> implements LineChartInterface, Chart.OnPointDrawListener {

    private Paint textPaint;
    private Paint linePaint;
    private Paint pointPaint;
    private Paint lineFillPaint;
    private int animatorCount;
    private float lastXAxisRatio;
    private int lastMaxPageCount;
    private int lastXAxisTextCount;
    private Path drawPath = new Path();
    private List<ChartLine> lines = new ArrayList<>();

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        lineFillPaint = new Paint();
        lineFillPaint.setStyle(Paint.Style.FILL);
        lineFillPaint.setAntiAlias(true);
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
    }

    @Override
    protected void onDrawChart(Canvas canvas, RectF chartRect) {
        super.onDrawChart(canvas, chartRect);
        canvas.save();
        canvas.clipRect(getCenterRect());
        drawLine(canvas);
        canvas.restore();
        drawPoint(canvas);
        notifyChildDrawComplete(canvas);
    }

    @Override
    protected void onDatasetChanged(RectF chartRect, LineChartAdapter adapter) {
        super.onDatasetChanged(chartRect, adapter);
        textPaint.setTextSize(dpToPx(style.getYAxisTextSize()));
        textPaint.setColor(style.getYAxisTextColor());
        float xAxisRatio = getAxisPositionRatio();
        float startOffset = style.isStartFromOrigin() ? 0 : 0.5f;
        float bottom = getCenterRect().bottom - dpToPx(style.getAxisLineWidth()) / 2;
        animatorCount = (int) (Math.max(lastMaxPageCount, maxPageCount) / 0.9f + 1);
        if (lines.size() > 0) {
            animatorCount = Math.min(animatorCount, lines.get(0).getMaxPointCount());
        }
        int totalCount = Math.max(xAxisTextCount, lines.size() > 0 ? lines.get(0).getMaxPointCount() : 0);
        boolean showToShow, showToHide, hideToShow, animator;
        float x, y;
        for (int i = 0; i < groupCount; i++) {
            ChartLine line;
            if (lines.size() > i) {
                line = lines.get(i);
            } else {
                line = new ChartLine();
                lines.add(line);
            }
            line.setLength(totalCount);
            line.setPointType(adapter.getPointType(i));
            line.moveColorTo(adapter.getLineColor(i), true);
            line.setLineType(adapter.getLineType(i));
            for (int j = 0; j < totalCount; j++) {
                animator = j < animatorCount;
                showToShow = j < lastXAxisTextCount && j < xAxisTextCount;
                showToHide = j < lastXAxisTextCount && j >= xAxisTextCount;
                hideToShow = j >= lastXAxisTextCount && j < xAxisTextCount;
                x = getCenterRect().left + xAxisRatio * (j + startOffset);
                if (showToShow || hideToShow) {
                    y = bottom - getYAxisValueRatio() * (values[i][j] - yAxisMinValue);
                    if (hideToShow && animator) {
                        line.getPoint(j).movePointTo(getCenterRect().left + (lastXAxisRatio <= 0 ? xAxisRatio : lastXAxisRatio) * (j + startOffset), getCenterRect().bottom, false);
                    }
                    line.getPoint(j).movePointTo(x, y, animator);
                    line.getPoint(j).setText(ChartUtils.getText(values[i][j], style.getYAxisValueDecimal()));
                } else {
                    line.getPoint(j).movePointTo(x, bottom, showToHide);
                    line.getPoint(j).setText("");
                }
            }
            line.setLength(xAxisTextCount);
        }
        lastXAxisRatio = xAxisRatio;
        lastMaxPageCount = maxPageCount;
        lastXAxisTextCount = xAxisTextCount;
    }

    private void drawLine(Canvas canvas) {
        for (int i = 0; i < groupCount; i++) {
            drawPath.reset();
            drawChartLine(canvas, lines.get(i), linePaint, getDragDistance(), 0);
            if (lines.get(i).getPointCount() > 1) {
                if (style.isFilled()) {
                    for (int j = 0; j < lines.get(i).getPointCount(); j++) {
                        Point point = lines.get(i).getPoint(j);
                        if (j == 0) {
                            drawPath.moveTo(point.getCurX() - getDragDistance(), getCenterRect().bottom);
                            drawPath.lineTo(point.getCurX() - getDragDistance(), point.getCurY());
                        } else if (j == lines.get(i).getPointCount() - 1) {
                            drawPath.lineTo(point.getCurX() - getDragDistance(), point.getCurY());
                            drawPath.lineTo(point.getCurX() - getDragDistance(), getCenterRect().bottom);
                        } else {
                            drawPath.lineTo(point.getCurX() - getDragDistance(), point.getCurY());
                        }
                    }
                    drawPath.close();
                    lineFillPaint.setColor(ChartUtils.updateAlpha(lines.get(i).getColor(), 60));
                    canvas.drawPath(drawPath, lineFillPaint);
                }
            }

        }
    }

    private void drawPoint(Canvas canvas) {
        float radius = dpToPx(style.getPointRadius());
        for (int i = 0; i < groupCount; i++) {
            drawPoints(canvas, lines.get(i), pointPaint, getDragDistance(), 0, radius, getCenterRect(), style.isShowValue() ? this : null);
        }
    }

    @Override
    protected void onAnimationUpdate(float animatorValue) {
        super.onAnimationUpdate(animatorValue);
        for (int i = 0; i < groupCount; i++) {
            for (int j = 0; j < animatorCount; j++) {
                lines.get(i).updateAnimatorValue(animatorValue);
            }
        }
    }

    @Override
    public PointType getPointType(int linePosition) {
        return lines.get(linePosition).getPointType();
    }

    @Override
    public LineType getLineType(int linePosition) {
        return lines.get(linePosition).getLineType();
    }

    @Override
    public int getLineColor(int linePosition) {
        return lines.get(linePosition).getColor();
    }

    @Override
    public void onPointDraw(Canvas canvas, Point point) {
        if (!TextUtils.isEmpty(point.getText())) {
            float width = textPaint.measureText(point.getText());
            canvas.drawText(point.getText(), point.getCurX() - width / 2, point.getCurY() - textPaint.getTextSize() / 2, textPaint);
        }
    }
}
