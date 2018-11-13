package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.elab_yang.egometer.DeleteDataBaseActivity;
import com.example.elab_yang.egometer.IActivityBasicSetting;
import com.example.elab_yang.egometer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity implements IActivityBasicSetting {

    @BindView(R.id.layout1)
    RelativeLayout layout1;

    @BindView(R.id.layout2)
    RelativeLayout layout2;

    @BindView(R.id.layout3)
    RelativeLayout layout3;

    @BindView(R.id.layout4)
    RelativeLayout layout4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
        setToolbar();
        setStatusbar();
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

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4})
    void Click(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                // 개인정보 입력
                startActivity(new Intent(this, EditProfileActivity.class));
                break;

            case R.id.layout2:
                // Database 삭제
                startActivity(new Intent(this, DeleteDataBaseActivity.class));
                break;

            case R.id.layout3:
                // 앱 평가하기
                Toast.makeText(getApplicationContext(), "업데이트 예정", Toast.LENGTH_SHORT).show();
                break;

            case R.id.layout4:
                // 앱 소개하기
                Toast.makeText(getApplicationContext(), "업데이트 예정", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
