package com.example.elab_yang.egometer;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.elab_yang.egometer.activity.fitnesstest.TestResultActivity;
import com.example.elab_yang.egometer.etc.IntentConst;
import com.example.elab_yang.egometer.service.EZBLEService;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

public class LoadTestActivity extends AppCompatActivity implements IActivityBasicSetting {
    private static final String TAG = "LoadTestActivity";

    int total_bpm = 0;
    int cnt = 0;
    int cnt1 = 0;
    int bpm_avg = 0;
    int finish_sig = 0;

    Boolean nextstage_flag = false;
    int timecnt = 0;

    TextToSpeech tts;

    Boolean tts_flag = false;

    final static int SET_2_MINUTE = 120000;  // 2분 == 2 * 60 * 1000;

    @BindView(R.id.cv_countdownView)
    CountdownView countdownView;

    @BindView(R.id.aim_speed_text_view)
    TextView aimSpeedTextView;

    @BindView(R.id.stage_text_view)
    TextView stageTextView;

    @BindView(R.id.heart_rate_text_view)
    TextView heartRateTextView;

    @BindView(R.id.speed_text_view)
    TextView speedTextView;

    @BindView(R.id.layout)
    LinearLayout layout;

    @BindView(R.id.max_bpm)
    TextView max_bpm;

    @BindView(R.id.avg_bpm)
    TextView avg_bpm;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    @BindView(R.id.levelupLayout)
    RelativeLayout levelupLayout;

    CountDownTimer timer;

    String hr;

    String deviceAddress, deviceName;

    float aimSpeed = 15.0f;
    int testStage = 1;

    int[] hrr = new int[5];
    int[] nowSpeedd = new int[5];

    int bpm_max = 0;

    Handler mHandler;

    BluetoothGattCharacteristic mNotifyCharacteristic;
    EZBLEService mBluetoothLeService;
    private boolean mConnected = false;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((EZBLEService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(deviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EZBLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(EZBLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(EZBLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(EZBLEService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(EZBLEService.ACTION_HEART_RATE_AVAILABLE);
        intentFilter.addAction(EZBLEService.ACTION_INDOOR_BIKE_AVAILABLE);
        intentFilter.addAction(EZBLEService.ACTION_TREADMILL_AVAILABLE);
        return intentFilter;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EZBLEService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;

            } else if (EZBLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;

            } else if (EZBLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                timer.start();
                countdownView.start(SET_2_MINUTE);

            } else if (EZBLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.e(TAG, "onReceive: " + intent.getStringExtra(EZBLEService.EXTRA_DATA));

            } else if (EZBLEService.ACTION_HEART_RATE_AVAILABLE.equals(action)) {
                hr = intent.getStringExtra(EZBLEService.EXTRA_DATA);
                heartRateTextView.setText(hr);
                Log.d(TAG, "onReceive: hr = " + hr);
                hrr[cnt % 5] = Integer.parseInt(hr);
                // hrr[0] = 140;

                // avg
                total_bpm = total_bpm + hrr[cnt % 5];
                bpm_avg = total_bpm / (cnt + 1);
                avg_bpm.setText(String.valueOf(bpm_avg));

                // max
                if (hrr[cnt % 5] > bpm_max) {
                    bpm_max = hrr[cnt % 5];
                }
                max_bpm.setText(String.valueOf(bpm_max));
                cnt++;
            } else if (EZBLEService.ACTION_INDOOR_BIKE_AVAILABLE.equals(action)) {
//                cnt1++;
//                Toast.makeText(getApplicationContext(), "timecnt = " + timecnt, Toast.LENGTH_SHORT).show();

                String nowSpeed = intent.getStringExtra(EZBLEService.EXTRA_DATA);
                speedTextView.setText(nowSpeed);
                Log.d(TAG, "onReceive: nowSpeed = " + nowSpeed);
                Log.d(TAG, "onReceive: Float.parseFloat(nowSpeed " + Float.parseFloat(nowSpeed));
                Log.d(TAG, "onReceive: aimSpeed " + aimSpeed);
                Log.d(TAG, "onReceive: ((int) aimSpeed) + 5 " + ((int) aimSpeed) + 5);

//                Toast.makeText(getApplicationContext(), "cnt1 = " + cnt1, Toast.LENGTH_SHORT).show();
                // 10초에 한번씩 방송
                if (cnt1 % 10 == 1) {
                    tts_flag = true;
                    Toast.makeText(getApplicationContext(), "개꿀", Toast.LENGTH_SHORT).show();
                }


                if (Float.parseFloat(nowSpeed) < aimSpeed) {
                    // 속도가 느려..
                    layout.setBackgroundResource(R.color.weaker_red);
                    if (tts_flag) {
                        tts.speak("느려요", TextToSpeech.QUEUE_FLUSH, null, null);
                        tts_flag = false;
                    }
                    if (Float.parseFloat(nowSpeed) == 0) {
                        if (finish_sig == 5) {
                            // 종료
                            finish_exercise();
                            finish();
                        } else {
                            finish_sig++;
                        }
                    } else {
                        finish_sig = 0;
                    }

                } else if (Float.parseFloat(nowSpeed) > ((int) aimSpeed) + 5) {
                    if (tts_flag) {
                        tts.speak("빨라요", TextToSpeech.QUEUE_FLUSH, null, null);
                        tts_flag = false;
                    }
                    layout.setBackgroundResource(R.color.weaker_blue);
                } else {
                    // 적당혀
                    layout.setBackgroundResource(R.color.weaker_green);
                }
                cnt1++;
            } else if (EZBLEService.ACTION_TREADMILL_AVAILABLE.equals(action))

            {
                String totalDistance = intent.getStringExtra(EZBLEService.EXTRA_DATA);
//                totalDistanceTextView.setText(totalDistance);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_test);
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
        setStatusbar();
        tts_setting();
        setLottie();

        deviceAddress = getIntent().getStringExtra(IntentConst.FITNESS_LOAD_TEST_DEVICE_ADDRESS);
        Log.e(TAG, "onCreate: " + deviceAddress);
//        initSetting();

        aimSpeedTextView.setText(String.valueOf(aimSpeed) + " km/h");
        stageTextView.setText(String.valueOf(testStage) + "단계");

        timer = new CountDownTimer(SET_2_MINUTE, 1000) {

            public void onTick(long m) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//                gaugeView.setTargetValue(new Random().nextInt(101));
                Log.d(TAG, "onTick: " + m + "");
                progressBar.setProgress(100 - (int) (100 * m) / 120000);
                // 종료 30초 전 알림
                if (m < 30000 && nextstage_flag == false) {
                    // 밀고 스피크
                    tts.speak("30초 뒤 레벨업", TextToSpeech.QUEUE_FLUSH, null, null);
                    nextstage_flag = true;
                }
            }

            public void onFinish() {
                tts.speak("단계가 올라갑니다.", TextToSpeech.QUEUE_FLUSH, null, null);
                aimSpeed += 2.0;
                testStage += 1;
                aimSpeedTextView.setText(String.valueOf(aimSpeed) + " km/h");
                stageTextView.setText(String.valueOf(testStage) + "단계");
                timer.start();
                countdownView.start(SET_2_MINUTE);
                levelupEvent();

                nextstage_flag = false;
                // 프로그레스바 초기화 : 다시 0
                progressBar.setProgress((int) (0));
            }
        };

        Intent gattServiceIntent = new Intent(this, EZBLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


//        countdownView.start(SET_2_MINUTE);
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    public void tts_setting() {
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);
                tts.setSpeechRate(0.8f);
            }
            tts.speak("가즈아ㅏㅏㅏ", TextToSpeech.QUEUE_FLUSH, null, null);
        });
    }

    public void setLottie() {
        animationView.setAnimation("favourite_app_icon.json");
        animationView.loop(true);
//Lottie Animation start
//        animationView.playAnimation();
    }

    public void levelupEvent() {
        levelupLayout.setVisibility(View.VISIBLE);
        animationView.playAnimation();
        mHandler = new Handler();
        runOnUiThread(() -> {
            mHandler.postDelayed(() -> {
                try {
                    levelupLayout.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        });
    }

    public void finish_exercise() {
//        tts.speak("검사를 종료합니다. 고생하셨습니다.", TextToSpeech.QUEUE_FLUSH, null, null);
        // 결과창 view
        Intent intent = new Intent(LoadTestActivity.this, TestResultActivity.class);
        // 몇 단계
        intent.putExtra("stage", String.valueOf(testStage));
        // 최대심박수
//            int bpm = 150;
//            intent.putExtra("bpm", String.valueOf(testStage));
        intent.putExtra("bpm", max_bpm.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        tts.stop();
        tts.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(deviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
//        chronometer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        countdownView.stop();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("현재 검사 " + stageTextView.getText().toString() + " 진행중입니다.\n검사를 종료하시겠어요?");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            finish_exercise();
            dialog.dismiss();
            finish();
        });
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
//        super.onBackPressed();
    }
}
