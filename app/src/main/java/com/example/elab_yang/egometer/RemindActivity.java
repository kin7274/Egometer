package com.example.elab_yang.egometer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class RemindActivity extends AppCompatActivity {
    Context mContext;
    TextView joogangdo_text, gogangdo_text;  // 중강도, 고강도 가이드

    // 실시간 운동 들어가기 전 내 운동 부하 검사 데이터를 간략히 한 번 보여주자
    // 음성 출력도 하자

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        mContext = this;
        setToolbar();
        setStatusbar();
        set();
    }

    public void setToolbar() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    public void set(){
        // 중강도
        String time, speed, bpm1, bpm2;
        joogangdo_text = (TextView) findViewById(R.id.joogangdo_text);
        joogangdo_text.setText("9분간 중강도로 속도 00-00km/h, 심박수 000-000를 유지하도록 하여야 합니다.");
        // 고강도
        String time1, speed1, bpm3;
        gogangdo_text = (TextView) findViewById(R.id.gogangdo_text);
        gogangdo_text.setText("1분간 고강도로 속도 00km/h 이상, 심박수 000 이상을 유지하도록 하여야 합니다.");
    }
}
