package com.rdb.chart.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rdb.chart.IndicatorAlign;
import com.rdb.chart.axis.AxisCharTableSelectedPainter;
import com.rdb.chart.axis.AxisChartBubbleSelectedPainter;
import com.rdb.chart.column.ColumnChart;
import com.rdb.chart.column.ColumnChartAdapter;
import com.rdb.chart.column.ColumnChartStyle;
import com.rdb.chart.dial.DialChart;
import com.rdb.chart.dial.DialChartAdapter;
import com.rdb.chart.dial.DialChartStyle;
import com.rdb.chart.line.LineChart;
import com.rdb.chart.line.LineChartAdapter;
import com.rdb.chart.line.LineChartStyle;
import com.rdb.chart.line.LineType;
import com.rdb.chart.line.PointType;
import com.rdb.chart.pie.PieChart;
import com.rdb.chart.pie.PieChartAdapter;
import com.rdb.chart.pie.PieChartStyle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int color;
    private int group;
    private int xCount;
    private int yCount;
    private float dialValue;
    private float maxValue;
    private float minValue;
    private String[] texts;
    private int[] colors=new int[]{0xff0069a6,0xffff00ff,0xffb92120};
    private int[] alphaColors=new int[]{0x800069a6,0x80ff00ff,0x80b92120};
    private float[] pieValues;
    private float[][] axisValues;
    private Random random = new Random();
    private Button reload;
    private PieChart pieChart;
    private DialChart dialChart;
    private LineChart lineChart;
    private ColumnChart columnChart;
    private PieChartStyle pieChartStyle;
    private DialChartStyle dialChartStyle;
    private ColumnChartStyle columnChartStyle;
    private LineChartStyle lineChartStyle;
    private PieAdapter pieAdapter;
    private LineAdapter lineAdapter;
    private DialAdapter dialAdapter;
    private ColumnAdapter columnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        initViews();
        initStyle();
        loadData();
    }

    private void initViews() {
        reload = findViewById(R.id.reload);
        pieChart = findViewById(R.id.pieChart);
        dialChart = findViewById(R.id.dialChart);
        lineChart = findViewById(R.id.lineChart);
        columnChart = findViewById(R.id.columnChart);
        pieAdapter = new PieAdapter();
        dialAdapter = new DialAdapter();
        lineAdapter = new LineAdapter();
        columnAdapter = new ColumnAdapter();
        pieChart.setAdapter(pieAdapter);
        dialChart.setAdapter(dialAdapter);
        lineChart.setAdapter(lineAdapter);
        columnChart.setAdapter(columnAdapter);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        lineChart.setSelectedPainter(new AxisCharTableSelectedPainter(this));
        columnChart.setSelectedPainter(new AxisChartBubbleSelectedPainter(this));
    }

    private void initStyle() {
        color = getResources().getColor(R.color.colorAccent);
        lineChartStyle = new LineChartStyle();
        lineChartStyle.setDashIntervals(4, 4, 4, 4);
        lineChartStyle.setStartFromOrigin(false);
        lineChartStyle.setAxisLineWidth(0.5f);
        lineChartStyle.setGridLineWidth(0.5f);
        lineChartStyle.setShowYAxis(true);
        lineChartStyle.setVerLineType(LineType.DASH);
        lineChartStyle.setHorLineType(LineType.DASH);
        lineChartStyle.setIndicatorAlign(IndicatorAlign.BOTTOM_CENTER);
        lineChartStyle.setAxisLineColor(color);
        lineChartStyle.setGridLineColor(color);
        lineChartStyle.setXAxisTextColor(color);
        lineChartStyle.setYAxisTextColor(color);
        lineChartStyle.setIndicatorTextColor(color);
        lineChartStyle.setHorMatch(true);
        lineChart.setStyle(lineChartStyle);
        columnChartStyle = new ColumnChartStyle();
        columnChartStyle.setDashIntervals(4, 4, 4, 4);
        columnChartStyle.setAxisLineWidth(0.5f);
        columnChartStyle.setGridLineWidth(0.5f);
        columnChartStyle.setShowYAxis(true);
        lineChartStyle.setVerLineType(LineType.DASH);
        columnChartStyle.setHorLineType(LineType.DASH);
        columnChartStyle.setAxisLineColor(color);
        columnChartStyle.setGridLineColor(color);
        columnChartStyle.setXAxisTextColor(color);
        columnChartStyle.setYAxisTextColor(color);
        columnChartStyle.setIndicatorTextColor(color);
        columnChartStyle.setIndicatorAlign(IndicatorAlign.BOTTOM_CENTER);
        columnChartStyle.setHorMatch(true);
        columnChartStyle.setShowValue(true);
        columnChart.setStyle(columnChartStyle);
        dialChartStyle = new DialChartStyle();
        dialChartStyle.setSmallScaleCount(3);
        dialChartStyle.setRingWidth(3);
        dialChartStyle.setScaleTextSize(6);
        dialChartStyle.setUnitTextSize(6);
        dialChartStyle.setNameTextSize(6);
        dialChartStyle.setValueTextSize(14);
        dialChartStyle.setLargeScaleHeight(3);
        dialChartStyle.setLargeScaleWidth(1);
        dialChartStyle.setSmallScaleHeight(1.5f);
        dialChartStyle.setSmallScaleWidth(0.5f);
        dialChartStyle.setDialColor(color);
        dialChart.setStyle(dialChartStyle);
        pieChartStyle = new PieChartStyle();
        pieChartStyle.setTextSize(10);
        pieChartStyle.setValueDecimal(2);
        pieChart.setStyle(pieChartStyle);
    }

    private void loadData() {
        group = 3;
        xCount = 5 + random.nextInt(10);
        yCount = 2 + random.nextInt(4);
        dialValue = 100 + random.nextInt(100);
        maxValue = 200;
        minValue = 0;
        texts = new String[xCount];
        pieValues = new float[xCount];
        axisValues = new float[group][xCount];
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < group; j++) {
                axisValues[j][i] = random.nextFloat() * 200;
            }
            texts[i] = "2017-1-" + i;
            pieValues[i] = random.nextFloat() * 200;
        }
        pieAdapter.notifyDataSetChanged();
        dialAdapter.notifyDataSetChanged();
        lineAdapter.notifyDataSetChanged();
        columnAdapter.notifyDataSetChanged();
    }

    private class LineAdapter extends LineChartAdapter {

        @Override
        public String getValueUnit() {
            return "单位";
        }

        @Override
        public String getChartName() {
            return "折线图";
        }

        @Override
        public int getMaxPageCount() {
            return 8;
        }

        @Override
        public int getXAxisTextCount() {
            return xCount;
        }

        @Override
        public int getYAxisValueCount() {
            return yCount;
        }

        @Override
        public String getXAxisText(int position) {
            return texts[position];
        }

        @Override
        public float getYAxisMinValue() {
            return minValue;
        }

        @Override
        public float getYAxisMaxValue() {
            return maxValue;
        }

        @Override
        public int getGroupCount() {
            return group;
        }

        @Override
        public float getValue(int groupPosition, int xAxisPosition) {
            return axisValues[groupPosition][xAxisPosition];
        }

        @Override
        public String getGroupName(int groupPosition) {
            return "折线" + groupPosition;
        }

        @Override
        public PointType getPointType(int linePosition) {
            return PointType.values()[linePosition];
        }

        @Override
        public LineType getLineType(int linePosition) {
            return LineType.NORMAL;
        }

        @Override
        public int getLineColor(int linePosition) {
            return alphaColors[linePosition];
        }

        @Override
        public int getPointColor(int linePosition) {
            return alphaColors[linePosition];
        }

        @Override
        public int getSelectPointColor(int linePosition) {
            return colors[linePosition];
        }
    }

    private class DialAdapter extends DialChartAdapter {

        @Override
        public String getValueUnit() {
            return "单位";
        }

        @Override
        public String getChartName() {
            return "仪表图";
        }

        @Override
        public float getMinValue() {
            return minValue;
        }

        @Override
        public float getMaxValue() {
            return maxValue;
        }

        @Override
        public float getCurValue() {
            return dialValue;
        }

        @Override
        public float getTotalDegree() {
            return 240;
        }

        @Override
        public boolean showScaleText(int largeScalePosition) {
            return largeScalePosition % 2 == 0;
        }
    }

    private class PieAdapter extends PieChartAdapter {

        @Override
        public String getValueUnit() {
            return null;
        }

        @Override
        public String getChartName() {
            return "饼图";
        }

        @Override
        public int getCount() {
            return xCount;
        }

        @Override
        public float getValue(int position) {
            return pieValues[position];
        }

        @Override
        public int getColor(int position) {
            return alphaColors[position % 3];
        }

        @Override
        public int getSelectedColor(int position) {
            return colors[position % 3];
        }

        @Override
        public String getText(int position) {
            return texts[position];
        }
    }
    private class ColumnAdapter extends ColumnChartAdapter {

        @Override
        public String getValueUnit() {
            return "单位";
        }

        @Override
        public String getChartName() {
            return "柱状图";
        }

        @Override
        public int getMaxPageCount() {
            return 8;
        }

        @Override
        public int getXAxisTextCount() {
            return xCount;
        }

        @Override
        public int getYAxisValueCount() {
            return yCount;
        }

        @Override
        public String getXAxisText(int position) {
            return texts[position];
        }

        @Override
        public float getYAxisMinValue() {
            return minValue;
        }

        @Override
        public float getYAxisMaxValue() {
            return maxValue;
        }

        @Override
        public int getGroupCount() {
            return group;
        }

        @Override
        public float getValue(int groupPosition, int xAxisPosition) {
            return axisValues[groupPosition][xAxisPosition];
        }

        @Override
        public String getGroupName(int groupPosition) {
            return "柱状" + groupPosition;
        }

        @Override
        public int getColumnColor(int groupPosition, int xAxisPosition) {
            return alphaColors[groupPosition];
        }

        @Override
        public int getColumnSelectedColor(int groupPosition, int xAxisPosition) {
            return colors[groupPosition];
        }
    }
}
