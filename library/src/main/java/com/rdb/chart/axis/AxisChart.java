package com.rdb.chart.axis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.rdb.chart.Chart;
import com.rdb.chart.ChartIndicator;
import com.rdb.chart.ChartLine;
import com.rdb.chart.ChartPainter;
import com.rdb.chart.ChartUtils;
import com.rdb.chart.IndicatorType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 2016/12/19 0019.
 */

public abstract class AxisChart<T extends AxisChartAdapter, V extends AxisChartStyle> extends Chart<T, V> implements AxisChartInterface {

    protected int groupCount;
    protected float[][] values;
    protected SparseArray<String> groupNames = new SparseArray();
    protected List<ChartIndicator> indicators = new ArrayList<>();
    protected int maxPageCount;
    protected int xAxisTextCount;
    protected int yAxisValueCount;
    protected float yAxisMinValue;
    protected float yAxisMaxValue;
    protected int selectPosition = -1;
    private String unitText;
    private float xAxisPositionRatio;
    private float yAxisPositionRatio;
    private float yAxisValueRatio;
    private boolean dragEnable;
    private float dragDistance;
    private float maxDragDistance;
    private int lastXValueCount;
    private int lastYValueCount;
    private boolean drag;
    private int touchSlop;
    private Paint axisPaint;
    private Paint horLinePaint;
    private Paint verLinePaint;
    private Paint dashLinePaint;
    private Paint xAxisTextPaint;
    private Paint yAxisTextPaint;
    private Paint indicatorPaint;
    private Paint indicatorTextPaint;
    private RectF drawRect = new RectF();
    private RectF gridRect = new RectF();
    private RectF xTextRect = new RectF();
    private RectF yTextRect = new RectF();
    private RectF centerRect = new RectF();
    private RectF indicatorRect = new RectF();
    private ChartPainter<AxisChart> selectedPainter;
    private AxisChartDrawListener<AxisChart> drawListener;
    private ChartLine xAxisLine;
    private ChartLine yAxisLine;
    private List<ChartAxis> verChartAxiss = new ArrayList<>();
    private List<ChartAxis> horChartAxiss = new ArrayList<>();
    private float startX;
    private float lastDistance;

    public AxisChart(Context context) {
        this(context, null);
    }

    public AxisChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AxisChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        axisPaint = new Paint();
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setAntiAlias(true);
        horLinePaint = new Paint();
        horLinePaint.setStyle(Paint.Style.STROKE);
        horLinePaint.setAntiAlias(true);
        verLinePaint = new Paint();
        verLinePaint.setStyle(Paint.Style.STROKE);
        verLinePaint.setAntiAlias(true);
        dashLinePaint = new Paint();
        dashLinePaint.setStyle(Paint.Style.STROKE);
        dashLinePaint.setAntiAlias(true);
        dashLinePaint.setPathEffect(new DashPathEffect(new float[]{6, 6, 6, 6}, 1));
        xAxisTextPaint = new Paint();
        xAxisTextPaint.setAntiAlias(true);
        yAxisTextPaint = new Paint();
        yAxisTextPaint.setAntiAlias(true);
        indicatorPaint = new Paint();
        indicatorPaint.setAntiAlias(true);
        indicatorTextPaint = new Paint();
        indicatorTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onDrawChart(Canvas canvas, RectF chartRect) {
        canvas.save();
        canvas.clipRect(drawRect);
        if (drawListener != null) {
            drawListener.onDraw(canvas, this, AxisDrawProgress.START);
        }
        drawYAxis(canvas);
        drawXAxis(canvas);
        drawIndicator(canvas);
        if (drawListener != null) {
            drawListener.onDraw(canvas, this, AxisDrawProgress.AFTER_AXIS);
        }
        canvas.restore();
    }

    protected void notifyChildDrawComplete(Canvas canvas) {
        if (drawListener != null) {
            drawListener.onDraw(canvas, this, AxisDrawProgress.AFTER_DATA);
        }
        if (selectedPainter != null) {
            selectedPainter.onDraw(canvas, this);
        }
        if (drawListener != null) {
            drawListener.onDraw(canvas, this, AxisDrawProgress.AFTER_ALL);
        }
    }

    @Override
    protected void onDataSetChanged(RectF chartRect, T adapter) {
        super.onDataSetChanged(chartRect, adapter);
        if (TextUtils.isEmpty(valueUnit)) {
            unitText = "";
        } else {
            unitText = "(" + valueUnit + ")";
        }
        maxPageCount = adapter.getMaxPageCount();
        xAxisTextCount = adapter.getXAxisTextCount();
        yAxisValueCount = adapter.getYAxisValueCount();
        yAxisMinValue = adapter.getYAxisMinValue();
        yAxisMaxValue = adapter.getYAxisMaxValue();
        axisPaint.setStrokeWidth(dpToPx(style.getAxisLineWidth()));
        horLinePaint.setStrokeWidth(dpToPx(style.getGridLineWidth()));
        verLinePaint.setStrokeWidth(dpToPx(style.getGridLineWidth()));
        xAxisTextPaint.setColor(style.getXAxisTextColor());
        xAxisTextPaint.setTextSize(dpToPx(style.getXAxisTextSize()));
        yAxisTextPaint.setColor(style.getYAxisTextColor());
        yAxisTextPaint.setTextSize(dpToPx(style.getYAxisTextSize()));
        indicatorTextPaint.setColor(style.getIndicatorTextColor());
        indicatorTextPaint.setTextSize(dpToPx(style.getIndicatorTextSize()));
        selectPosition = -1;
        dragEnable = xAxisTextCount > maxPageCount;
        dragDistance = 0;
        groupCount = adapter.getGroupCount();
        values = new float[groupCount][xAxisTextCount];
        for (int i = 0; i < groupCount; i++) {
            for (int j = 0; j < xAxisTextCount; j++) {
                values[i][j] = adapter.getValue(i, j);
            }
        }
        indicators.clear();
        List list = adapter.getChartIndicators();
        if (list != null) {
            indicators.addAll(list);
        }
        float xAxisTextHeight = xAxisTextPaint.getTextSize() * 1.5f;
        for (int i = 0; i < xAxisTextCount; i++) {
            groupNames.put(i, adapter.getGroupName(i));
            ChartAxis axis;
            if (verChartAxiss.size() > i) {
                axis = verChartAxiss.get(i);
            } else {
                axis = new ChartAxis();
                verChartAxiss.add(axis);
            }
            axis.setText(adapter.getXAxisText(i));
        }
        float yAxisTextWidth = yAxisTextPaint.measureText(unitText);
        for (int i = 0; i < yAxisValueCount; i++) {
            ChartAxis axis;
            if (horChartAxiss.size() > i) {
                axis = horChartAxiss.get(i);
            } else {
                axis = new ChartAxis();
                horChartAxiss.add(axis);
            }
            axis.setValue(yAxisMinValue + (yAxisMaxValue - yAxisMinValue) * i / (yAxisValueCount - 1));
            axis.setText(ChartUtils.getText(axis.getValue(), style.getYAxisValueDecimal()));
            axis.textWidth = yAxisTextPaint.measureText(axis.getText());
            yAxisTextWidth = Math.max(axis.textWidth, yAxisTextWidth);
        }
        float yTextOffset = dpToPx(8);
        yAxisTextWidth = yAxisTextWidth + yTextOffset;
        float indicatorTextHeight = style.getIndicatorAlign() != null ? measureIndicatorTextHeight() : 0;
        drawRect.set(chartRect);
        if (style.getIndicatorAlign() == null || indicators.size() == 0) {
            xTextRect.set(drawRect.left + yAxisTextWidth, drawRect.bottom - xAxisTextHeight, drawRect.right, drawRect.bottom);
            yTextRect.set(drawRect.left, drawRect.top, drawRect.left + yAxisTextWidth, xTextRect.top);
            centerRect.set(yTextRect.right, drawRect.top, drawRect.right, xTextRect.top);
        } else {
            if (style.getIndicatorAlign().isAlignTop()) {
                indicatorRect.set(drawRect.left + yAxisTextWidth, drawRect.top, drawRect.right, drawRect.top + indicatorTextHeight);
                xTextRect.set(drawRect.left + yAxisTextWidth, drawRect.bottom - xAxisTextHeight, drawRect.right, drawRect.bottom);
                yTextRect.set(drawRect.left, indicatorRect.bottom, drawRect.left + yAxisTextWidth, xTextRect.top);
                centerRect.set(yTextRect.right, indicatorRect.bottom, drawRect.right, xTextRect.top);
            } else {
                indicatorRect.set(drawRect.left + yAxisTextWidth, drawRect.bottom - indicatorTextHeight, drawRect.right, drawRect.bottom);
                xTextRect.set(drawRect.left + yAxisTextWidth, indicatorRect.top - xAxisTextHeight, drawRect.right, indicatorRect.top);
                yTextRect.set(drawRect.left, drawRect.top, drawRect.left + yAxisTextWidth, xTextRect.top);
                centerRect.set(yTextRect.right, drawRect.top, drawRect.right, xTextRect.top);
            }
        }
        gridRect.set(centerRect.left, centerRect.top + yAxisTextPaint.getTextSize() * 2, centerRect.right, centerRect.bottom);
        int pageCount = (dragEnable || !style.isHorMatch()) ? maxPageCount : xAxisTextCount;
        float lastXAxisRatio = xAxisPositionRatio;
        float lastYAxisRatio = yAxisPositionRatio;
        if (xAxisTextCount > 0) {
            if (style.isStartFromOrigin()) {
                xAxisPositionRatio = centerRect.width() / (pageCount - 0.5f);
            } else {
                xAxisPositionRatio = centerRect.width() / pageCount;
            }
        } else {
            xAxisPositionRatio = 0;
        }
        yAxisValueRatio = gridRect.height() / (yAxisMaxValue - yAxisMinValue);
        yAxisPositionRatio = gridRect.height() / (yAxisValueCount - 1);
        if (lastXAxisRatio <= 0) {
            lastXAxisRatio = xAxisPositionRatio;
        }
        if (lastYAxisRatio <= 0) {
            lastYAxisRatio = yAxisPositionRatio;
        }
        float x;
        float y;
        for (int i = 0; i < xAxisTextCount; i++) {
            boolean lastShow = i < lastXValueCount;
            ChartAxis axis = verChartAxiss.get(i);
            if (!lastShow) {
                x = centerRect.left + (style.isStartFromOrigin() ? i : (i + 0.5f)) * lastXAxisRatio;
                axis.movePointTo(x, xTextRect.centerY() + xAxisTextPaint.getTextSize() / 2, false);
                axis.moveLineTo(x, centerRect.bottom, x, centerRect.bottom, false);
            }
            x = centerRect.left + (style.isStartFromOrigin() ? i : (i + 0.5f)) * xAxisPositionRatio;
            axis.movePointTo(x, xTextRect.centerY() + xAxisTextPaint.getTextSize() / 2, true);
            y = centerRect.bottom - yAxisPositionRatio * (yAxisValueCount - 1);
            axis.moveLineTo(x, centerRect.bottom, x, y, true);
            axis.moveLineColor(ChartUtils.updateAlpha(style.getGridLineColor(), 0), false);
            axis.moveLineColor(ChartUtils.updateAlpha(style.getGridLineColor(), 60), lastShow);
            axis.getLine().setLineType(style.getVerLineType());
        }
        lastXValueCount = xAxisTextCount;
        for (int i = 0; i < yAxisValueCount; i++) {
            ChartAxis axis = horChartAxiss.get(i);
            boolean lastShow = i < lastYValueCount;
            if (!lastShow) {

            }
            y = centerRect.bottom - yAxisPositionRatio * i;
            axis.movePointTo(yTextRect.right - yTextOffset, y, false);
            axis.moveLineTo(centerRect.left, y, centerRect.right, y, false);
            axis.moveLineColor(ChartUtils.updateAlpha(style.getGridLineColor(), 0), false);
            axis.moveLineColor(ChartUtils.updateAlpha(style.getGridLineColor(), 60), true);
            axis.getLine().setLineType(style.getHorLineType());
        }
        boolean lastShow = false;
        if (xAxisLine == null) {
            xAxisLine = new ChartLine();
        } else {
            lastShow = true;
            xAxisLine.setLength(0);
        }
        xAxisLine.moveColorTo(style.getAxisLineColor(), lastShow);
        xAxisLine.addPoint(gridRect.left, gridRect.bottom, lastShow);
        xAxisLine.addPoint(gridRect.right, gridRect.bottom, lastShow);
        lastShow = false;
        if (yAxisLine == null) {
            yAxisLine = new ChartLine();
        } else {
            lastShow = true;
            yAxisLine.setLength(0);
        }
        yAxisLine.moveColorTo(style.getAxisLineColor(), lastShow);
        yAxisLine.addPoint(gridRect.left, gridRect.bottom, lastShow);
        yAxisLine.addPoint(gridRect.left, gridRect.top, lastShow);
        lastYValueCount = yAxisValueCount;
        if (dragEnable) {
            maxDragDistance = (xAxisTextCount - maxPageCount) * xAxisPositionRatio;
        }
    }

    private float measureIndicatorTextHeight() {
        return indicatorTextPaint.getTextSize() * 2.0f;
    }

    private void drawYAxis(Canvas canvas) {
        for (int i = 0; i < yAxisValueCount; i++) {
            if (i > 0) {
                ChartLine line = horChartAxiss.get(i).getLine();
                drawChartLine(canvas, line, horLinePaint, 0, 0);
            }
            if (style.isShowYAxisText()) {
                canvas.drawText(horChartAxiss.get(i).getText(), horChartAxiss.get(i).getPoint().getCurX() - horChartAxiss.get(i).textWidth, horChartAxiss.get(i).getPoint().getCurY() + yAxisTextPaint.getTextSize() / 2, yAxisTextPaint);
            }
        }
        canvas.drawText(unitText, yTextRect.left, horChartAxiss.get(yAxisValueCount - 1).getPoint().getCurY() - yAxisTextPaint.getTextSize(), yAxisTextPaint);
        if (style.isShowYAxis()) {
            drawChartLine(canvas, yAxisLine, axisPaint, 0, 0);
        }
    }

    private void drawXAxis(Canvas canvas) {
        float x;
        for (int i = 0; i < xAxisTextCount; i++) {
            x = verChartAxiss.get(i).getPoint().getCurX() - dragDistance;
            if (style.isShowXAxisText()) {
                if (x >= centerRect.left) {
                    float width = xAxisTextPaint.measureText(verChartAxiss.get(i).getText());
                    canvas.drawText(verChartAxiss.get(i).getText(), x - width / 2, verChartAxiss.get(i).getPoint().getCurY(), xAxisTextPaint);
                }
            }
            if (style.isShowXAxisScale()) {
                x = x + 0.5f * xAxisPositionRatio;
                if (x > centerRect.left) {
                    canvas.drawLine(x, xTextRect.top, x, xTextRect.top + xAxisTextPaint.getTextSize() / 3, axisPaint);
                }
            }
            ChartLine line = verChartAxiss.get(i).getLine();
            if (line.getMinX() - getDragDistance() > centerRect.left) {
                drawChartLine(canvas, line, verLinePaint, getDragDistance(), 0);
            }
        }
        if (style.isShowXAxis()) {
            drawChartLine(canvas, xAxisLine, axisPaint, 0, 0);
        }
    }

    public void drawLineByYAxisValue(Canvas canvas, float value, Paint paint) {
        float x = centerRect.right;
        float y = centerRect.bottom - yAxisValueRatio * (value - horChartAxiss.get(0).getValue());
        drawLine(canvas, centerRect.left, y, x, y, paint);
    }

    public float getYByValue(float value) {
        return centerRect.bottom - yAxisValueRatio * (value - horChartAxiss.get(0).getValue());
    }

    private void drawIndicator(Canvas canvas) {
        if (style.getIndicatorAlign() != null && indicators != null && indicators.size() > 0) {
            String name;
            float totalWidth = measureIndicatorTotalWidth(indicatorTextPaint, indicators);
            float startX = 0;
            if (style.getIndicatorAlign().isAlignCenter()) {
                startX = indicatorRect.width() > totalWidth ? (indicatorRect.width() - totalWidth) / 2 : 0;
            } else if (style.getIndicatorAlign().isAlignRight()) {
                startX = indicatorRect.width() > totalWidth ? indicatorRect.width() - totalWidth : 0;
            }
            for (int i = 0; i < indicators.size(); i++) {
                name = indicators.get(i).getText();
                float width = indicatorTextPaint.measureText(name);
                indicatorPaint.setColor(indicators.get(i).getColor());
                if (indicators.get(i).getType() == IndicatorType.POINT) {
                    ChartUtils.drawPoint(canvas, indicatorPaint, indicators.get(i).getPointType(), indicatorRect.left + startX + 15, indicatorRect.centerY(), 6);
                } else if (indicators.get(i).getType() == IndicatorType.LINE) {
                    canvas.drawLine(indicatorRect.left + startX, indicatorRect.centerY(), indicatorRect.left + startX + 30, indicatorRect.centerY(), indicatorPaint);
                } else if (indicators.get(i).getType() == IndicatorType.DASH_LINE) {
                    dashLinePaint.setColor(indicators.get(i).getColor());
                    drawLine(canvas, indicatorRect.left + startX, indicatorRect.centerY(), indicatorRect.left + startX + 30, indicatorRect.centerY(), dashLinePaint);
                } else if (indicators.get(i).getType() == IndicatorType.LINE_POINT) {
                    canvas.drawLine(indicatorRect.left + startX, indicatorRect.centerY(), indicatorRect.left + startX + 30, indicatorRect.centerY(), indicatorPaint);
                    ChartUtils.drawPoint(canvas, indicatorPaint, indicators.get(i).getPointType(), indicatorRect.left + startX + 15, indicatorRect.centerY(), 6);
                }
                startX += 40;
                canvas.drawText(name, indicatorRect.left + startX, indicatorRect.centerY() + indicatorTextPaint.getTextSize() / 2, indicatorTextPaint);
                startX += (width + 40);
            }
        }
    }

    private float measureIndicatorTotalWidth(Paint paint, List<ChartIndicator> indicators) {
        float width = 0;
        for (int i = 0; i < indicators.size(); i++) {
            if (i > 0) {
                width += 40;
            }
            width += (paint.measureText(indicators.get(i).getText()) + 40);
        }
        return width;
    }

    @Override
    protected void onAnimationUpdate(float animatorValue) {
        super.onAnimationUpdate(animatorValue);
        for (int i = 0; i < verChartAxiss.size(); i++) {
            verChartAxiss.get(i).updateAnimatorValue(animatorValue);
        }
        for (int i = 0; i < horChartAxiss.size(); i++) {
            horChartAxiss.get(i).updateAnimatorValue(animatorValue);
        }
        xAxisLine.updateAnimatorValue(animatorValue);
        yAxisLine.updateAnimatorValue(animatorValue);
    }

    public float getAxisPositionRatio() {
        return xAxisPositionRatio;
    }

    public float getYAxisValueRatio() {
        return yAxisValueRatio;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public ChartAxis getSelectPositionAxis() {
        if (selectPosition >= 0) {
            return verChartAxiss.get(selectPosition);
        }
        return null;
    }

    public RectF getCenterRect() {
        return centerRect;
    }

    public float getDragDistance() {
        return dragDistance;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                drag = false;
                if (dragEnable) {
                lastDistance = getDragDistance();
                    if (touchSlop == 0) {
                        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                    }
                }
                return super.onTouchEvent(event) || true;
            case MotionEvent.ACTION_MOVE:
                if (dragEnable && !drag) {
                    if (Math.abs(startX - event.getX()) >= touchSlop) {
                        drag = true;
                    }
                }
                if (drag) {
                    dragDistance = lastDistance - event.getX() + startX;
                    dragDistance = Math.max(0, dragDistance);
                    dragDistance = Math.min(maxDragDistance, dragDistance);
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!drag) {
                    if (xAxisTextCount > 0) {
                        int position = -1;
                        for (int i = 0; i < lastXValueCount; i++) {
                            boolean clickGrid = gridRect.contains(event.getX(), event.getY());
                            if (clickGrid) {
                                float distance = verChartAxiss.get(i).getPoint().getCurX() - getDragDistance() - event.getX();
                                if (distance < -xAxisPositionRatio / 3) {
                                    position = -1;
                                } else if (distance >= -xAxisPositionRatio / 3 && distance <= xAxisPositionRatio / 3) {
                                    position = i;
                                    break;
                                } else if (distance > xAxisPositionRatio / 3) {
                                    position = -1;
                                    break;
                                }
                            } else {
                                position = -1;
                            }
                        }
                        if (position >= 0) {
                            if (selectPosition >= 0) {
                                verChartAxiss.get(selectPosition).getLine().moveColorTo(ChartUtils.updateAlpha(style.getGridLineColor(), 60), false);
                            }
                            selectPosition = position;
                            if (selectPosition >= 0) {
                                verChartAxiss.get(selectPosition).getLine().moveColorTo(style.getGridLineColor(), false);
                            }
                            postInvalidate();
                        }
                    }
                }
                break;
        }
        return drag || super.onTouchEvent(event);
    }

    public void setSelectedPainter(ChartPainter<AxisChart> selectedPainter) {
        this.selectedPainter = selectedPainter;
    }

    @Override
    public int getMaxPageCount() {
        return maxPageCount;
    }

    @Override
    public int getXAxisTextCount() {
        return xAxisTextCount;
    }

    @Override
    public int getYAxisValueCount() {
        return yAxisValueCount;
    }

    @Override
    public String getXAxisText(int position) {
        return verChartAxiss.get(position).getText();
    }

    @Override
    public float getYAxisMinValue() {
        return yAxisMinValue;
    }

    @Override
    public float getYAxisMaxValue() {
        return yAxisMaxValue;
    }

    @Override
    public int getGroupCount() {
        return groupCount;
    }

    @Override
    public float getValue(int groupPosition, int xAxisPosition) {
        return values[groupPosition][xAxisPosition];
    }

    @Override
    public String getGroupName(int columnPosition) {
        return groupNames.get(columnPosition);
    }

    @Override
    public List<ChartIndicator> getChartIndicators() {
        return indicators;
    }

    public void setDrawListener(AxisChartDrawListener<AxisChart> drawListener) {
        this.drawListener = drawListener;
    }
}
