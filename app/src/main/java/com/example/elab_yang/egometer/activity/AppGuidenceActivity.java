package com.example.elab_yang.egometer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.elab_yang.egometer.R;

public class AppGuidenceActivity extends AppCompatActivity {
    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidence);
        TextView scene1 = (TextView) findViewById(R.id.scene1);
        TextView scene2 = (TextView) findViewById(R.id.scene2);
        TextView scene3 = (TextView) findViewById(R.id.scene3);
        scene1.setVisibility(View.VISIBLE);
        scene2.setVisibility(View.GONE);
        scene3.setVisibility(View.GONE);

        Button next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(v -> {
            if (flag == 0) {
                scene1.setVisibility(View.GONE);
                scene2.setVisibility(View.VISIBLE);
                scene3.setVisibility(View.GONE);
                flag = 1;
            } else if (flag == 1) {
                scene1.setVisibility(View.GONE);
                scene2.setVisibility(View.GONE);
                scene3.setVisibility(View.VISIBLE);
                flag = 0;
            }
        });
    }
}
