package com.example.elab_yang.egometer.activity.ergometer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.model.DB;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IndoorBikeResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "IndoorBikeResult";
    DB db;
    SQLiteDatabase sql;
    int time;
    String[] arr_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_bike_result);
        setToolbar();
        setStatusbar();
        set();
        db = new DB(this);
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
    public void set() {
        // 인텐트 리시브
        Intent intent = getIntent();
        time = intent.getExtras().getInt("result_time");
        String extra = intent.getExtras().getString("result_extra");
        arr_data = extra.split("&");

        // 객체 생성
        TextView txt_time = (TextView) findViewById(R.id.txt_time);
        TextView txt_AvgSpeed = (TextView) findViewById(R.id.txt_AvgSpeed);
        TextView txt_Distance = (TextView) findViewById(R.id.txt_Distance);
        TextView txt_AvgBPM = (TextView) findViewById(R.id.txt_AvgBPM);
        TextView txt_Kcal = (TextView) findViewById(R.id.txt_Kcal);

        Button set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);

        // setText
        txt_time.setText(String.valueOf(time) + "초");
        txt_AvgSpeed.setText(arr_data[1]);
        txt_Distance.setText(arr_data[2]);
        txt_AvgBPM.setText(arr_data[3]);
        txt_Kcal.setText(arr_data[4]);
    }

    // DB에 저장한다
    public void setDB(){
        sql = db.getWritableDatabase();
        // 현재시간 : String getTime
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        sql.execSQL(String.format("INSERT INTO tb_egometer VALUES(null, '%s','%s','%s','%s','%s','%s')", getTime, String.valueOf(time) + "초", arr_data[1], arr_data[2], arr_data[3], arr_data[4]));
        Toast.makeText(getApplicationContext(), "저장했어유", Toast.LENGTH_SHORT).show();
        sql.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // DB에 저장
            case R.id.set_btn:
                Toast.makeText(getApplicationContext(), "DB에 저장해보아요~", Toast.LENGTH_SHORT).show();
                setDB();
                finish();
                break;
        }
    }
}
