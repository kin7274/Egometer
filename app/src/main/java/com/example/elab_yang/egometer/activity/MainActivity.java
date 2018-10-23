package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.TestStartBeforeActivity;
import com.example.elab_yang.egometer.adapter.DeviceAdapter;
import com.example.elab_yang.egometer.model.Device;

import java.util.ArrayList;
import java.util.HashSet;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final long RIPPLE_DURATION = 250;
    RecyclerView recyclerView;
    //    DeviceAdapter deviceAdapter;
    DeviceAdapter deviceAdapter;
    HashSet<Device> deviceDatabase = new HashSet<>();
    ArrayList<Device> deviceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusbar();

        TextView textview = (TextView) findViewById(R.id.textview);
        textview.setVisibility(View.GONE);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mytoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Paper.init(this);
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
            textview.setVisibility(View.VISIBLE);
        }
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = menuItem.getItemId();
        switch (id) {
            // 호구조사
            case R.id.nav_profile:
//                Toast.makeText(getApplicationContext(),"장치 추가", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_add_device:
                // 장치 추가
//                Toast.makeText(getApplicationContext(),"장치 추가", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DeviceChoiceActivity.class));
                break;
            case R.id.nav_db_check:
                // DB 확인
//                Toast.makeText(getApplicationContext(),"DB 확인", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DBChoiceActivity.class));
                break;
            case R.id.nav_load_test:
                // 운동부하검사
                startActivity(new Intent(MainActivity.this, TestStartBeforeActivity.class));
                break;
            //환경설정
            case R.id.nav_setting:
                Toast.makeText(getApplicationContext(),"환경설정", Toast.LENGTH_SHORT).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}