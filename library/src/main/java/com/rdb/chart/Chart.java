package com.rdb.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.rdb.chart.line.LineType;

/**
 * Created by DB on 2016/12/20.
 */
public abstract class Chart<T extends ChartAdapter, V extends ChartStyle> extends View implements ChartInterface, ValueAnimator.AnimatorUpdateListener {

    protected V style;
    protected String chartName;
    protected String valueUnit;
    protected Path drawPath = new Path();
    protected RectF chartRect = new RectF();
    protected DashPathEffect dashPathEffect;
    private T adapter;
    private float density;
    private float animatorValue;
    private ChartDataSetObserver chartDataSetObserver;
    private ValueAnimator moveAnimator = ValueAnimator.ofFloat(0, 1);

    private Runnable notifyDataSetRunnable = new Runnable() {
        @Override
        public void run() {
            notifyDataSetChanged();
        }
    };

    public Chart(Context context) {
        this(context, null);
    }

    public Chart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        moveAnimator.setDuration(500);
        moveAnimator.addUpdateListener(this);
        density = context.getResources().getDisplayMetrics().density;
    }

    public T getAdapter() {
        return adapter;
    }

    public void setAdapter(T adapter) {
        if (this.adapter != null && chartDataSetObserver != null) {
            this.adapter.unregisterDataSetObserver(chartDataSetObserver);
        }
        this.adapter = adapter;
        if (adapter != null) {
            if (chartDataSetObserver == null) {
                chartDataSetObserver = new ChartDataSetObserver(this);
            }
            adapter.registerDataSetObserver(chartDataSetObserver);
        }
        notifyDataSetChanged();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            post(notifyDataSetRunnable);
        }
    }

    @Override
    protected final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (adapter != null && style != null && !chartRect.isEmpty()) {
            onDrawChart(canvas, chartRect);
        }
    }

    protected abstract void onDrawChart(Canvas canvas, RectF chartRect);

    @Override
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        animatorValue = (float) valueAnimator.getAnimatedValue();
        onAnimationUpdate(animatorValue);
        postInvalidate();
    }

    protected void onAnimationUpdate(float animatorValue) {

    }

    void notifyDataSetChanged() {
        if (adapter != null && style != null) {
            chartRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            float[] dashIntervals = new float[style.getDashIntervals().length];
            for (int i = 0; i < style.getDashIntervals().length; i++) {
                dashIntervals[i] = dpToPx(style.getDashIntervals()[i]);
            }
            dashPathEffect = new DashPathEffect(dashIntervals, 1);
            if (!chartRect.isEmpty()) {
                if (moveAnimator.isRunning()) {
                    moveAnimator.cancel();
                }
                onDataSetChanged(chartRect, adapter);
                moveAnimator.start();
            }
        }
    }

    protected void onDataSetChanged(RectF chartRect, T adapter) {
        valueUnit = adapter.getValueUnit();
        chartName = adapter.getChartName();
    }

    public void drawChartLine(Canvas canvas, ChartLine line, Paint paint, float xOffset, float yOffset) {
        if (line.getLineType() != null && line.getPointCount() > 1) {
            drawPath.reset();
            for (int i = 0; i < line.getPointCount(); i++) {
                Point point = line.getPoint(i);
                if (i == 0) {
                    drawPath.moveTo(point.getCurX() - xOffset, point.getCurY() - yOffset);
                } else {
                    drawPath.lineTo(point.getCurX() - xOffset, point.getCurY() - yOffset);
                }
            }
            paint.setColor(line.getColor());
            paint.setPathEffect(line.getLineType() == LineType.DASH ? dashPathEffect : null);
            canvas.drawPath(drawPath, paint);
        }
    }

    public void drawLine(Canvas canvas, float startX, float startY, float endX, float endY, Paint paint) {
        drawPath.reset();
        drawPath.moveTo(startX, startY);
        drawPath.lineTo(endX, endY);
        canvas.drawPath(drawPath, paint);
    }

    protected float dpToPx(float dp) {
        return dp * density;
    }

    public float getAnimatorValue() {
        return animatorValue;
    }

    public RectF getChartRect() {
        return chartRect;
    }

    public V getStyle() {
        return style;
    }

    public void setStyle(V style) {
        this.style = (V) style.clone();
        notifyDataSetChanged();
    }

    @Override
    public String getValueUnit() {
        return valueUnit;
    }

    @Override
    public String getChartName() {
        return chartName;
    }

    public interface OnPointDrawListener {
        void onPointDraw(Canvas canvas, Point point);
    }
}
