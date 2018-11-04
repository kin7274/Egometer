package com.example.elab_yang.egometer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.example.elab_yang.egometer.R;

public class TestResultActivity extends AppCompatActivity {
    SeekBar seekBar;
    Context mContext;
    public int num = 0;
    // seekbar를 위한 초기값
    public int joong_gang_do_time_default = 9;  // 9분
    public int go_gang_do_time_default = 1;  // 1분
    TextView text_stage, text_bom_max;  // 운동 부하 검사 결과 단계, 최대심박수
    TextView joong_gang_do_time, go_gang_do_time;  // 중강도, 고강도 시간 입력
    TextView bpm_guide, bpm_guide1, speed_guide, speed_guide1;  // 심박, 속도 가이드 수치 입력
    Button letsgo, close_btn;  // 바로 운동 시작 버튼, OK~ 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        mContext = this;
        setToolbar();
        setStatusbar();
        joong_gang_do_time = (TextView) findViewById(R.id.joong_gang_do_time);
        joong_gang_do_time.setText(String.valueOf(joong_gang_do_time_default) + "분");
        go_gang_do_time = (TextView) findViewById(R.id.go_gang_do_time);
        go_gang_do_time.setText(String.valueOf(go_gang_do_time_default) + "분");

        // 최종 단계
        text_stage = (TextView) findViewById(R.id.text_stage);
        text_stage.setText("3단계까지 가셨군요");

        // 최대 심박
        text_bom_max = (TextView) findViewById(R.id.text_bom_max);
        text_bom_max.setText("최대 심박수는 150이네요");

        // 중강도
        // 심박 if BPMamx = 150이라면
        // BPMmax 0.5 - 0.7
        bpm_guide = (TextView) findViewById(R.id.bpm_guide);
        int bpm = 150;
        double bpm1 = bpm * 0.5;
        double bpm2 = bpm * 0.7;
        bpm_guide.setText("심박수는 " + Double.toString(bpm1) + " - " + Double.toString(bpm2) + "를 유지");
        // 속도
        speed_guide = (TextView) findViewById(R.id.speed_guide);
        speed_guide.setText("속도는 15-20km/h를 유지");

        // 고강도
        // 심박
        bpm_guide1 = (TextView) findViewById(R.id.bpm_guide1);
        bpm_guide1.setText("심박수는 " + Double.toString(bpm2) + " 이상을 유지");

        // 속도
        speed_guide1 = (TextView) findViewById(R.id.speed_guide1);
        speed_guide1.setText("속도는 20km/h 이상 유지");

        // 시크바
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        // thumb 크기 조절
        ViewTreeObserver vto = seekBar.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Resources res = getResources();
                Drawable thumb = res.getDrawable(R.drawable.placeholder);
                int h = (int) (seekBar.getMeasuredHeight() * 1.5);
                int w = h;
                Bitmap bitmap = ((BitmapDrawable) thumb).getBitmap();
                Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, w, h, true);
                Drawable newThumb = new BitmapDrawable(res, bitmapScaled);
                newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                seekBar.setThumb(newThumb);
                seekBar.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        // 시크바 이벤트
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 상태가 변경되었을때
                num = seekBar.getProgress();
//                joong_gang_do_time.setText(new StringBuilder().append(num));
//                go_gang_do_time.setText(new StringBuilder().append(10 - num));
                joong_gang_do_time.setText(new StringBuilder().append(seekbar_data_convert(num)));
                go_gang_do_time.setText(new StringBuilder().append(seekbar_data_convert(20 - num)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 움직임이 시작될때
                num = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 움직임이 멈췄을때
                num = seekBar.getProgress();
            }
        });

        // 바로 운동하러 ㄱ
        letsgo = (Button) findViewById(R.id.letsgo);
        letsgo.setOnClickListener(v -> {
            // 인텐트로 바로 운동하러 ㄱ
            Toast.makeText(getApplicationContext(), "바로 운동하러 ㄱ", Toast.LENGTH_SHORT).show();
        });

        // 부하 검사 종료
        close_btn = (Button) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(v -> finish());
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
}
