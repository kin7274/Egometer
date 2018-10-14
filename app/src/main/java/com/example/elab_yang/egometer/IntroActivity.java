package com.example.elab_yang.egometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(v -> {
            // 시작하기
            Intent intent = new Intent(IntroActivity.this, DeviceScanActivity.class);
            startActivity(intent);
        });
    }
}
