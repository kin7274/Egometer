package com.example.elab_yang.egometer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestStartBeforeActivity extends AppCompatActivity implements IActivityBasicSetting {
    private TextToSpeech tts;
    Context mContext;
    private Handler mHandler;

    @BindView(R.id.message1)
    TextView message1;

    @BindView(R.id.message2)
    TextView message2;

    @BindView(R.id.start_button)
    Button start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_start_before);
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        mContext = this;
        bindView();
        removeStatebar();
        // 1초 딜레이
        guide_speech();
    }

    public void removeStatebar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void guide_speech() {
        // 음성 기본 설정
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                // 언어 설정
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);  // 음성 톤 x1.0
                tts.setSpeechRate(1.0f);  // 읽는 속도 x1.0
            }
        });
        mHandler = new Handler();
        runOnUiThread(() -> {
            // 1초 후
            mHandler.postDelayed(() -> {
                try {
                    tts.speak(message1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                    // QUEUE_ADD : 위에꺼 다 끝나면
                    tts.speak(message2.getText().toString(), TextToSpeech.QUEUE_ADD, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        });
    }

    // 운동부하검사 시작 버튼;
    @OnClick(R.id.start_button)
    void Click() {
        startActivity(new Intent(TestStartBeforeActivity.this, BikeScanActivity.class));
        finish();
        // 운동부하검사 시작 버튼;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
