package com.example.elab_yang.egometer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.example.elab_yang.egometer.IActivityBasicSetting;
import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.TestStartBeforeActivity;
import com.example.elab_yang.egometer.activity.appInfo.AppGuidenceActivity;
import com.example.elab_yang.egometer.adapter.DeviceAdapter;
import com.example.elab_yang.egometer.model.Device;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements IActivityBasicSetting, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final long RIPPLE_DURATION = 250;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.text_emptydevice)
    TextView emptydevice;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    DeviceAdapter deviceAdapter;
    HashSet<Device> deviceDatabase = new HashSet<>();
    ArrayList<Device> deviceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        Paper.init(this);
        bindView();
        setStatusbar();
        setNavi();
        setDevice();
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    public void setNavi() {
        // toolbar
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mytoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setDevice() {
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
            emptydevice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
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
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_profile:
                // 호구조사
//                Toast.makeText(getApplicationContext(),"장치 추가", Toast.LENGTH_SHORT).show();

                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                if (pref.getString("user_data0", "").equals("")) {
                    Snackbar.make(this.getWindow().getDecorView().getRootView(),
                            "먼저! 환경설정 -> 개인정보 입력해주세요", 3000).setAction("확인", v -> startActivity(new Intent(getApplicationContext(), EditProfileActivity.class))).show();
                    // 기존값이 없구나
                } else {
                    startActivity(new Intent(this, ProfileActivity.class));
                }
                break;

            case R.id.nav_add_device:
                // 장치 추가
//                Toast.makeText(getApplicationContext(),"장치 추가", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DeviceChoiceActivity.class));
                break;

            case R.id.nav_db_check:
                // DB 확인
//                Toast.makeText(getApplicationContext(),"DB 확인", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DBChoiceActivity.class));
                break;

            case R.id.nav_bloodsuger:
                // 혈당 다이어리
//                Toast.makeText(getApplicationContext(),"DB 확인", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, BloodSugarActivity.class));
                break;

            case R.id.nav_load_test:
                // 운동 부하 검사
                startActivity(new Intent(this, TestStartBeforeActivity.class));
                break;

            case R.id.nav_result_test:
                // 운동 부하 검사 결과
//                Toast.makeText(getApplicationContext(),"환경설정", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, TestResult2Activity.class));
                break;

            case R.id.nav_guide:
                // 사용자가이드
                startActivity(new Intent(this, AppGuidenceActivity.class));
                break;

            case R.id.nav_setting:
                // 환경설정
//                Toast.makeText(getApplicationContext(),"환경설정", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}