package com.rdb.chart;

/**
 * Created by DB on 2016/12/27.
 */

public enum IndicatorAlign {
    BOTTOM_CENTER, BOTTOM_LEFT, BOTTOM_RIGHT, TOP_CENTER, TOP_LEFT, TOP_RIGHT;

    public boolean isAlignTop() {
        return this == TOP_LEFT || this == TOP_RIGHT || this == TOP_CENTER;
    }

    public boolean isAlignBottom() {
        return this == BOTTOM_LEFT || this == BOTTOM_RIGHT || this == BOTTOM_CENTER;
    }

    public boolean isAlignLeft() {
        return this == BOTTOM_LEFT || this == TOP_LEFT;
    }

    public boolean isAlignRight() {
        return this == BOTTOM_RIGHT || this == TOP_RIGHT;
    }

    public boolean isAlignCenter() {
        return this == BOTTOM_CENTER || this == TOP_CENTER;
    }
}
