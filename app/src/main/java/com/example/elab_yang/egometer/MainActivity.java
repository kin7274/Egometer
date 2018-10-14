package com.example.elab_yang.egometer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;

import io.paperdb.Paper;

// 메인이야
public class MainActivity extends AppCompatActivity {
    //
    private static final String TAG = "MainActivity";
    private static final long RIPPLE_DURATION = 250;
    Context context;
    //
    LinearLayout deviceLayout;
    //
    RecyclerView recyclerView;
    //
    DeviceAdapter deviceAdapter;
    //
    String mDeviceName;
    String mDeviceAddress;
    //
    HashSet<Device> deviceDatabase = new HashSet<>();
    ArrayList<Device> deviceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Button add_device = (Button) findViewById(R.id.add_device);
        add_device.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MachineScanActivity.class)));
        //
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        deviceDatabase = Paper.book("device").read("user_device");
        if (deviceDatabase != null) {
            if (deviceDatabase.size() != 0) {
                deviceArrayList = new ArrayList<>(deviceDatabase);
                deviceAdapter = new DeviceAdapter(this, deviceArrayList);
                recyclerView.setAdapter(deviceAdapter);
                for (int i = 0; i < deviceArrayList.size(); i++) {
                    Device device = deviceArrayList.get(i);
                    Log.e(TAG, "onCreate: " + device.getDeviceName() + ", " + device.getDeviceAddress());
                }
            }
        } else {
            Log.e(TAG, "onCreate: " + "등록된 장비 없음");
        }
    }
}
