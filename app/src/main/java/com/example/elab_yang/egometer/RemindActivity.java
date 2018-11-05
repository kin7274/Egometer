package com.example.elab_yang.egometer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.elab_yang.egometer.etc.EGZeroConst;

import java.util.Locale;

import static com.example.elab_yang.egometer.etc.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;


// TODO: 2018-11-04 운동 전 혈당을 여기서 받으면 되겠다!
// TODO: 2018-11-04 운동 부하 검사 안했다면 하도록 권고
// TODO: 2018-11-04

public class RemindActivity extends AppCompatActivity {
    private static final String TAG = "RemindActivity";
    Context mContext;
    TextView joogangdo_text, gogangdo_text;  // 중강도, 고강도 가이드
    Handler mHandler;
    TextToSpeech tts;
    Button next_btn;
    String deviceAddress = "";
    String before_bloodsugar = "";

    String data2;
    String AAAA = "";
    String AAA[] = {"", ""};
    // 실시간 운동 들어가기 전 내 운동 부하 검사 데이터를 간략히 한 번 보여주자
    // 음성 출력도 하자

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        mContext = this;

        // TODO: 2018-11-05 운동부하검사 안하고 들어오면 꺼짐
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String mydata = "";
        AAAA = pref.getString("mydata", mydata);
        AAA = AAAA.split("/");
//        AAA[0] = stage
//        AAA[1] = 최대 심박수
//        AAA[2] = 중강도 시간

        // 장치이름을 받아와서
        Intent intent = getIntent();
        deviceAddress = intent.getExtras().getString(REAL_TIME_INDOOR_BIKE_DEVICE);

        setToolbar();
        setStatusbar();
        set();
        guide_speech();
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
        // 중강도
//        String time = "9";
        String time = seekbar_data_convert(Integer.parseInt(AAA[2]));
        String speed = "시속 15에서 20키로미터";
//        AAA[0] = stage
//        AAA[1] = 최대 심박수
//        AAA[2] = 중강도 시간

        double min = 0.5;
        double max = 0.7;

        int bpm1_str =  (int) (Integer.parseInt(AAA[1]) * min);
        int bpm2_str =  (int) (Integer.parseInt(AAA[1]) * max);
        String bpm1 = String.valueOf(bpm1_str);
        String bpm2 = String.valueOf(bpm2_str);
//        String bpm1 = "75";
//        String bpm2 = "105";
        joogangdo_text = (TextView) findViewById(R.id.joogangdo_text);
//        joogangdo_text.setText("9분간 중강도로 속도 00-00km/h, 심박수 000-000를 유지하도록 하여야 합니다.");
        joogangdo_text.setText(time + "간 중강도로 속도 " + speed + ", 심박수 " + bpm1 + "에서 " + bpm2 + "를 유지하도록 하여야 합니다.");
        // 고강도
//        String time1 = "1";
        Log.d(TAG, "set: AAA[2] " + AAA[2]);
        Log.d(TAG, "set: Integer.parseInt(AAA[2]) " + Integer.parseInt(AAA[2]));

        String time1 = seekbar_data_convert(20-Integer.parseInt(AAA[2]));
        String speed1 = "시속 20키로미터";
        gogangdo_text = (TextView) findViewById(R.id.gogangdo_text);
//        gogangdo_text.setText("1분간 고강도로 속도 00km/h 이상, 심박수 000 이상을 유지하도록 하여야 합니다.");
        gogangdo_text.setText(time1 + "간 고강도로 속도 " + speed1 + " 이상, 심박수 " + bpm2 + " 이상을 유지하도록 하여야 합니다.");

        data2 = time + "/" + bpm1 + "/" + bpm2;

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: 운동 전 혈당 입력");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("운동전 혈당을 입력해주세요")
                    .setMessage("혈당은 몇인가요?");
            final EditText et = new EditText(this);
            builder.setPositiveButton("YES", (dialog, which) -> {
                // 운동 전 혈당을 받아서
                before_bloodsugar = et.getText().toString();
                // 인텐트
                Intent intent = new Intent(RemindActivity.this, IndoorBikeRealTimeActivity.class);
                intent.putExtra(REAL_TIME_INDOOR_BIKE_DEVICE, deviceAddress);
                intent.putExtra("before_bloodsugar", before_bloodsugar);
                intent.putExtra("data2", data2);
                startActivity(intent);
                finish();
            })
                    .setView(et)
                    .create()
                    .show();
        });
    }

    public void guide_speech() {
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.0f);
            }
        });
        mHandler = new Handler();
        runOnUiThread(() -> {
            // 1초 후
            mHandler.postDelayed(() -> {
                try {
                    tts.speak(joogangdo_text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                    tts.speak(gogangdo_text.getText().toString(), TextToSpeech.QUEUE_ADD, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        });
    }

    String seekbar_data_convert(int num) {
        if (num % 2 == 1) {
            // 나머지값이 있으면 이건 홀수니까
            if (num / 2 == 0) {
                return "30초";
            } else {
                return num / 2 + "분 30초";
            }
        }
        // 짝수
        if (num / 2 == 0) {
            return "-";
        } else {
            return num / 2 + "분";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.stop();
        tts.shutdown();
    }
}
