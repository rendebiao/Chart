package com.rdb.chart;

/**
 * Created by DB on 2016/12/20.
 */

public class ChartDataSetObserver extends android.database.DataSetObserver {

    Chart chart;

    public ChartDataSetObserver(Chart chart) {
        this.chart = chart;
    }

    @Override
    public void onChanged() {
        chart.notifyDatasetChanged();
    }

    @Override
    public void onInvalidated() {
        super.onInvalidated();
    }
}
