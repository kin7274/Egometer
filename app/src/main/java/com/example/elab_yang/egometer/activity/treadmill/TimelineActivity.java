package com.example.elab_yang.egometer.activity.treadmill;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.elab_yang.egometer.service.BluetoothLeService;
import com.example.elab_yang.egometer.R;

import static com.example.elab_yang.egometer.service.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static com.example.elab_yang.egometer.service.BluetoothLeService.ACTION_DATA_AVAILABLE_CHANGE;
import static com.example.elab_yang.egometer.service.BluetoothLeService.EXTRA_DATA;
import static com.example.elab_yang.egometer.service.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;

public class TimelineActivity extends AppCompatActivity {
    private final static String TAG = TimelineActivity.class.getSimpleName();
    Context mContext;
    TextView text1, text2;
    //
    String deviceAddress = "";
    String message, abc = "";
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            text1.setVisibility(View.GONE);
            text2.setVisibility(View.VISIBLE);
            Intent intent = new Intent(TimelineActivity.this, receiveData.class);
            intent.putExtra("BLE", abc);
            abc = "";
            startActivity(intent);
            finish();
        }
    };
    //
    BluetoothLeService mBluetoothLeService = new BluetoothLeService();
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE_CHANGE.equals(action)) {
                message = (intent.getStringExtra(EXTRA_DATA)).substring(0, 20);
                Log.d(TAG, "message = " + message);
                abc += message;
            }
            // 블루투스값 쭉 모음 = abc
            Log.d(TAG, "abc = " + abc);
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(deviceAddress);
            Log.d(TAG, "서비스가 연결되었습니다!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // 메인
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mContext = this;
        // 상태바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setLottie();
        set();
        //
        deviceAddress = getIntent().getStringExtra(REAL_TIME_INDOOR_BIKE_DEVICE);
        if (deviceAddress != null) {
            Log.d(TAG, "onCreate: " + deviceAddress);
        }
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(ACTION_DATA_AVAILABLE);
        intentfilter.addAction(ACTION_DATA_AVAILABLE_CHANGE);
        registerReceiver(mMessageReceiver, intentfilter);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        //
        mBluetoothLeService.writeCharacteristic("o");
    }

    public void setLottie(){
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.loop(true);
        animationView.playAnimation();
    }

    public void set(){
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text1.setVisibility(View.VISIBLE);
        text2.setVisibility(View.GONE);
    }
    // 종료ㅡ 서비스도
    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 2초 뒤에 Runnable 객체 수행
        handler.postDelayed(r, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 예약 취소
        handler.removeCallbacks(r);
    }
}
