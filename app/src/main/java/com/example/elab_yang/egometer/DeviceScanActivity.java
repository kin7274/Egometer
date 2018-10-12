package com.example.elab_yang.egometer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DeviceScanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DeviceScanActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1000;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000; // Stops scanning after 10 seconds.

    RecyclerView recyclerView;
    Button scan_btn;
    Handler handler;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;

    ArrayList<BluetoothDevice> bleDeviceList;
    DeviceScanAdapter adapter;
    boolean mScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_scan);
        handler = new Handler();
        scan_btn = (Button) findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(this);
        bleDeviceList = new ArrayList<>();
        checkScanPermission();
        checkBleSupport();
        getBluetoothAdapter();
        checkBluetoothSupport();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void checkBleSupport() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE지원안대", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "블루투스지원안대", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkScanPermission() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("위치확인점");
            builder.setMessage("위치확인점");
            builder.setPositiveButton(android.R.string.ok, null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_btn) {
            Log.d(TAG,"버튼 클릭");
            if (!mScanning) {
                scan_btn.setText("STOP");
                bleDeviceList.clear();
                adapter.notifyDataSetChanged();
                scanLeDevice(true);
                Log.d(TAG,"버튼 클릭 종료");
            } else {
                scan_btn.setText("SCAN");
                scanLeDevice(false);
                Log.d(TAG,"버튼 이벤트 종료");
            }
        }
    }

    private void scanLeDevice(final boolean enable) {
        Log.d(TAG,"스킨을 시작");
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                    scan_btn.setText("SCAN");
                }
            }, SCAN_PERIOD);
            mScanning = true;
            startNEWBTLEDiscovery();
            Log.d(TAG,"BTL를 찾앗어");
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
            Log.d(TAG,"블루투스 정지");
        }
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if (bleDeviceList.size() < 1) {
                bleDeviceList.add(device);
                adapter.notifyDataSetChanged();
                Log.d(TAG,"bleDeviceListsize() =" + bleDeviceList.size());
            } else {
                boolean flag = true;
                for (int i = 0; i < bleDeviceList.size(); i++) {
                    if (device.getAddress().equals(bleDeviceList.get(i).getAddress())) {
                        flag = false;
                    }
                }
                if (flag) {
                    bleDeviceList.add(device);
                    adapter.notifyDataSetChanged();
                }
            }
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startNEWBTLEDiscovery() {
        bluetoothLeScanner.startScan(getScanFilters(), getScanSettings(), leScanCallback);
    }

    private List<ScanFilter> getScanFilters() {
        List<ScanFilter> allFilters = new ArrayList<>();
        ScanFilter scanFilter0 = new ScanFilter.Builder().setDeviceName("KNU EGO").build();
        allFilters.add(scanFilter0);
        return allFilters;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: 2018-07-21 스캔 동작하기 -- 박제창 (Dreamwalker)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, " +
                            "this app will not be able to discover BLE Device when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
                    builder.show();
                }
        }
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ScanSettings getScanSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new ScanSettings.Builder()
                    .setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setMatchMode(android.bluetooth.le.ScanSettings.MATCH_MODE_STICKY)
                    .build();
        } else {
            return new ScanSettings.Builder()
                    .setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bluetoothAdapter.isEnabled()) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        adapter = new DeviceScanAdapter(bleDeviceList, this);
        recyclerView.setAdapter(adapter);
        scanLeDevice(true);
        scan_btn.setText("STOP");
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        bleDeviceList.clear();
    }

    private void stopBLEDiscovery() {
        if (adapter != null)
            bluetoothLeScanner.stopScan(leScanCallback);
    }
}
