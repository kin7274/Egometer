package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.elab_yang.egometer.R;

public class DeviceChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_choice);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 에르고미터 선택
        ConstraintLayout choice1_egometer = (ConstraintLayout) findViewById(R.id.choice1_egometer);
        choice1_egometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceChoiceActivity.this, EGOScanActivity.class));
            }
        });

        // 트레드밀 선택
        ConstraintLayout choice2_treadmill = (ConstraintLayout) findViewById(R.id.choice2_treadmill);
        choice2_treadmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceChoiceActivity.this, TREADScanActivIty.class));
            }
        });
    }
}
