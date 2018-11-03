package com.example.elab_yang.egometer;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

import static android.graphics.Color.RED;
import static com.example.elab_yang.egometer.etc.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;

public class IndoorBikeRealTimeActivity extends AppCompatActivity {
    private static final String TAG = "IndoorBikeRealTimeActiv";

    // 운동 전 혈당
    String before_bloodsugar = "";
    // 운동 후 혈당
    String after_bloodsugar = "";

    BluetoothGattCharacteristic mNotifyCharacteristic;
    EZBLEService mBluetoothLeService;

    String mDeviceAddress;
    private boolean mConnected = false;

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.textView2)
    TextView kcalTextview;

    @BindView(R.id.textView3)
    TextView nowSpeedTextView;

    @BindView(R.id.textView9)
    TextView totalDistanceTextView;

    @BindView(R.id.textView11)
    TextView userStateMsgTextView; //운동상태
    @BindView(R.id.textView13)
    TextView heartRateTextView; //심박수

    @BindView(R.id.textView6)
    TextView meanSpeedTextView; //평균속도

    @BindView(R.id.chronometer)
    Chronometer chronometer;

    @BindView(R.id.line_chart)
    LineChart lineChart;

    @BindView(R.id.info_layout)
    LinearLayout infoLayout;

    @BindView(R.id.home)
    ImageView backButton;

    @BindView(R.id.cv_countdownView)
    CountdownView countdownView;

    Met met = new Met();
    ArrayList<Met> metArrayList = new ArrayList<>();

    private boolean startIndicator = false;
    float globalKCal = 0.0f;
    float sumSpeed = 0.0f;

    private LineDataSet lineDataSet;
    private LineData lineData;
    private ArrayList<Entry> realtimeData = new ArrayList<>();
    private int cnt = 0;
    private int speedCount = 0;
    String EI;

    float userHeartRate = 190.0f;
    float userMinHeartRate = userHeartRate * 0.5f;
    float userMaxHeartRate = userHeartRate * 0.7f;


    Bundle bundle = new Bundle();

    int workoutTime;
    int workoutIntense;

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
                    mConnected = true;
                    //updateConnectionState(R.string.connected);
                    invalidateOptionsMenu();
//                emptyLayout.setVisibility(View.GONE);
                    infoLayout.setVisibility(View.VISIBLE);
                    countdownView.setVisibility(View.VISIBLE);

                } else if (EZBLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
                    mConnected = false;
                    chronometer.stop();
                    countdownView.pause();
                    //updateConnectionState(R.string.disconnected);
                    invalidateOptionsMenu();
//                emptyLayout.setVisibility(View.VISIBLE);
                    infoLayout.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);

                    firstStartFlag = true;
                    //clearUI();
                } else if (EZBLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                    startIndicator = true;
                    chronometer.start();
                    countdownView.start(workoutTime * 1000);
                    if (firstStartFlag) {
                        countdownView.restart();
                        firstStartFlag = false;
                    }
                } else if (EZBLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                    Log.e(TAG, "onReceive: " + intent.getStringExtra(EZBLEService.EXTRA_DATA));
                    displayData(intent.getStringExtra(EZBLEService.EXTRA_DATA));
                } else if (EZBLEService.ACTION_HEART_RATE_AVAILABLE.equals(action)) {
                    Log.e(TAG, "onReceive:  실시간 화면에서 심박수 받앗어요 ");
                    String hr = intent.getStringExtra(EZBLEService.EXTRA_DATA);
                    heartRateTextView.setText(hr);
                    setLineChartData(hr);

                    float userHR = Float.parseFloat(hr);
                    if (userHR > 0.0f && userHR < userMinHeartRate) {
                        String msg = "운동강도를 올릴 필요가 있습니다.";
                        userStateMsgTextView.setText(msg);
                        userStateMsgTextView.setBackgroundColor(Color.RED);

//                    customWaveView.setmBlowWaveColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.low_stat));
//                    customWaveView.setmAboveWaveColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.low_stat));
//                    customWaveView.setBackgroundColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.low_stat));
                    } else if (userHR >= userMinHeartRate && userHR < userMaxHeartRate) {
                        String msg = "적절한 운동강도로 운동중입니다.";
                        userStateMsgTextView.setText(msg);
                        userStateMsgTextView.setBackgroundColor(Color.GREEN);

//                    customWaveView.setmBlowWaveColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.shopAccent));
//                    customWaveView.setmAboveWaveColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.shopAccent));
//                    customWaveView.setBackgroundColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.shopAccent));
                    } else if (userHR >= userMaxHeartRate) {
                        String msg = "운동강도가 초과됬습니다. 속도를 낮추고나 W를 낮춰주세요";
                        userStateMsgTextView.setText(msg);
                        userStateMsgTextView.setBackgroundColor(Color.YELLOW);

//                    customWaveView.setmBlowWaveColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.bsp_red));
//                    customWaveView.setmAboveWaveColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.bsp_red));
//                    customWaveView.setBackgroundColor(ContextCompat.getColor(IndoorBikeRealTimeActivity.this, R.color.bsp_red));
                    } else if (userHR == 0.0f) {
                        String msg = "심박센서 착용 및 위치 확인해주세요.";
                        userStateMsgTextView.setText(msg);
                        userStateMsgTextView.setBackgroundColor(Color.GREEN);

                    }

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
        // TODO: 2018-02-21 차트 속성 설정.
        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_bike_real_time);

//        showDialog();
        ButterKnife.bind(this);
        chronometer.setTextSize(20);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;

        mDeviceAddress = getIntent().getStringExtra(REAL_TIME_INDOOR_BIKE_DEVICE);

        Intent gattServiceIntent = new Intent(this, EZBLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        countdownView.start(1800000);
        countdownView.setVisibility(View.GONE);
        initChart();
        infoLayout.setVisibility(View.GONE);
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
    }

    private void displayData(String data) {
        if (data != null) {
            textView.append(data);
        }
    }

    @OnClick(R.id.home)
    public void onClickButton() {
//        finish();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림")
                .setMessage("운동을 종료하시겠어요?")
                .setPositiveButton("네", (dialog, which) -> finish());
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
                String avg_speed = meanSpeedTextView.getText().toString();
                // 이동거리
                String total_distance = totalDistanceTextView.getText().toString();
                // 평균BPM
                String avg_bpm = heartRateTextView.getText().toString();
                // 소모 칼로리
                String kcal = kcalTextview.getText().toString();
//            Log.d(TAG, "평균쇽도 = " + avg_speed);
//            Log.d(TAG, "이동거리 = " + total_distance);
//            Log.d(TAG, "평균심박 = " + avg_bpm);
//            Log.d(TAG, "칼로리량 = " + kcal);

                //            Log.d(TAG, "3333333333333333333333333333. EI =" + EI);
                Intent intent = new Intent(IndoorBikeRealTimeActivity.this, IndoorBikeResultActivity.class);

                Intent bsmintent = getIntent();
                before_bloodsugar = intent.getExtras().getString("before_bloodsugar");
                intent.putExtra("before_bloodsugar", before_bloodsugar);
                intent.putExtra("after_bloodsugar", after_bloodsugar);
                intent.putExtra("result_time", time);
                intent.putExtra("result_extra", avg_speed + "&" + total_distance + "&" + avg_bpm + "&" + kcal);

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
}