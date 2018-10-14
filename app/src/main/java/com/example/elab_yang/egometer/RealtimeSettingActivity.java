package com.example.elab_yang.egometer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RealtimeSettingActivity extends AppCompatActivity implements IActivityBasicSetting, CircleTimerView.CircleTimerListener {

    private static final String TAG = "RealtimeSettingActivity";

    String deviceAddress;

    @BindView(R.id.start_fitness_button)
    Button startFitnessButton;


    @BindView(R.id.ctv)
    CircleTimerView circleTimerView;

    @BindView(R.id.info)
    ImageView infoButton;


    // TODO: 2018-10-10 0 : 저강도, 1: 중강도, 2: 고강도
    // TODO: 2018-10-10 defualt value :  시간: 30분, 중강도
    int workoutTime = 1800;
    int workoutIntense = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_setting);
        initSetting();
        deviceAddress = getIntent().getStringExtra(IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE);

    }


    private void initTimerView() {
        circleTimerView.setCurrentTime(1800);
        circleTimerView.setCircleTimerListener(this);
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
        initTimerView();
    }

    @Override
    public void onTimerStop() {
    }

    @Override
    public void onTimerStart(int time) {
        Log.e(TAG, "onTimerStart: " + time);
    }

    @Override
    public void onTimerPause(int time) {
        Log.e(TAG, "onTimerPause: " + time);
    }

    @Override
    public void onTimerTimingValueChanged(int time) {
        Log.e(TAG, "onTimerTimingValueChanged: " + time);
    }

    @Override
    public void onTimerSetValueChanged(int time) {
        Log.e(TAG, "onTimerSetValueChanged: " + time);
        workoutTime = time;
    }

    @Override
    public void onTimerSetValueChange(int time) {
        Log.e(TAG, "onTimerSetValueChange: " + time);
        workoutTime = time;
    }

    @OnClick(R.id.start_fitness_button)
    public void onClickedStartFitnessButton() {
        Bundle bundle = new Bundle();

        Intent intent = new Intent(RealtimeSettingActivity.this, IndoorBikeRealTimeActivity.class);
        bundle.putString(IntentConst.REAL_TIME_DEVICE_ADDEDSS, deviceAddress);
        bundle.putInt(IntentConst.REAL_TIME_WORKOUT_TOTAL_TIME, workoutTime);
        bundle.putInt(IntentConst.REAL_TIME_WORKOUT_INTENSE, workoutIntense);
        intent.putExtra(IntentConst.REAL_TIME_SETTING_FITNESS_INFO, bundle);
        startActivity(intent);
        finish();

    }

    @OnClick(R.id.info)
    public void onClickedInfoButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("정보");
        builder.setMessage("운동 시간은 30-60분이 적당합니다. 혈당 감소에는 중강도 운동, 고강도 운동이 효과가 있다는 보고가 있습니다. 시간을 설정한 후 운동 강도를 설정해주세요");
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
