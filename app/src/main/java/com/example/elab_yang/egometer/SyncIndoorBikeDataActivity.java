package com.example.elab_yang.egometer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import static com.example.elab_yang.egometer.EZSyncService.ACTION_FINAL_DONE;
import static com.example.elab_yang.egometer.EZSyncService.EXTRA_FINAL_DATA;
import static com.example.elab_yang.egometer.IntentConst.SYNC_INDOOR_BIKE_DEVICE;

public class SyncIndoorBikeDataActivity extends AppCompatActivity {
    private static final String TAG = "SyncIndoorBikeDataActiv";

    @BindView(R.id.result)
    TextView _result;
    @BindView(R.id.home)
    ImageView backImageView;

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    BluetoothGattCharacteristic mNotifyCharacteristic;
    EZSyncService mBluetoothLeService;
    boolean mConnected = false;

    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    String deviceAddress;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((EZSyncService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
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
        intentFilter.addAction(EZSyncService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(EZSyncService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(EZSyncService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(EZSyncService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(EZSyncService.ACTION_HEART_RATE_AVAILABLE);
        intentFilter.addAction(EZSyncService.ACTION_INDOOR_BIKE_AVAILABLE);
        intentFilter.addAction(EZSyncService.ACTION_TREADMILL_AVAILABLE);
        //
        intentFilter.addAction(EZSyncService.ACTION_FIRST_DONE);
        intentFilter.addAction(EZSyncService.ACTION_SECOND_DONE);
        //
        intentFilter.addAction(ACTION_FINAL_DONE);
        intentFilter.addAction(EXTRA_FINAL_DATA);
        return intentFilter;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (EZSyncService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                _result.setText("Connected !");
                invalidateOptionsMenu();
            } else if (EZSyncService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                _result.setText("DISCONNECTED ! ");
                invalidateOptionsMenu();
            }

            else if (EZSyncService.ACTION_FIRST_DONE.equals(action)){
                _result.setText("시간 동기화 완료");
            }
            else if (EZSyncService.ACTION_SECOND_DONE.equals(action)){
                _result.setText("장비 인증 완료 ");
            }
            else if (ACTION_FINAL_DONE.equals(action)){
                // TODO: 2018-09-05 동기화 완료되면 처리하는 부분이고 전달은 gson으로처리함 . - 박제창
                // TODO: 2018-09-05 전달할 데이터는 페이퍼 케시에 임시 저장함. - 바ㄱ제차ㅇ
                _result.setText("데이터 동기화 완료");
                String resultString = intent.getStringExtra(EXTRA_FINAL_DATA);
                Log.e(TAG, "최종 전달 받은 값 onReceive: " + resultString);
//                Gson gson = new Gson();
//                ArrayList<Fitness> result = gson.fromJson(resultString, new TypeToken<ArrayList<Fitness>>(){}.getType());
//                Log.e(TAG, "onReceive: " + result.size());
//                _result.setText("정보 처리 중...");
//
//                if (Paper.book("syncIndoorBike").read("data") == null) {
//                    Paper.book("syncIndoorBike").write("data", result);
//                    startActivity(new Intent(SyncIndoorBikeDataActivity.this, SyncIndoorBikeResultActivity.class));
//                    finish();
//                } else {
//                    Paper.book("syncIndoorBike").delete("data");
//                    Paper.book("syncIndoorBike").write("data", result);
//                    startActivity(new Intent(SyncIndoorBikeDataActivity.this, SyncIndoorBikeResultActivity.class));
//                    finish();
                }
            }

//            else if (EZSyncService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//                 TODO: 2018-07-24 서비스와 연결되엉ㅅ을때 방송되어 받아지는 리시버 - 박제창
//            } else if (EZSyncService.ACTION_DATA_AVAILABLE.equals(action)) {
//                Log.e(TAG, "onReceive: " + intent.getStringExtra(EZBLEService.EXTRA_DATA));
//            } else if (EZSyncService.ACTION_HEART_RATE_AVAILABLE.equals(action)) {
//                Log.e(TAG, "onReceive:  실시간 화면에서 심박수 받앗어요 ");
//                String hr = intent.getStringExtra(EZBLEService.EXTRA_DATA);
//            } else if (EZSyncService.ACTION_INDOOR_BIKE_AVAILABLE.equals(action)) {
//                String nowSpeed = intent.getStringExtra(EZBLEService.EXTRA_DATA);
//            } else if (EZSyncService.ACTION_TREADMILL_AVAILABLE.equals(action)) {
//                String totalDistance = intent.getStringExtra(EZBLEService.EXTRA_DATA);
//            }
//        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_indoor_bike_data);

        Paper.init(this);
        ButterKnife.bind(this);

        deviceAddress = getIntent().getStringExtra(SYNC_INDOOR_BIKE_DEVICE);
        Log.e(TAG, "onCreate: " + deviceAddress);

        boolean isBleAvailable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);

        if (isBleAvailable) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "블루투스 4.0을 지원안하네 이 폰;;;", Toast.LENGTH_SHORT).show();
            }
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }
        Intent gattServiceIntent = new Intent(this, EZSyncService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(deviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLeService != null){

        }
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @OnClick(R.id.home)
    public void homeImageButtonClicked(View v) {
//        unbindService(mServiceConnection);
//        mBluetoothLeService = null;
        finish();
    }
}
