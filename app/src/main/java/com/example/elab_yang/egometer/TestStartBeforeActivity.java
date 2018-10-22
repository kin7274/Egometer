package com.example.elab_yang.egometer;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestStartBeforeActivity extends AppCompatActivity implements IActivityBasicSetting {
    @BindView(R.id.top_title_message)
    TextView topTextView;

    @BindView(R.id.mid_title_message)
    TextView midTextView;

    @BindView(R.id.start_button)
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test_start_before);
        bindView();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {

    }

    private IntentFilter intentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        return intentFilter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.start_button)
    public void onClickedStartButton() {
        startActivity(new Intent(this, BikeScanActivity.class));
        finish();
    }
}
