package com.example.elab_yang.egometer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.elab_yang.egometer.R;

public class IntroActivity extends AppCompatActivity {
    SharedPreferences pref;
    @Override
    protected void onStart() {
        super.onStart();
        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView next_btn = (TextView) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(v -> {
            // 시작하기
            Intent intent = new Intent(IntroActivity.this, DeviceScanActivity.class);
            startActivity(intent);
        });
    }
}
