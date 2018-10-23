package com.example.elab_yang.egometer.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.model.CardItem;
import com.example.elab_yang.egometer.model.DB;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class BloodDiaryActivity extends AppCompatActivity {
    LineChart line_chart;
    private LineDataSet lineDataSet;
    private LineData lineData;

    Context mContext;
    // 에르고미터 디비
    DB db;
    SQLiteDatabase sql;
    String str;

    // Entry는 float형만
    private ArrayList<Entry> entry = new ArrayList<>();
    // x축, y축
//    float xData, yData;
    int cnt = 0;
    int yData = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blooddiary);
        mContext = this;
        line_chart = (LineChart) findViewById(R.id.line_chart);
        initChart();
//        setChart(yData);
        setChart();

        db = new DB(this);
        sql = db.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(sql, "tb_egometer");
        long rows_count = (long) count;
        sql.close();
        Toast.makeText(getApplicationContext(), "행의 갯수 : " + rows_count, Toast.LENGTH_SHORT).show();
    }

    // 차트 속성 설정
    public void initChart() {
        YAxis yAxis = line_chart.getAxisRight();
        yAxis.setEnabled(false);
        XAxis xAxis = line_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public void setChart() {
        // arr.add(new Entry(x축, y축))
//         entry.add(new Entry(xData, yData));
        entry.add(new Entry(0, 130));
        entry.add(new Entry(10, 90));
        entry.add(new Entry(20, 110));
//        entry.add(new Entry(cnt, (float) yData));
        // 범례
        lineDataSet = new LineDataSet(entry, "Blood Sugar");
        // CUBIC_BEZIER : 스무스하게
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // 원으로 점 표시O
        lineDataSet.setDrawCircles(true);
        // 레이블 표시X
        lineDataSet.setDrawValues(false);
        // 차트 스무스하게; 간격
        lineDataSet.setCubicIntensity(0.2f);
//        lineDataSet.setCubicIntensity(0.1f);
        // 색상 RED
        lineDataSet.setColor(Color.RED);
        lineData = new LineData(lineDataSet);
        // SET
        line_chart.setData(lineData);
        // X축 cnt 증가에 따라 이동
//        line_chart.moveViewToX(lineData.getEntryCount());
        // 수정
        line_chart.notifyDataSetChanged();
//        cnt++;
//        yData += 10;
    }
}
