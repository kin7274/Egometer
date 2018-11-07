package com.example.elab_yang.egometer.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.elab_yang.egometer.R;

public class TestResult2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result2);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String mydata = "";
        String AAAA = pref.getString("mydata", mydata);

        TextView dididi = (TextView) findViewById(R.id.dididi);

//        String mydata = stage + "/" + bpm1_str + "/" + bpm2_str+ "/" + String.valueOf(num);
        // [0] : 단계
        // [1] : min bpm
        // [2] : max bpm
        // [3] : 중강도 시간

        dididi.setText(AAAA);
    }
}
