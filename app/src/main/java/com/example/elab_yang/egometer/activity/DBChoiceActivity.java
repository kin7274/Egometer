package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.elab_yang.egometer.IActivityBasicSetting;
import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.activity.ergometer.EGOgetDBActivity;
import com.example.elab_yang.egometer.activity.treadmill.TREADScanActivIty;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DBChoiceActivity extends AppCompatActivity implements IActivityBasicSetting {

    @BindView(R.id.select_ergometer)
    RelativeLayout select_ergometer;

    @BindView(R.id.select_treadmill)
    RelativeLayout select_treadmill;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_choice);
        initSetting();
    }

    @OnClick({R.id.select_ergometer, R.id.select_treadmill})
    void Click(View v) {
        switch (v.getId()) {
            case R.id.select_ergometer:
                Toast.makeText(getApplicationContext(), "에르고미터 운동 기록을 열겠습니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, EGOgetDBActivity.class));
                break;

            case R.id.select_treadmill:
                Toast.makeText(getApplicationContext(), "트레드밀 운동 기록을 열겠습니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, TREADScanActivIty.class));
                break;
        }
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
        removeStatebar();
    }

    public void removeStatebar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}