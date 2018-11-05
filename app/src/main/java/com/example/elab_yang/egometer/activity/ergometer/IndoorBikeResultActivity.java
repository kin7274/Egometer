package com.example.elab_yang.egometer.activity.ergometer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.CustomDialog;
import com.example.elab_yang.egometer.IndoorBikeRealTimeActivity;
import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.activity.treadmill.TimelineActivity;
import com.example.elab_yang.egometer.model.CardItem;
import com.example.elab_yang.egometer.model.DB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.elab_yang.egometer.etc.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;

public class IndoorBikeResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "IndoorBikeResult";
    Context mContext;
    DB db;
    SQLiteDatabase sql;
    int time = 0;
    // 운동 전 혈당
    String before_bloodsugar = "";
    // 운동 후 혈당
    String after_bloodsugar = "";
    String[] arr_data;

    TextView txt_time, txt_AvgSpeed, txt_Distance, txt_AvgBPM, txt_Kcal, txt_before_bloodsugar, txt_after_bloodsugar;

    Handler mHandler;
    private TextToSpeech tts;

    String num;
    String memo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_bike_result);
        mContext = this;
        setToolbar();
        setStatusbar();
        set();
        eval();
        db = new DB(this);

//        showDialog();
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
        before_bloodsugar = intent.getExtras().getString("before_bloodsugar");
        after_bloodsugar = intent.getExtras().getString("after_bloodsugar");
        String extra = intent.getExtras().getString("result_extra");
        arr_data = extra.split("&");

        // 객체 생성
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_AvgSpeed = (TextView) findViewById(R.id.txt_AvgSpeed);
        txt_Distance = (TextView) findViewById(R.id.txt_Distance);
        txt_AvgBPM = (TextView) findViewById(R.id.txt_AvgBPM);
        txt_Kcal = (TextView) findViewById(R.id.txt_Kcal);
        txt_before_bloodsugar = (TextView) findViewById(R.id.txt_before_bloodsugar);
        txt_after_bloodsugar = (TextView) findViewById(R.id.txt_after_bloodsugar);

        Button set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);

        // 클릭 이벤트
        txt_time.setOnClickListener(this);
        txt_AvgSpeed.setOnClickListener(this);
        txt_Distance.setOnClickListener(this);
        txt_AvgBPM.setOnClickListener(this);
        txt_Kcal.setOnClickListener(this);
        txt_before_bloodsugar.setOnClickListener(this);
        txt_after_bloodsugar.setOnClickListener(this);

        // setText
        txt_time.setText(String.valueOf(time) + "초");
        txt_AvgSpeed.setText(arr_data[0]);
        txt_Distance.setText(arr_data[1]);
        txt_AvgBPM.setText(arr_data[2]);
        txt_Kcal.setText(arr_data[3]);
        txt_before_bloodsugar.setText(before_bloodsugar);
        txt_after_bloodsugar.setText(after_bloodsugar);
//        txt_after_bloodsugar.setText(after_bloodsugar);
    }

    // DB에 저장한다
    public void setDB() {
        sql = db.getWritableDatabase();
        // 현재시간 : String getTime
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        sql.execSQL(String.format("INSERT INTO tb_egometer VALUES(null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')"
                , getTime, String.valueOf(time) + "초", arr_data[0], arr_data[1], arr_data[2], arr_data[3], before_bloodsugar, after_bloodsugar, num, memo));
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

            case R.id.txt_time:
                showDialog(txt_time);
                break;

            case R.id.txt_AvgSpeed:
                showDialog(txt_AvgSpeed);

                break;
            case R.id.txt_Distance:
                showDialog(txt_Distance);

                break;
            case R.id.txt_AvgBPM:

                break;
            case R.id.txt_Kcal:

                break;
            case R.id.txt_before_bloodsugar:

                break;

            case R.id.txt_after_bloodsugar:

                break;
        }
    }

    public void showDialog(TextView id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(IndoorBikeResultActivity.this).inflate(R.layout.edit_box3, null, false);
        builder.setView(view);
        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
        final EditText edit1 = (EditText) view.findViewById(R.id.edit1);
        ButtonSubmit.setText("삽입");
        final AlertDialog dialog = builder.create();
        ButtonSubmit.setOnClickListener(v1 -> {
            String strEdit1 = edit1.getText().toString();
            // TODO: 2018-11-04 R.id마다 뒤에 단위 추가되도록
            id.setText(strEdit1);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void eval() {
        mHandler = new Handler();
        runOnUiThread(() -> {
            // 1초 후
            mHandler.postDelayed(() -> {
                try {
                    Log.d(TAG, "eval: 띠용");
                    ddiyong();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        });
    }

    public void ddiyong() {
        final CustomDialog customdialog = new CustomDialog(this);
        customdialog.show();
        customdialog.setOnDismissListener(dialog -> {
//            Log.d(TAG, "ddiyong: num" + num);
            // int 시크바 가져옴
            String num = String.valueOf(CustomDialog.getNum());
            Log.d(TAG, "ddiyong: num : " + num);
            // String 메모값 가져옴
            String memo = CustomDialog.getMemo();
            Log.d(TAG, "ddiyong: memo : " + memo);
        });
    }
}
