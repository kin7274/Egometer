package com.example.elab_yang.egometer;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.elab_yang.egometer.etc.IntentConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BikeScanActivity extends AppCompatActivity implements IActivityBasicSetting, DeviceItemClickListener {
    private static final String TAG = "BikeScanActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1000;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000; // Stops scanning after 10 seconds.
    private TextToSpeech tts;
    private Handler mHandler;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    DeviceScanAdapterV2 adapter;
    ArrayList<BluetoothDevice> bleDeviceList;
    ArrayList<ScanResult> scanResultArrayList;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    Handler handler;
    boolean mScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_scan);
        initSetting();
        setStatusbar();
        setSupportActionBar(toolbar);
        guide_speech();
        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BikeScanActivity.this);
            builder.setTitle("알림");
            builder.setMessage("장비 검색과 운동부하 검사를 종료하시겠어요?");
            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> finish());
            builder.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        bleDeviceList = new ArrayList<>();
        scanResultArrayList = new ArrayList<>();
        handler = new Handler();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        checkScanPermission();

        checkBleSupport();
        getBluetoothAdapter();
        checkBluetoothSupport();


        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            adapter = new DeviceScanAdapterV2(bleDeviceList, scanResultArrayList, this);
            recyclerView.setAdapter(adapter);
            scanLeDevice(true);
//            StateButton.setText("STOP");
        }
        adapter.setDeviceItemClickListener(this);
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
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
                    tts.speak("장치를 선택해주세요", TextToSpeech.QUEUE_FLUSH, null, null);
                    // QUEUE_ADD : 위에꺼 다 끝나면
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkScanPermission() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.permission_alert_title));
            builder.setMessage(getString(R.string.permission_alert_message));
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(dialog -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION));
            builder.show();
        }
    }

    private void checkBleSupport() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        }
    }

    private void getBluetoothAdapter() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    private void checkBluetoothSupport() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        } else {

            if (enable) {
                handler.postDelayed(() -> {
                    Log.e(TAG, "scanLeDevice: ---> " + "Scan Handler Done!!");
                    mScanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                    invalidateOptionsMenu();
//                        StateButton.setText("SCAN");
//                        animationView.cancelAnimation();
//                        animationView.setFrame(0);
                }, SCAN_PERIOD);
                mScanning = true;
                startNEWBTLEDiscovery();
                invalidateOptionsMenu();
                //bluetoothLeScanner.startScan(leScanCallback);
            } else {
                mScanning = false;
                bluetoothLeScanner.stopScan(leScanCallback);
                invalidateOptionsMenu();
            }
        }

    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
//            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();

            // TODO: 2018-10-12 중복 방지
            if (bleDeviceList.size() < 1) {
                bleDeviceList.add(device);
                scanResultArrayList.add(result);
                adapter.notifyDataSetChanged();
            } else {
                boolean flag = true;
                for (int i = 0; i < bleDeviceList.size(); i++) {
                    if (device.getAddress().equals(bleDeviceList.get(i).getAddress())) {
                        flag = false;
                    }
                }
                if (flag) {
                    bleDeviceList.add(device);
                    scanResultArrayList.add(result);
                    adapter.notifyDataSetChanged();
                }
            }
            //String address = device.getAddress();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    // New BTLE Discovery use startScan (List<ScanFilter> filters,
    //                                  ScanSettings settings,
    //                                  ScanCallback callback)
    // It's added on API21
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startNEWBTLEDiscovery() {
        // Only use new API when user uses Lollipop+ device
        if (bluetoothLeScanner == null) {
            getBluetoothAdapter();
        } else {
            bluetoothLeScanner.startScan(getScanFilters(), getScanSettings(), leScanCallback);
        }
    }

    private List<ScanFilter> getScanFilters() {
        List<ScanFilter> allFilters = new ArrayList<>();
        ScanFilter scanFilter0 = new ScanFilter.Builder().setDeviceName("KNU EG0").build();
        allFilters.add(scanFilter0);
        return allFilters;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ScanSettings getScanSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
                    .build();
        } else {
            return new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_scan, menu);
        if (!mScanning) {
            menu.findItem(R.id.cancel).setVisible(false);
            menu.findItem(R.id.scan).setVisible(true);
        } else {
            menu.findItem(R.id.cancel).setVisible(true);
            menu.findItem(R.id.scan).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                bleDeviceList.clear();
                scanResultArrayList.clear();
                adapter.notifyDataSetChanged();
                scanLeDevice(true);
                break;
            case R.id.cancel:
                scanLeDevice(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View v, int position) {
        Log.e(TAG, "onItemClick: " + position);
        String deviceAddress = bleDeviceList.get(position).getAddress();
        String deviceName = bleDeviceList.get(position).getName();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Check");
        builder.setMessage(deviceName + "의 엑세서리로 검사를 시작합니다.");
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {

            Intent intent = new Intent(this, LoadTestActivity.class);
            intent.putExtra(IntentConst.FITNESS_LOAD_TEST_DEVICE_ADDRESS, deviceAddress);
            startActivity(intent);
            finish();
        });
        builder.show();
    }

    @Override
    public void onItemLongClick(View v, int position) {
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
