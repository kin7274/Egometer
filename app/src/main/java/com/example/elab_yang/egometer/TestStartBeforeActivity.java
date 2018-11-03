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

public class TestStartBeforeActivity extends AppCompatActivity {
    private TextToSpeech tts;
    Context mContext;
    private Handler mHandler;
    TextView top_title_message, mid_title_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test_start_before);
        top_title_message = (TextView) findViewById(R.id.top_title_message);
        mid_title_message = (TextView) findViewById(R.id.mid_title_message);
        // 1초 후 음성 start;
        guide_speech();
        // 운동부하검사 시작 버튼;
        Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(v -> {
            startActivity(new Intent(TestStartBeforeActivity.this, BikeScanActivity.class));
            finish();
        });
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
                    tts.speak(top_title_message.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                    // QUEUE_ADD : 위에꺼 다 끝나면
                    tts.speak(mid_title_message.getText().toString(), TextToSpeech.QUEUE_ADD, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        });
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
