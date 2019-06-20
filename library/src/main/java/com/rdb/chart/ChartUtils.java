package com.rdb.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.rdb.chart.line.PointType;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by DB on 2016/12/22.
 */

public class ChartUtils {

    private static final int DEF_DIV_SCALE = 10;
    private static Path drawPath = new Path();
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    public static String getText(float value, int scale) {
        float newValue = new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(scale);
        return numberFormat.format(newValue);
    }

    public static float getCirclePointX(float centerX, float radius, float degree) {
        return (float) (centerX + radius * Math.cos(degree * Math.PI / 180));
    }

    public static float getCirclePointY(float centerY, float radius, float degree) {
        return (float) (centerY + radius * Math.sin(degree * Math.PI / 180));
    }

    public static float getDegree(float centerX, float centerY, float x, float y) {
        float xDistance = x - centerX;
        float yDistance = y - centerY;
        float degree = (float) (Math.atan(yDistance / xDistance) * 180 / Math.PI);
        if (xDistance < 0) {
            degree += 180;
        } else if (yDistance < 0) {
            degree += 360;
        }
        return degree;
    }

    public static int updateAlpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static void drawPoint(Canvas canvas, Paint pointPaint, PointType pointType, float centerX, float centerY, float radius) {
        if (pointType == PointType.ROUND) {
            canvas.drawCircle(centerX, centerY, radius, pointPaint);
        } else if (pointType == PointType.SQUARE) {
            canvas.drawRect(centerX - radius, centerY - radius, centerX + radius, centerY + radius, pointPaint);
        } else if (pointType == PointType.STAR) {
            float minRadius = radius / 2;
            drawPath.reset();
            drawPath.moveTo(centerX, centerY - radius);
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, minRadius, 306), ChartUtils.getCirclePointY(centerY, minRadius, 306));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 342), ChartUtils.getCirclePointY(centerY, radius, 342));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, minRadius, 18), ChartUtils.getCirclePointY(centerY, minRadius, 18));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 54), ChartUtils.getCirclePointY(centerY, radius, 54));
            drawPath.lineTo(centerX, centerY + minRadius);
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 126), ChartUtils.getCirclePointY(centerY, radius, 126));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, minRadius, 162), ChartUtils.getCirclePointY(centerY, minRadius, 162));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 198), ChartUtils.getCirclePointY(centerY, radius, 198));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, minRadius, 234), ChartUtils.getCirclePointY(centerY, minRadius, 234));
            drawPath.close();
            canvas.drawPath(drawPath, pointPaint);
            drawPath.reset();
        } else if (pointType == PointType.PENTAGON) {
            drawPath.reset();
            drawPath.moveTo(centerX, centerY - radius);
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 30), ChartUtils.getCirclePointY(centerY, radius, 30));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 150), ChartUtils.getCirclePointY(centerY, radius, 150));
            drawPath.close();
            canvas.drawPath(drawPath, pointPaint);
            drawPath.reset();
        } else if (pointType == PointType.TRIANGLE) {
            drawPath.reset();
            drawPath.moveTo(centerX, centerY - radius);
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 342), ChartUtils.getCirclePointY(centerY, radius, 342));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 54), ChartUtils.getCirclePointY(centerY, radius, 54));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 126), ChartUtils.getCirclePointY(centerY, radius, 126));
            drawPath.lineTo(ChartUtils.getCirclePointX(centerX, radius, 198), ChartUtils.getCirclePointY(centerY, radius, 198));
            drawPath.close();
            canvas.drawPath(drawPath, pointPaint);
            drawPath.reset();
        }
    }


    public static double add(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Double.toString(f1));
        BigDecimal b2 = new BigDecimal(Double.toString(f2));
        return b1.add(b2).floatValue();
    }

    public static float sub(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Double.toString(f1));
        BigDecimal b2 = new BigDecimal(Double.toString(f2));
        return b1.subtract(b2).floatValue();
    }

    public static float mul(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Double.toString(f1));
        BigDecimal b2 = new BigDecimal(Double.toString(f2));
        return b1.multiply(b2).floatValue();
    }

    public static float div(float f1, float f2) {
        return div(f1, f2, DEF_DIV_SCALE);
    }

    public static float div(float f1, float f2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(f1));
        BigDecimal b2 = new BigDecimal(Double.toString(f2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
