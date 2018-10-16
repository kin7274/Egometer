package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;

public class IndoorBikeResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "IndoorBikeResultActivity";

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

    public void set() {
        // 인텐트 리시브
        Intent intent = getIntent();
        int time = intent.getExtras().getInt("result_time");
        String extra = intent.getExtras().getString("result_extra");
        String[] arr_data = extra.split("&");

        // 객체 생성
        TextView txt_time = (TextView) findViewById(R.id.txt_time);
        TextView txt_EI = (TextView) findViewById(R.id.txt_EI);
        TextView txt_AvgSpeed = (TextView) findViewById(R.id.txt_AvgSpeed);
        TextView txt_Distance = (TextView) findViewById(R.id.txt_Distance);
        TextView txt_AvgBPM = (TextView) findViewById(R.id.txt_AvgBPM);
        TextView txt_Kcal = (TextView) findViewById(R.id.txt_Kcal);

        Button hahahahahah_btn = (Button) findViewById(R.id.hahahahahah_btn);
        hahahahahah_btn.setOnClickListener(this);
        Button set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);
        Button ext_btn = (Button) findViewById(R.id.ext_btn);
        ext_btn.setOnClickListener(this);

        // setText
        txt_time.setText(String.valueOf(time) + "초");
        txt_EI.setText(arr_data[0]);
        txt_AvgSpeed.setText(arr_data[1]);
        txt_Distance.setText(arr_data[2]);
        txt_AvgBPM.setText(arr_data[3]);
        txt_Kcal.setText(arr_data[4]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 자랑하기
            case R.id.hahahahahah_btn:
                Toast.makeText(getApplicationContext(), "자랑을 해보아요~", Toast.LENGTH_SHORT).show();
                break;
            // DB에 저장
            case R.id.set_btn:
                Toast.makeText(getApplicationContext(), "DB에 저장해보아요~", Toast.LENGTH_SHORT).show();
                break;
            // 나가기
            case R.id.ext_btn:
                Toast.makeText(getApplicationContext(), "여기서 나가보아요~", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
