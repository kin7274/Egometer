package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.elab_yang.egometer.R;

public class ProfileActivity extends AppCompatActivity {

    String[] user_data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        EditText user_name = (EditText) findViewById(R.id.user_name);
        EditText user_age = (EditText) findViewById(R.id.user_age);
        EditText user_blood_min = (EditText) findViewById(R.id.user_blood_min);
        EditText user_blood_max = (EditText) findViewById(R.id.user_blood_max);
        EditText user_weight = (EditText) findViewById(R.id.user_weight);
        EditText user_height = (EditText) findViewById(R.id.user_height);
        Button set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(v -> {
            user_data[0] = user_name.getText().toString();
            user_data[1] = user_age.getText().toString();
            user_data[2] = user_blood_min.getText().toString();
            user_data[3] = user_blood_max.getText().toString();
            user_data[4] = user_weight.getText().toString();
            user_data[5] = user_height.getText().toString();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.putExtra("user_name", user_data[0]);
            finish();
        });
    }
}