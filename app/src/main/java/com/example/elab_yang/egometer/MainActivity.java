package com.example.elab_yang.egometer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import static com.example.elab_yang.egometer.DeviceControlActivity.EXTRAS_DEVICE_ADDRESS;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView deviceName, deviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setStatusbar();
        set();
    }

    // 툴바
    public void setToolbar() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
    }

    // 상태바 색 변경
    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    // 객체 생성
    public void set() {
        // 장치 추가 버튼
        Button add_device = (Button) findViewById(R.id.add_device);
        add_device.setOnClickListener(this);
        // 페어링
        Button pairing_device = (Button) findViewById(R.id.pairing_device);
        pairing_device.setOnClickListener(this);
        // 장치 이름과 주소 표기
        deviceName = (TextView) findViewById(R.id.txt_device_name);
        deviceAddress = (TextView) findViewById(R.id.txt_device_address);
    }

    // 블루투스 설정 후 Device Name, Address 가져옴
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String device_name_address = data.getStringExtra("result");
                String[] device = device_name_address.split(",");
                // 디바이스 이름과 주소 표시
                deviceName.setText(device[0]);
                deviceAddress.setText(device[1]);
            }
        }
    }

    // 클릭 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 장치 추가하러 ㄱㄱ
            case R.id.add_device:
                Intent intent = new Intent(MainActivity.this, DeviceScanActivity.class);
                startActivityForResult(intent, 1);
                break;
            // 페어링
            case R.id.pairing_device:
                Intent intent2 = new Intent(MainActivity.this, DeviceControlActivity.class);
                intent2.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, deviceName.getText());
                intent2.putExtra(EXTRAS_DEVICE_ADDRESS, deviceAddress.getText());
                startActivity(intent2);
                break;
        }
    }
}
