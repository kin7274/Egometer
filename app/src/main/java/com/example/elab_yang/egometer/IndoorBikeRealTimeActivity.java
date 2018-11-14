package com.example.elab_yang.egometer;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.activity.ergometer.IndoorBikeResultActivity;
import com.example.elab_yang.egometer.model.Met;
import com.example.elab_yang.egometer.service.EZBLEService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

import static android.graphics.Color.RED;
import static com.example.elab_yang.egometer.etc.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;

public class IndoorBikeRealTimeActivity extends AppCompatActivity implements IActivityBasicSetting {
    private static final String TAG = "IndoorBikeRealTimeActiv";

    TextToSpeech tts;


    int bpm_avg = 0;
    int total_bpm = 0;
    // 운동 전 혈당
    String before_bloodsugar = "";
    // 운동 후 혈당
    String after_bloodsugar = "";

    BluetoothGattCharacteristic mNotifyCharacteristic;
    EZBLEService mBluetoothLeService;

    String mDeviceAddress;
    private boolean mConnected = false;

    @BindView(R.id.layout)
    RelativeLayout layout;

    @BindView(R.id.textView6)
    TextView meanSpeedTextView; //평균속도

    @BindView(R.id.heart_rate_text_view)
    TextView heartRateTextView; //심박수

    @BindView(R.id.text_stage)
    TextView text_stage;

    @BindView(R.id.text_aim_bpm)
    TextView text_aim_bpm;

    @BindView(R.id.txt_Kcal)
    TextView kcalTextview;

    @BindView(R.id.speed_text_view)
    TextView nowSpeedTextView;

    @BindView(R.id.bpm_avg)
    TextView text_avg_bpm;

    @BindView(R.id.avg_distance)
    TextView totalDistanceTextView;

    @BindView(R.id.chronometer)
    Chronometer chronometer;

    @BindView(R.id.line_chart)
    LineChart lineChart;

//    @BindView(R.id.cv_countdownView)
//    CountdownView countdownView;

    CountdownView countdownView;

    int chrono_flag = 0;

    String AAAA;
    TextView textView11;

    Met met = new Met();
    ArrayList<Met> metArrayList = new ArrayList<>();

    private boolean startIndicator = false;
    float globalKCal = 0.0f;
    float sumSpeed = 0.0f;

    private LineDataSet lineDataSet;
    private LineData lineData;
    private ArrayList<Entry> realtimeData = new ArrayList<>();
    private int cnt = 0;
    private int cnt1 = 0;
    private int speedCount = 0;

    String[] AAA;
    //    float userHeartRate = 190.0f;
//    float userMinHeartRate = userHeartRate * 0.5f;
//    float userMaxHeartRate = userHeartRate * 0.7f;
    float min_bpm = 0.0f;
    float max_bpm = 0.0f;

    int flag = 0;

    //    long elapsedMillis;
    long seconds;

    Handler mHandler;

    Bundle bundle = new Bundle();

    int workoutTime;
    int workoutIntense;

    int cntcnt = 0;
    int[] hr = new int[5];

    int num = 0;
    int def_num = 0;
    boolean firstStartFlag = false;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((EZBLEService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
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

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver;

    {
        mGattUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (EZBLEService.ACTION_GATT_CONNECTED.equals(action)) {
//                    mConnected = true;
                    //updateConnectionState(R.string.connected);
                    invalidateOptionsMenu();
//                emptyLayout.setVisibility(View.GONE);
//                    infoLayout.setVisibility(View.VISIBLE);
//                    countdownView.setVisibility(View.VISIBLE);

                } else if (EZBLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                    mConnected = false;
                    chronometer.stop();
//                    countdownView.pause();
                    //updateConnectionState(R.string.disconnected);
                    invalidateOptionsMenu();
//                emptyLayout.setVisibility(View.VISIBLE);
//                    infoLayout.setVisibility(View.GONE);
//                    countdownView.setVisibility(View.GONE);
                    firstStartFlag = true;
                    //clearUI();
                } else if (EZBLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                    startIndicator = true;
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    chrono_flag = 1;
                    start_set_chronometer();
                    Log.d(TAG, "onReceive: chronometer start");

//                    countdownView.start(workoutTime * 1000);
//                    if (firstStartFlag) {
//                        countdownView.restart();
//                        firstStartFlag = false;
//                    }
                } else if (EZBLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                    Log.e(TAG, "onReceive: " + intent.getStringExtra(EZBLEService.EXTRA_DATA));
                    displayData(intent.getStringExtra(EZBLEService.EXTRA_DATA));
                } else if (EZBLEService.ACTION_HEART_RATE_AVAILABLE.equals(action)) {
//                    Log.e(TAG, "onReceive:  실시간 화면에서 심박수 받앗어요 ");
//                    String hr = intent.getStringExtra(EZBLEService.EXTRA_DATA);
//                    heartRateTextView.setText(hr);
//
                    Log.d(TAG, "onReceive: cntcnt = " + cntcnt);
//                    float userHR = Float.parseFloat(hr);
                    String str_hr = intent.getStringExtra(EZBLEService.EXTRA_DATA);
                    heartRateTextView.setText(str_hr);
                    setLineChartData(str_hr);
                    // 일단 view에 넣어두고

                    // 인트로 변환해 비교하기위해
                    hr[cntcnt % 5] = Integer.parseInt(str_hr);

                    // avg
                    total_bpm = total_bpm + hr[cntcnt % 5];
                    Log.d(TAG, "onReceive: totalbpm = " + total_bpm);
                    bpm_avg = total_bpm/(cntcnt + 1);
                    Log.d(TAG, "onReceive: cntcnt = " + cntcnt);

                    Log.d(TAG, "onReceive: bpm_avg = " + bpm_avg);
                    Log.d(TAG, "onReceive: total_bpm/(cntcnt + 1) = " + total_bpm/(cntcnt + 1));


                    text_avg_bpm.setText(String.valueOf(bpm_avg));
                    cntcnt++;

                } else if (EZBLEService.ACTION_INDOOR_BIKE_AVAILABLE.equals(action)) {

                    String nowSpeed = intent.getStringExtra(EZBLEService.EXTRA_DATA);
                    nowSpeedTextView.setText(nowSpeed);
                    if (!nowSpeed.equals("0.00")) {
                        globalKCal += countSpeed(nowSpeed);
                        String tmp = String.format("%3.2f", globalKCal);
                        String msg = tmp + "kcal";
                        kcalTextview.setText(msg);
                        sumSpeed += Float.parseFloat(nowSpeed);
                        float meanSpeed = sumSpeed / speedCount;
                        String meanSpeedMsg = String.format("%2.1f", meanSpeed);
                        meanSpeedTextView.setText(meanSpeedMsg);
                        speedCount++;
                    }

                } else if (EZBLEService.ACTION_TREADMILL_AVAILABLE.equals(action)) {
                    String totalDistance = intent.getStringExtra(EZBLEService.EXTRA_DATA);
                    totalDistanceTextView.setText(totalDistance);
                }
            }
        };
    }

    private void setLineChartData(String hr) {
        float floatValue = Float.parseFloat(hr);
        realtimeData.add(new Entry(cnt, floatValue));
        lineDataSet = new LineDataSet(realtimeData, "실시간 데이터");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setColor(RED);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.moveViewToX(lineData.getEntryCount());
        lineChart.notifyDataSetChanged();
        ++cnt;
    }


    private float countSpeed(String speed) {
        metArrayList = met.getIndoorBikeMetData();
        if (Float.parseFloat(speed) < 15.0f) {
            float metValue = metArrayList.get(0).getMetValue();
            float userKCal = 3.5f * 0.05f * 65.0f * metValue * 0.017f;
            return userKCal;
        } else if (Float.parseFloat(speed) >= 15.0f && Float.parseFloat(speed) < 20.0f) {
            float metValue = metArrayList.get(1).getMetValue();
            float userKCal = 3.5f * 0.05f * 65.0f * metValue * 0.017f;
            return userKCal;
        } else if (Float.parseFloat(speed) >= 20.0f) {
            float metValue = metArrayList.get(2).getMetValue();
            float userKCal = 3.5f * 0.05f * 65.0f * metValue * 0.017f;
            return userKCal;
        } else {
            return 0.0f;
        }
    }

    private void initChart() {
        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_bike_real_time);
        initSetting();
        tts_setting();

        Intent bsmintent = getIntent();
//        before_bloodsugar = bsmintent.getExtras().getString("before_bloodsugar");
        AAAA = bsmintent.getExtras().getString("mydata");
        AAA = AAAA.split("/");
//        AAA[1] : min bpm;
//        AAA[2] : max bpm;

        num = Integer.parseInt(AAA[4]);
        def_num = num * 30;

//        textView11 = (TextView) findViewById(R.id.textView11);
        text_stage.setText("중강도 1번");
        text_aim_bpm.setText(AAA[2] + " - " + AAA[3]);

        tts.speak("중강도 시작", TextToSpeech.QUEUE_FLUSH, null, null);

//        chronometer = (Chronometer) findViewById(R.id.chronometer);

//        layout = (ConstraintLayout) findViewById(R.id.layout);

//        countdownView = (CountdownView) findViewById(R.id.cv_countdownView);
//        showDialog();
        ButterKnife.bind(this);

        min_bpm = Float.parseFloat(AAA[2]);
        max_bpm = Float.parseFloat(AAA[3]);
//        Log.d(TAG, "onCreate: Float.parseFloat(AAA[1])" + Float.parseFloat(AAA[1]));
//        Log.d(TAG, "onCreate: Float.parseFloat(AAA[2])" + Float.parseFloat(AAA[2]));
        Log.d(TAG, "onCreate: min_bpm = " + min_bpm);
        Log.d(TAG, "onCreate: max_bpm = " + max_bpm);




        chronometer.setTextSize(20);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;

        mDeviceAddress = getIntent().getStringExtra(REAL_TIME_INDOOR_BIKE_DEVICE);

        Intent gattServiceIntent = new Intent(this, EZBLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        long time = 30 * 60 * 1000;  // 1800000초 = 30분

//        countdownView.setVisibility(View.GONE);
//        countdownView.start(time);
//        flag = 0 : 중강도;
//        flag = 1 : 고강도;

        initChart();
//        infoLayout.setVisibility(View.GONE);
    }

    // 운동 전 혈당입력
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("운동전 혈당을 입력해주세요")
                .setMessage("혈당은 몇인가요?");
        final EditText et = new EditText(this);
        builder.setPositiveButton("YES", (dialog, which) -> {
            // 운동 전 혈당을 받아서
            before_bloodsugar = et.getText().toString();
            Toast.makeText(getApplicationContext(), "감사합니다", Toast.LENGTH_SHORT).show();
        })
                .setView(et)
                .create()
                .show();
    }

    public void tts_setting() {
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);
//                tts.setSpeechRate(1.0f);
//                tts.setSpeechRate(0.5f);
                tts.setSpeechRate(0.8f);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        chronometer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }

//        chronometer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        chronometer.stop();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        tts.stop();
        tts.shutdown();
    }

    private void displayData(String data) {
        if (data != null) {
//            textView.append(data);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("운동을 종료하시겠어요?");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
//            Toast.makeText(getApplicationContext(), "결과값을 정리하자", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("운동 후 혈당을 입력해주세요")
                    .setMessage("혈당은 몇인가요?");
            final EditText et1 = new EditText(this);
            builder1.setPositiveButton("응", (dialog1, which1) -> {
                // 운동 후 혈당을 받아서
                after_bloodsugar = et1.getText().toString();
                Toast.makeText(getApplicationContext(), "감사합니다", Toast.LENGTH_SHORT).show();

                // 시간
                int time = (int) (long) (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
                //            Log.d(TAG, "운동시간 = " + time +"초");
                // 평균속도
//                String avg_speed = meanSpeedTextView.getText().toString();
                // 이동거리
                String total_distance = totalDistanceTextView.getText().toString();


                // 평균BPM
                String avg_bpm = String.valueOf(bpm_avg);
                // 소모 칼로리
                String kcal = kcalTextview.getText().toString();
                String avg_speed = meanSpeedTextView.getText().toString();

//            Log.d(TAG, "평균쇽도 = " + avg_speed);
//            Log.d(TAG, "이동거리 = " + total_distance);
//            Log.d(TAG, "평균심박 = " + avg_bpm);
//            Log.d(TAG, "칼로리량 = " + kcal);

                //            Log.d(TAG, "3333333333333333333333333333. EI =" + EI);

                Intent bsmintent = getIntent();
                before_bloodsugar = bsmintent.getExtras().getString("before_bloodsugar");
//                String AAAA = bsmintent.getExtras().getString("mydata");
//                AAA = AAAA.split("/");

//                data2 = time + "/" + bpm1 + "/" + bpm2;

//                AAA[0] : time : 00;
//                AAA[1] : bpm1 : 000;
//                AAA[2] : bpm2 : 000;

                // TODO: 2018-11-07 13:48 캐시 저장 형식 변경
                // [0] : 단계
                // [1] : min bpm
                // [2] : max bpm
                // [3] : 중강도 시간


                // AAA[3]
                Intent intent = new Intent(IndoorBikeRealTimeActivity.this, IndoorBikeResultActivity.class);
                intent.putExtra("before_bloodsugar", before_bloodsugar);
                intent.putExtra("after_bloodsugar", after_bloodsugar);
                intent.putExtra("result_time", time);
                intent.putExtra("result_extra", avg_speed + "&" + total_distance + "&" + bpm_avg + "&" + kcal);



                startActivity(intent);
                finish();
            })
                    .setView(et1)
                    .create()
                    .show();
        });
        builder.show()
                .create();
    }

    public void start_set_chronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener((Chronometer chronometer) -> {
            seconds = (int) (((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000));
            Log.d(TAG, "onChronometerTick: seconds = " + seconds);

//                int timetimetime = seekbar_data_convert(Integer.parseInt(AAA[3]));
            if (seconds >= def_num && seconds < 600 && flag == 0) {
                // 고강도 1번

                text_stage.setText("고강도 1번");

                tts.speak("고강도 시작", TextToSpeech.QUEUE_FLUSH, null, null);
                flag = 1;

            } else if (seconds >= 600 && seconds < (600 + def_num) && flag == 1) {
                // 중강도 2번
                text_stage.setText("중강도 2번");

                tts.speak("중강도 2번째 시작", TextToSpeech.QUEUE_FLUSH, null, null);
                flag = 0;

            } else if (seconds >= (600 + def_num) && seconds < 1200 && flag == 0) {
                // 고강도 1번
                text_stage.setText("고강도 2번");


                tts.speak("고강도 2번째 시작", TextToSpeech.QUEUE_FLUSH, null, null);
                flag = 1;

            } else if (seconds >= 1200 && seconds < (1200 + def_num) && flag == 1) {
                // 중강도 2번
                text_stage.setText("중강도 3번");

                tts.speak("중강도 3번째 시작", TextToSpeech.QUEUE_FLUSH, null, null);
                flag = 0;

            } else if (seconds >= (1200 + def_num) && seconds <= 1800 && flag == 0) {
                // 고강도 3번
                text_stage.setText("고강도 3번");

                tts.speak("고강도 3번째 시작", TextToSpeech.QUEUE_FLUSH, null, null);
                flag = 1;

            }

            if ((seconds % 10) == 0) {
                float userHR = (float) hr[cnt1 % 5];
                // 중강도
                if (flag == 0) {
                    if (userHR > 0.0f && userHR < min_bpm) {
                        String msg = "운동강도를 올릴 필요가 있습니다.";
                        layout.setBackgroundResource(R.color.weaker_red);
                        tts.speak("속도가 낮습니다", TextToSpeech.QUEUE_FLUSH, null, null);
                    } else if (userHR >= min_bpm && userHR < max_bpm) {
                        String msg = "적절한 운동강도로 운동중입니다.";

                        layout.setBackgroundResource(R.color.weaker_green);

                        tts.speak("적당혀", TextToSpeech.QUEUE_FLUSH, null, null);

                    } else if (userHR >= max_bpm) {
                        String msg = "운동강도가 초과됬습니다. 속도를 낮추고나 W를 낮춰주세요";
                        layout.setBackgroundResource(R.color.weaker_blue);

                        tts.speak("속도가 빠릅니다. 속도를 낮춰주세요", TextToSpeech.QUEUE_FLUSH, null, null);
                    }

                    // 고강도
                } else {
                    if (userHR > 0.0f && userHR < max_bpm) {
                        String msg = "운동강도를 올릴 필요가 있습니다.";
                        tts.speak("속도가 낮습니다. 속도를 올려주세요", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
                if (userHR == 0.0f) {
                    tts.speak("심박센서 위치를 확인해주세요", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
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
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
    }
}