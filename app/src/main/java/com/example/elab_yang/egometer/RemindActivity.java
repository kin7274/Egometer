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
import android.widget.Toast;

import com.example.elab_yang.egometer.etc.EGZeroConst;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.elab_yang.egometer.etc.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;


// TODO: 2018-11-04 운동 전 혈당을 여기서 받으면 되겠다!
// TODO: 2018-11-04 운동 부하 검사 안했다면 하도록 권고
// TODO: 2018-11-04

public class RemindActivity extends AppCompatActivity implements IActivityBasicSetting {
    private static final String TAG = "RemindActivity";
    Context mContext;
    Handler mHandler;
    TextToSpeech tts;
    String deviceAddress = "";
    String before_bloodsugar = "";

    String AAAA = "";
    String AAA[] = {"", ""};

    @BindView(R.id.joogangdo_text)
    TextView joogangdo_text;

    @BindView(R.id.gogangdo_text)
    TextView gogangdo_text;

    @BindView(R.id.next_btn)
    Button next_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        mContext = this;
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
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
        // TODO: 2018-11-05 운동부하검사 안하고 들어오면 꺼짐
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String mydata = "";
        AAAA = pref.getString("mydata", mydata);
        Log.d(TAG, "onCreate: AAAA = " + AAAA);
        //
        if (!AAAA.equals("")) AAA = AAAA.split("/");

        Intent intent = getIntent();
        deviceAddress = intent.getExtras().getString(REAL_TIME_INDOOR_BIKE_DEVICE);
        
        String time = seekbar_data_convert(Integer.parseInt(AAA[4]));
        String speed = "시속 15에서 20키로미터";
        joogangdo_text.setText(time + "간 중강도로 속도 " + speed + ", 심박수 " + AAA[2] + "에서 " + AAA[3] + "를 유지하도록 하여야 합니다.");
        Log.d(TAG, "set: AAA[4] " + AAA[4]);
        Log.d(TAG, "set: Integer.parseInt(AAA[4]) " + Integer.parseInt(AAA[4]));

        String time1 = seekbar_data_convert(20 - Integer.parseInt(AAA[4]));
        String speed1 = "시속 20키로미터";
        gogangdo_text.setText(time1 + "간 고강도로 속도 " + speed1 + " 이상, 심박수 " + AAA[3] + " 이상을 유지하도록 하여야 합니다.");
    }

    @OnClick(R.id.next_btn)
    void Click() {
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
            intent.putExtra("mydata", AAAA);
            startActivity(intent);
            finish();
        })
                .setView(et)
                .create()
                .show();
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
