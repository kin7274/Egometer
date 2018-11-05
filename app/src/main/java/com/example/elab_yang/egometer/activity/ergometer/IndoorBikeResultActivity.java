package com.example.elab_yang.egometer.activity.ergometer;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.TestStartBeforeActivity;
import com.example.elab_yang.egometer.activity.TestResultActivity;
import com.example.elab_yang.egometer.model.DB;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    int index;

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
        num = String.valueOf(CustomDialog.getnNum());
        Log.d(TAG, "ddiyong: num : " + num);
        // String 메모값 가져옴
        memo = CustomDialog.getMemo();
        Log.d(TAG, "ddiyong: memo : " + memo);

        sql = db.getWritableDatabase();
        // 현재시간 : String getTime
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        sql.execSQL(String.format("INSERT INTO tb_egometer VALUES(null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')"
                , getTime, String.valueOf(time) + "초", arr_data[0], arr_data[1], arr_data[2], arr_data[3], before_bloodsugar, after_bloodsugar, num, memo));
        Log.d(TAG, "setDB: num " + num);
        Log.d(TAG, "setDB: memo " + memo);
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
            num = String.valueOf(CustomDialog.getnNum());

            Log.d(TAG, "ddiyong: num!!! " + num);
            if((num.equals("0"))||(num.equals("1"))){
                // 쉬워, 할만해
                // 강도가 너무 낮은거야
                Toast.makeText(getApplicationContext(), "낮은 강도의 운동을 하고계셔요", Toast.LENGTH_SHORT).show();
                onCreateDialog(1);
                // 운동처방
                // 1. 최대심박 범위를 0.1 올린다.
                // 2. 부하검사를 다시 진행한다.
                // 3. 중강도 구간을 30초 줄인다.
            } else if((num.equals("2"))){
                // 보통
                // 강도 적정
                Toast.makeText(getApplicationContext(), "적정하군요@@~!@@", Toast.LENGTH_SHORT).show();
//                onCreateDialog();
            } else {
                // 힘들어, 듸절거 같애
                // 강도 너무 높아
                Toast.makeText(getApplicationContext(), "너무 높은 강도의 운동을 하고계셔요", Toast.LENGTH_SHORT).show();
                onCreateDialog(2);
                // 운동처방
                // 1. 최대심박 범위를 0.1 내린다.
                // 2. 부하검사를 다시 진행한다.
                // 3. 중강도 구간을 30초 늘린다.
            }
//            Log.d(TAG, "ddiyong: num" + num);
            // int 시크바 가져옴
//            num = String.valueOf(CustomDialog.getnNum());
//            Log.d(TAG, "ddiyong: num : " + num);
//            // String 메모값 가져옴
//            memo = CustomDialog.getMemo();
//            Log.d(TAG, "ddiyong: memo : " + memo);
        });
    }

    protected Dialog onCreateDialog(int id) {
        final String [] items = {"1. 최대심박 범위를 0.1씩 올린다.", "2. 운동 부하 검사 재실시", "3. 중강도 구간을 30초 늘이고 고강도 구간을 줄인다."};
        final String [] items2 = {"1. 최대심박 범위를 0.1씩 내린다.", "2. 운동 부하 검사 재실시", "3. 중강도 구간을 30초 줄이고 고강도 구간을 늘린다."};
        AlertDialog.Builder builder = new AlertDialog.Builder(IndoorBikeResultActivity.this);
        builder.setTitle("운동 처방전 - ");
        if(id == 1){
            builder.setTitle("운동 처방전 - 낮은 운동 강도")
            .setSingleChoiceItems(items, 0, (DialogInterface dialog, int which) -> {
                Toast.makeText(IndoorBikeResultActivity.this, items[which], Toast.LENGTH_SHORT).show();
                index = which;
//            dialog.dismiss(); // 누르면 바로 닫히는 형태
            })
                    .setPositiveButton("확인", (DialogInterface dialog, int which) -> {
//                if(index == 0){
//                    // 심박 범위를 내린다.
//                    Toast.makeText(getApplicationContext(), "심박 범위 0.1씩 증가", Toast.LENGTH_SHORT).show();
//                    // 수치변화를 다이얼로그로 보여주자
//                } else if(index == 1){
//                    // 운동 부하 검사 재실시
//                    Toast.makeText(getApplicationContext(), "운동 부하 검사 재실시", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(IndoorBikeResultActivity.this, TestStartBeforeActivity.class);
//                    startActivity(intent);
//                } else if(index == 2){
//                    // 운동 구간 변화
//                    Toast.makeText(getApplicationContext(), "중강도 구간을 30초 늘이고 고강도 구간을 줄인다", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(IndoorBikeResultActivity.this, TestResultActivity.class);
//                    startActivity(intent);
//                }
                dialog.dismiss(); // 누르면 바로 닫히는 형태
            })
                    .show();
        } else {
            builder.setTitle("운동 처방전 - 높은 운동 강도")
                    .setSingleChoiceItems(items, 0, (DialogInterface dialog, int which) -> {
                        Toast.makeText(IndoorBikeResultActivity.this, items2[which], Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onCreateDialog: which " + which);
                        index = which;
//            dialog.dismiss(); // 누르면 바로 닫히는 형태
                    })
                    .setPositiveButton("확인", (DialogInterface dialog, int which) -> {
//                        Log.d(TAG, "onCreateDialog: which!!! " + which);
//                        Log.d(TAG, "onCreateDialog: index!!! " + index);
////                        Log.d(TAG, "onCreateDialog: items2[which]!!! " + items2[which]);
//                        if(index == 0){
//                            Log.d(TAG, "onCreateDialog: 여기");
//                            // 심박 범위를 내린다.
//                            Toast.makeText(getApplicationContext(), "심박 범위 0.1씩 감소", Toast.LENGTH_SHORT).show();
//                            // 수치변화를 다이얼로그로 보여주자
//                            dialog.dismiss();
////                            finish();
//                        } else if(index == 1){
//                            Log.d(TAG, "onCreateDialog: 여기1");
//                            // 운동 부하 검사 재실시
//                            Toast.makeText(getApplicationContext(), "운동 부하 검사 재실시", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(IndoorBikeResultActivity.this, TestStartBeforeActivity.class);
//                            startActivity(intent);
//                            dialog.dismiss();
//                            // TODO: 2018-11-05 저장은 할꺼야?
//                            finish();
//                        } else if(index == 2){
//                            // 운동 구간 변화
//                            Log.d(TAG, "onCreateDialog: 여기2");
//                            Toast.makeText(getApplicationContext(), "중강도 구간을 30초 줄이고 고강도 구간을 늘린다", Toast.LENGTH_SHORT).show();
//                            Intent intent2 = new Intent(IndoorBikeResultActivity.this, TestResultActivity.class);
//                            startActivity(intent2);
//                            dialog.dismiss();
//                            // TODO: 2018-11-05 저장은 할꺼야?
//                            finish();
//                        }
                        dialog.dismiss(); // 누르면 바로 닫히게
                    })
                    .show();
        }

        return null;
    }
}
