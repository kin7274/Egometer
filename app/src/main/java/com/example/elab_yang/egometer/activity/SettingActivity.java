package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.DeleteDataBaseActivity;
import com.example.elab_yang.egometer.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setToolbar();
        setStatusbar();
        set();
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

    public void set() {
        ConstraintLayout layout1 = (ConstraintLayout) findViewById(R.id.layout1);
        layout1.setOnClickListener(this);
        ConstraintLayout layout2 = (ConstraintLayout) findViewById(R.id.layout2);
        layout2.setOnClickListener(this);
        ConstraintLayout layout3 = (ConstraintLayout) findViewById(R.id.layout3);
        layout3.setOnClickListener(this);
        ConstraintLayout layout4 = (ConstraintLayout) findViewById(R.id.layout4);
        layout4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                // 개인정보 입력
//                Toast.makeText(getApplicationContext(), "개인정보 수정", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.layout2:
                // Database 삭제
//                Toast.makeText(getApplicationContext(), "Database 삭제", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(SettingActivity.this, DeleteDataBaseActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout3:
                // 앱 평가하기
                Toast.makeText(getApplicationContext(), "업데이트 예정 : 앱 평가하기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout4:
                // 앱 소개하기
                Toast.makeText(getApplicationContext(), "업데이트 예정 : 앱 소개하기", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
