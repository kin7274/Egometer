package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.elab_yang.egometer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Random;

public class ViewChartActivity extends AppCompatActivity {
    String date = "";
    LineChart line_chart;
    private LineDataSet lineDataSet;
    private LineData lineData;
    private ArrayList<Entry> entry = new ArrayList<>();
    int cnt = 0;
    int yData = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_chartview);
        // Date READ
        Intent intent = getIntent();
        date = intent.getExtras().getString("DATE");
        setToolbar();
        setStatusbar();

        line_chart = (LineChart) findViewById(R.id.line_chart);
        initChart();
        setChart();
    }

    public void setToolbar() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
        TextView bar_title = (TextView) findViewById(R.id.bar_title);
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
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

        // 0 < random < 1 사이의 실수
//        double random = Math.random();

        Random rand = new Random();
        // 1 ~ 100 사이 숫자
//        for (int i = 0; i < 3; i++) {
//            int p = rand.nextInt(100) + 1;
//            float floatP = (float) p;
//
//            entry.add(new Entry(cnt, floatP));
//            cnt = cnt + 10;
//        }

        float cnt1 = 0;
        float cnt2 = 20;
        float cnt3 = 40;

        // p1 : 110 ~ 130
//        int p1 = rand.nextInt(130) + 110;
        int p1 = rand.nextInt(65) + 55;
        float floatP1 = (float) p1;

        // p2 : 80 ~ 90
//        int p2 = rand.nextInt(90) + 80;
        int p2 = rand.nextInt(45) + 40;
        float floatP2 = (float) p2;

        // p3 : 100 ~ 110
//        int p3 = rand.nextInt(110) + 100;
        int p3 = rand.nextInt(55) + 50;
        float floatP3 = (float) p3;

        entry.add(new Entry(cnt1, floatP1));
        entry.add(new Entry(cnt2, floatP2));
        entry.add(new Entry(cnt3, floatP3));

//        entry.add(new Entry(0, 130));
//        entry.add(new Entry(10, 90));
//        entry.add(new Entry(20, 110));
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
