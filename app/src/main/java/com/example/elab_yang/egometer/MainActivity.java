package com.example.elab_yang.egometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setStatusbar();
        set();
    }

    // 툴바
    public void setToolbar() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
    }

    // 상태바 색 변경
    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    // 객체 생성
    public void set(){
        // 장치 추가 버튼
        Button add_device = (Button) findViewById(R.id.add_device);
        add_device.setOnClickListener(this);
    }

    // 클릭 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 장치 추가하러 ㄱㄱ
            case R.id.add_device:
                startActivity(new Intent(MainActivity.this, DeviceChooseActivity.class));
                break;
        }
    }
}
