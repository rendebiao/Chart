package com.rdb.chart.column;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.rdb.chart.ChartUtils;
import com.rdb.chart.axis.AxisChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 2016/12/19 0019.
 */

public class ColumnChart extends AxisChart<ColumnChartAdapter, ColumnChartStyle> implements ColumnChartInterface {

    private Paint textPaint;
    private Paint columnPaint;
    private int animatorCount;
    private float lastXAxisRatio;
    private int lastMaxPageCount;
    private int lastXAxisTextCount;
    private List<List<ChartColumn>> columnLists = new ArrayList<>();

    public ColumnChart(Context context) {
        this(context, null);
    }

    public ColumnChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        columnPaint = new Paint();
        columnPaint.setAntiAlias(true);
    }

    @Override
    protected void onDrawChart(Canvas canvas, RectF chartRect) {
        super.onDrawChart(canvas, chartRect);
        canvas.save();
        canvas.clipRect(getCenterRect());
        drawColumn(canvas);
        canvas.restore();
        notifyChildDrawComplete(canvas);
    }

    private void drawColumn(Canvas canvas) {
        int color;
        float left, right;
        for (int i = 0; i < groupCount; i++) {
            for (int j = 0; j < columnLists.get(i).size(); j++) {
                ChartColumn column = columnLists.get(i).get(j);
                left = column.getLeft() - getDragDistance();
                right = column.getRight() - getDragDistance();
                if (left > getCenterRect().right) {
                    break;
                }
                if (left < getCenterRect().right || right >= getCenterRect().left) {
                    if (column.getTop() <= column.getBottom()) {
                        color = selectPosition == j ? column.getSelectedColor() : column.getColor();
                        columnPaint.setColor(color);
                        canvas.drawRect(column.getLeft() - getDragDistance(), column.getTop(), column.getRight() - getDragDistance(), column.getBottom(), columnPaint);
                        if (style.isShowValue() && !TextUtils.isEmpty(column.getText())) {
                            float width = textPaint.measureText(column.getText());
                            canvas.drawText(column.getText(), (column.getLeft() + column.getRight()) / 2 - getDragDistance() - width / 2, column.getTop() - textPaint.getTextSize() / 2, textPaint);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onAnimationUpdate(float animatorValue) {
        super.onAnimationUpdate(animatorValue);
        for (int i = 0; i < groupCount; i++) {
            for (int j = 0; j < animatorCount; j++) {
                columnLists.get(i).get(j).updateAnimatorValue(animatorValue);
            }
        }
    }

    @Override
    protected void onDatasetChanged(RectF chartRect, ColumnChartAdapter adapter) {
        super.onDatasetChanged(chartRect, adapter);
        textPaint.setTextSize(dpToPx(style.getYAxisTextSize()));
        textPaint.setColor(style.getYAxisTextColor());
        float xAxisRatio = xAxisTextCount > 0 ? getAxisPositionRatio() : lastXAxisRatio;
        float startOffset = style.isStartFromOrigin() ? 0 : 0.5f;
        float totalWidth = xAxisRatio * 0.6f;
        float columnWidth = totalWidth / (1.2f * groupCount - 0.2f);
        float columnSpace = 0.2f * columnWidth;
        float bottom = getCenterRect().bottom - dpToPx(style.getAxisLineWidth()) / 2;
        int totalCount = Math.max(xAxisTextCount, columnLists.size() > 0 ? columnLists.get(0).size() : 0);
        animatorCount = Math.min((int) (Math.max(lastMaxPageCount, maxPageCount) / 0.9f + 1), totalCount);
        boolean showToShow, showToHide, hideToShow, animator;
        float x, y, oldX;
        for (int i = 0; i < groupCount; i++) {
            List<ChartColumn> columns;
            if (columnLists.size() > i) {
                columns = columnLists.get(i);
            } else {
                columns = new ArrayList<>();
                columnLists.add(columns);
            }
            for (int j = 0; j < totalCount; j++) {
                animator = j < animatorCount;
                showToShow = j < lastXAxisTextCount && j < xAxisTextCount;
                showToHide = j < lastXAxisTextCount && j >= xAxisTextCount;
                hideToShow = j >= lastXAxisTextCount && j < xAxisTextCount;
                ChartColumn column;
                if (columns.size() > j) {
                    column = columns.get(j);
                } else {
                    column = new ChartColumn();
                    columns.add(column);
                }
                x = getCenterRect().left + xAxisRatio * (j + startOffset) - totalWidth / 2 + i * (columnWidth + columnSpace);
                if (showToShow || hideToShow) {
                    y = bottom - getYAxisValueRatio() * (values[i][j] - adapter.getYAxisMinValue());
                    if (hideToShow && animator) {
                        oldX = getCenterRect().left + (lastXAxisRatio <= 0 ? xAxisRatio : lastXAxisRatio) * (j + startOffset) - totalWidth / 2 + i * (columnWidth + columnSpace);
                        column.moveTo(oldX, false, bottom, false, oldX + columnWidth, false, bottom, false);
                    }
                    column.moveTo(x, animator, y, animator, x + columnWidth, animator, bottom, animator);
                    column.moveColorTo(adapter.getColumnColor(i, j), j < lastXAxisTextCount);
                    column.moveSelectedColorTo(adapter.getColumnSelectedColor(i, j), j < lastXAxisTextCount);
                    column.setText(ChartUtils.getText(values[i][j], style.getYAxisValueDecimal()));
                } else {
                    column.moveTo(x, showToHide, bottom, showToHide, x + columnWidth, showToHide, bottom, showToHide);
                    column.setText("");
                }
            }
        }
        lastXAxisRatio = xAxisRatio;
        lastMaxPageCount = maxPageCount;
        lastXAxisTextCount = xAxisTextCount;
    }

    @Override
    public int getColumnColor(int groupPosition, int xAxisPosition) {
        return columnLists.get(groupPosition).get(xAxisPosition).getColor();
    }

    @Override
    public int getColumnSelectedColor(int groupPosition, int xAxisPosition) {
        return columnLists.get(groupPosition).get(xAxisPosition).getSelectedColor();
    }
}
