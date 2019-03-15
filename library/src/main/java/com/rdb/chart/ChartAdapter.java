package com.rdb.chart;


import android.database.DataSetObservable;
import android.database.DataSetObserver;

/**
 * Created by DB on 2016/12/20.
 */

public abstract class ChartAdapter implements ChartInterface {

    private final DataSetObservable dataSetObservable = new DataSetObservable();

    public void registerDataSetObserver(DataSetObserver observer) {
        dataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        dataSetObservable.unregisterObserver(observer);
    }

    public void notifyDatasetChanged() {
        dataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        dataSetObservable.notifyInvalidated();
    }

}
