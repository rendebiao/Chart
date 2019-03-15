package com.rdb.chart.column;

import com.rdb.chart.ChartIndicator;
import com.rdb.chart.IndicatorType;
import com.rdb.chart.axis.AxisChartAdapter;
import com.rdb.chart.line.PointType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 2016/12/19 0019.
 */

public abstract class ColumnChartAdapter extends AxisChartAdapter implements ColumnChartInterface {

    private List<ChartIndicator> indicators = new ArrayList<>();

    @Override
    public List<ChartIndicator> getChartIndicators() {
        if (indicators.size() == 0) {
            indicators = new ArrayList<>();
            for (int i = 0; i < getGroupCount(); i++) {
                indicators.add(new ChartIndicator(getColumnColor(i, 0), getGroupName(i), IndicatorType.POINT, PointType.SQUARE));
            }
        }
        return indicators;
    }

    @Override
    public void notifyDatasetChanged() {
        indicators.clear();
        super.notifyDatasetChanged();
    }
}
