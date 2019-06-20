package com.rdb.chart.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.rdb.chart.Chart;
import com.rdb.chart.ChartUtils;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends Chart<PieChartAdapter, PieChartStyle> implements PieChartInterface {

    private float startX;
    private float startY;
    private int curCount;
    private int lastCount;
    private boolean drag;
    private int touchSlop;
    private int selectedPosition;
    private Paint textPaint;
    private Paint sectorPaint;
    private RectF ovalRectf;
    private List<ChartSector> sectorList = new ArrayList<>();

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        sectorPaint = new Paint();
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDrawChart(Canvas canvas, RectF chartRect) {
        float textMargin = dpToPx(style.getTextMargin());
        float lineLength1 = dpToPx(style.getIndicatorLineLength1());
        float lineLength2 = dpToPx(style.getIndicatorLineLength2());
        float hPadding = 0;
        float vPadding = lineLength1 + textPaint.getTextSize() / 2;
        for (int i = 0; i < curCount; i++) {
            hPadding = Math.max(hPadding, textPaint.measureText(getText(i)));
        }
        hPadding += (lineLength1 + lineLength2);
        float radius = Math.min(chartRect.width() / 2 - hPadding, chartRect.height() / 2 - vPadding);
        float centerX = chartRect.width() / 2;
        float centerY = chartRect.height() / 2;
        if (ovalRectf == null) {
            ovalRectf = new RectF();
        }
        ovalRectf.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float startAngle = 0;
        int count = Math.max(lastCount, curCount);
        float totalValue = 0;
        for (int i = 0; i < count; i++) {
            totalValue += sectorList.get(i).getValue();
        }
        for (int i = 0; i < count; i++) {
            float sweep = sectorList.get(i).getValue() * 360f / totalValue;
            if (startAngle < 360 && sweep > 0) {
                if (startAngle + sweep > 360) {
                    sweep = 360 - startAngle;
                }
                sectorPaint.setColor(i == selectedPosition ? sectorList.get(i).getSelectedColor() : sectorList.get(i).getColor());
                canvas.drawArc(ovalRectf, startAngle, sweep, true, sectorPaint);
                if (i < curCount) {
                    float xP = ChartUtils.getCirclePointX(centerX, radius, startAngle + sweep / 2);
                    float yP = ChartUtils.getCirclePointY(centerY, radius, startAngle + sweep / 2);
                    float xEdP = ChartUtils.getCirclePointX(centerX, radius + lineLength1, startAngle + sweep / 2);
                    float yEdP = ChartUtils.getCirclePointY(centerY, radius + lineLength1, startAngle + sweep / 2);
                    boolean right = startAngle + sweep / 2 >= 270 || startAngle + sweep / 2 <= 90;
                    float xLast = right ? xEdP + lineLength2 : xEdP - lineLength2;
                    canvas.drawLine(xP, yP, xEdP, yEdP, textPaint);
                    canvas.drawLine(xEdP, yEdP, xLast, yEdP, textPaint);
                    String text = getText(i);
                    if (right) {
                        canvas.drawText(text, xLast + textMargin, yEdP + textPaint.getTextSize() / 2, textPaint);
                    } else {
                        float textWidth = textPaint.measureText(text);
                        canvas.drawText(text, xLast - textWidth - textMargin, yEdP + textPaint.getTextSize() / 2, textPaint);
                    }
                }
            }
            startAngle += sweep;
        }
    }

    @Override
    protected void onDataSetChanged(RectF chartRect, PieChartAdapter adapter) {
        super.onDataSetChanged(chartRect, adapter);
        textPaint.setTextSize(dpToPx(style.getTextSize()));
        lastCount = curCount;
        curCount = adapter.getCount();
        int count = Math.max(lastCount, curCount);
        for (int i = 0; i < count; i++) {
            ChartSector sector;
            if (sectorList.size() <= i) {
                sector = new ChartSector();
                sectorList.add(sector);
            } else {
                sector = sectorList.get(i);
            }
            if (i < curCount) {
                sector.setName(adapter.getText(i));
                sector.moveColorTo(adapter.getColor(i), true);
                sector.moveSelectedColorTo(adapter.getSelectedColor(i), true);
                if (i >= lastCount) {
                    sector.moveValueTo(0, false);
                }
                sector.moveValueTo(adapter.getValue(i), true);
            } else {
                sector.moveValueTo(0, true);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                drag = false;
                if (touchSlop == 0) {
                    touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                }
                return super.onTouchEvent(event) || true;
            case MotionEvent.ACTION_MOVE:
                if (!drag) {
                    if (Math.abs(startX - event.getX()) >= touchSlop) {
                        drag = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!drag) {
                    if (curCount > 0) {
                        int position = -1;
                        float totalValue = 0;
                        float touchDegree = ChartUtils.getDegree(ovalRectf.centerX(), ovalRectf.centerY(), startX, startY);
                        for (int i = 0; i < curCount; i++) {
                            totalValue += sectorList.get(i).getValue();
                        }
                        float startAngle = 0;
                        for (int i = 0; i < curCount; i++) {
                            float sweep = sectorList.get(i).getValue() * 360f / totalValue;
                            if (startAngle <= touchDegree && startAngle + sweep > touchDegree) {
                                position = i;
                                selectedPosition = position;
                                break;
                            }
                            startAngle += sweep;
                        }
                        if (position >= 0) {
                            postInvalidate();
                        }
                    }
                }
                break;
        }
        return drag || super.onTouchEvent(event);
    }

    @Override
    protected void onAnimationUpdate(float animatorValue) {
        super.onAnimationUpdate(animatorValue);
        int count = Math.max(lastCount, curCount);
        for (int i = 0; i < count; i++) {
            sectorList.get(i).updateAnimatorValue(animatorValue);
        }
    }

    @Override
    public int getCount() {
        return curCount;
    }

    @Override
    public float getValue(int position) {
        return sectorList.get(position).getValue();
    }

    @Override
    public int getColor(int position) {
        return sectorList.get(position).getColor();
    }

    @Override
    public int getSelectedColor(int position) {
        return sectorList.get(position).getSelectedColor();
    }

    @Override
    public String getText(int position) {
        return sectorList.get(position).getName();
    }
}
