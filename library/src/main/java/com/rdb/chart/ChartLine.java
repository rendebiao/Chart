package com.rdb.chart;

import com.rdb.chart.line.LineType;
import com.rdb.chart.line.PointType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 2016/12/19 0019.
 */

public class ChartLine implements ChartAnimatable {

    int pointCount;
    ChartColor color;
    PointType pointType;
    LineType lineType = LineType.NORMAL;
    List<Point> points = new ArrayList<>();

    public ChartLine() {
        color = new ChartColor();
    }

    public ChartLine(int color) {
        this.color = new ChartColor(color);
    }

    public int addPoint(float x, float y, boolean animateTo) {
        Point point;
        if (points.size() > pointCount) {
            point = points.get(pointCount);
        } else {
            point = new Point();
            points.add(point);
        }
        point.movePointTo(x, y, animateTo);
        pointCount++;
        return pointCount - 1;
    }

    public void moveColorTo(int toColor, boolean animateTo) {
        color.moveTo(toColor, animateTo);
    }

    public Point getPoint(int position) {
        return points.get(position);
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public PointType getPointType() {
        return pointType;
    }

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }

    public int getPointCount() {
        return pointCount;
    }

    public int getMaxPointCount() {
        return points.size();
    }

    public float getMinX() {
        float x = Float.MIN_VALUE;
        for (int i = 0; i < pointCount; i++) {
            x = Math.max(x, points.get(i).getCurX());
        }
        return x;
    }

    public float getMaxX() {
        float x = Float.MAX_EXPONENT;
        for (int i = 0; i < pointCount; i++) {
            x = Math.min(x, points.get(i).getCurX());
        }
        return x;
    }

    public float getMinY() {
        float y = Float.MIN_VALUE;
        for (int i = 0; i < pointCount; i++) {
            y = Math.max(y, points.get(i).getCurY());
        }
        return y;
    }

    public float getMaxY() {
        float y = Float.MAX_EXPONENT;
        for (int i = 0; i < pointCount; i++) {
            y = Math.min(y, points.get(i).getCurY());
        }
        return y;
    }

    public int getColor() {
        return color.getColor();
    }

    public void setLength(int length) {
        pointCount = length;
        if (points.size() < length) {
            for (int i = points.size(); i < length; i++) {
                points.add(new Point());
            }
        }
    }

    public void clear() {
        pointCount = 0;
        points.clear();
    }

    @Override
    public boolean updateAnimatorValue(float animatorValue) {
        for (int i = 0; i < pointCount; i++) {
            points.get(i).updateAnimatorValue(animatorValue);
        }
        color.updateAnimatorValue(animatorValue);
        return true;
    }
}
