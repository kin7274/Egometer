package com.example.elab_yang.egometer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.elab_yang.egometer.R;

public class IndoorBikeResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_bike_result);
        setToolbar();
        set();
    }

    // 툴바
    public void setToolbar() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
    }

    // 객체 생성
    public void set() {
        // 날짜
        TextView txt_now = (TextView) findViewById(R.id.txt_now);
//        txt_now.setText(str);

        // 시간
        TextView txt_time = (TextView) findViewById(R.id.txt_time);
//        txt_time.setText(str);

        // 운동강도
        TextView txt_EI = (TextView) findViewById(R.id.txt_EI);
//        txt_EI.setText(str);

        // 평균속도
        TextView txt_AvgSpeed = (TextView) findViewById(R.id.txt_AvgSpeed);
//        txt_AvgSpeed.setText(str);

        // 이동거리
        TextView txt_Distance = (TextView) findViewById(R.id.txt_Distance);
//        txt_Distance.setText(str);

        // 평균BPM
        TextView txt_AvgBPM = (TextView) findViewById(R.id.txt_AvgBPM);
//        txt_AvgBPM.setText(str);

        // 소모 칼로리
        TextView txt_Kcal = (TextView) findViewById(R.id.txt_Kcal);
//        txt_Kcal.setText(str);
    }
}
