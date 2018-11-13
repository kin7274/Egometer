package com.example.elab_yang.egometer.activity.appInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.elab_yang.egometer.IActivityBasicSetting;
import com.example.elab_yang.egometer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppGuidenceActivity extends AppCompatActivity implements IActivityBasicSetting {

    @BindView(R.id.scene1)
    TextView scene1;

    @BindView(R.id.scene2)
    TextView scene2;

    @BindView(R.id.scene3)
    TextView scene3;

    @BindView(R.id.next_btn)
    Button next_btn;

    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidence);
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
        scene1.setVisibility(View.VISIBLE);
        scene2.setVisibility(View.GONE);
        scene3.setVisibility(View.GONE);
    }


    @OnClick(R.id.next_btn)
    void onClick() {
        if (flag == 0) {
            scene1.setVisibility(View.GONE);
            scene2.setVisibility(View.VISIBLE);
            scene3.setVisibility(View.GONE);
            flag = 1;
        } else if (flag == 1) {
            scene1.setVisibility(View.GONE);
            scene2.setVisibility(View.GONE);
            scene3.setVisibility(View.VISIBLE);
            flag = 0;
        }
    }
}
