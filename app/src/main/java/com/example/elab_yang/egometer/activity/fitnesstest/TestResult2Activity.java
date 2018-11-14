package com.example.elab_yang.egometer.activity.fitnesstest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.elab_yang.egometer.IActivityBasicSetting;
import com.example.elab_yang.egometer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// [0] : 단계
// [1] : min bpm
// [2] : max bpm
// [3] : 중강도 시간

//        dididi.setText(AAAA);


public class TestResult2Activity extends AppCompatActivity implements IActivityBasicSetting {
    //        implements View.OnClickListener {
    private static final String TAG = "TestResultActivity";
    Context mContext;
    public int num = 18;
    String stage = "";
    String bpm = "";

    @BindView(R.id.bpm_up)
    Button bpm_up;

    @BindView(R.id.bpm_zero)
    Button bpm_zero;

    @BindView(R.id.bpm_down)
    Button bpm_down;

    // 중강도, 고강도 시간 입력
    @BindView(R.id.joong_gang_do_time)
    TextView joong_gang_do_time;

    @BindView(R.id.go_gang_do_time)
    TextView go_gang_do_time;

    // 심박, 속도 가이드 수치 입력
    @BindView(R.id.bpm_guide)
    TextView bpm_guide;

    @BindView(R.id.bpm_guide1)
    TextView bpm_guide1;

    @BindView(R.id.speed_guide)
    TextView speed_guide;

    @BindView(R.id.speed_guide1)
    TextView speed_guide1;

    @BindView(R.id.text_bom_max)
    TextView text_bom_max;

    @BindView(R.id.seekbar)
    SeekBar seekBar;

    int t = 0;

    SharedPreferences pref;

    // seekbar를 위한 초기값
    public int joong_gang_do_time_default = 9;  // 9분
    public int go_gang_do_time_default = 1;  // 1분


    float bpm1, bpm2;

    String[] abc = new String[4];
    String mydata = "";
    String AAAA = "";

    String bpm1_str, bpm2_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result2);
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
        dd();
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

    public void dd() {
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        AAAA = pref.getString("mydata", mydata);
        Log.d(TAG, "dd: AAAA = " + AAAA);
        if (AAAA.equals("")) {
            // 운동부하검서를 먼저 진행해주세요.

        } else {
            abc = AAAA.split("/");
//            abc[0] : 단계
//            abc[1] : 검사로 나온 최대값
//            abc[2] : 최소심박
//            abc[3] : 최대심박
//            abc[4] : 중강도시간

            joong_gang_do_time.setText(String.valueOf(seekbar_data_convert(Integer.parseInt(abc[4]))) );
            go_gang_do_time.setText(seekbar_data_convert((int) 20 - Integer.parseInt(abc[4])));


            text_bom_max.setText("최대 심박수는 " + abc[1] + "이네요");
            bpm_guide1.setText("심박수는 " + abc[2] + " 이상을 유지");

            speed_guide.setText("속도는 15-20km/h를 유지");
            set_bpm();
            speed_guide1.setText("속도는 20km/h 이상 유지");

            // 시크바
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
        }
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

    @OnClick({R.id.bpm_down, R.id.bpm_zero, R.id.bpm_up})
    void Click(View v) {
        switch (v.getId()) {
            case R.id.bpm_up:
                t++;
                Log.d(TAG, "onClick: t" + String.valueOf(t));
                set_bpm();
                break;

            case R.id.bpm_zero:
                t = 0;
                Log.d(TAG, "onClick: t" + String.valueOf(t));
                set_bpm();

                break;

            case R.id.bpm_down:
                t--;
                Log.d(TAG, "onClick: t" + String.valueOf(t));
                set_bpm();

                break;
        }
    }

    public void set_bpm() {
//        bpm1 = Float.parseFloat(abc[1]) * (0.5f + 0.05f * t);
//        bpm2 = Float.parseFloat(abc[1]) * (0.7f + 0.05f * t);

        bpm1 = Float.parseFloat(abc[2] + 0.05f *t);
        bpm2 = Float.parseFloat(abc[3] + 0.05f *t);
        bpm1_str = String.format("%.0f", bpm1);
        bpm2_str = String.format("%.0f", bpm2);
        Log.d(TAG, "bpm1_str" + bpm1_str);
        Log.d(TAG, "bpm2_str" + bpm2_str);
        bpm_guide.setText("심박수는 " + bpm1_str + " - " + bpm2_str + "를 유지");

        bpm_guide1.setText("심박수는 " + bpm2_str + " 이상을 유지");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pref = TestResult2Activity.this.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        mydata = abc[0] + "/" + abc[1] + "/" + bpm1_str + "/" + bpm2_str + "/" + String.valueOf(num);
        editor.putString("mydata", mydata);
        Log.d(TAG, "mydata = " + mydata);
        editor.apply();
        finish();
    }
}