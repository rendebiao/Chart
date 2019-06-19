package com.rdb.chart.line;

import com.rdb.chart.ChartIndicator;
import com.rdb.chart.IndicatorType;
import com.rdb.chart.axis.AxisChartAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 2016/12/19 0019.
 */

public abstract class LineChartAdapter extends AxisChartAdapter implements LineChartInterface {

    private List<ChartIndicator> indicators = new ArrayList<>();

    @Override
    public List<ChartIndicator> getChartIndicators() {
        if (indicators.size() == 0) {
            indicators = new ArrayList<>();
            for (int i = 0; i < getGroupCount(); i++) {
                indicators.add(new ChartIndicator(getLineColor(i), getGroupName(i), IndicatorType.LINE_POINT, getPointType(i)));
            }
        }
        return indicators;
    }

    @Override
    public void notifyDataSetChanged() {
        indicators.clear();
        super.notifyDataSetChanged();
    }
}
