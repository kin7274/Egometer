package com.example.elab_yang.egometer.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.elab_yang.egometer.IActivityBased;
import com.example.elab_yang.egometer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity implements IActivityBased {
    private static final String TAG = "EditProfileActivity";

    @BindView(R.id.user_name)
    EditText user_name;

    @BindView(R.id.user_age)
    EditText user_age;

    @BindView(R.id.user_blood_max)
    EditText user_blood_max;

    @BindView(R.id.user_blood_min)
    EditText user_blood_min;

    @BindView(R.id.user_weight)
    EditText user_weight;

    @BindView(R.id.user_height)
    EditText user_height;

    @BindView(R.id.set_btn)
    Button set_btn;

    // 저장값
    String[] user_data = {"", "", "", "", "", ""};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
        setToolbar();
        setStatusbar();
    }

    public void setToolbar() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    // 저장하기 버튼 클릭 시
    @OnClick(R.id.set_btn)
    void onClick() {
        // TODO: 2018-11-12 입력칸 빈 null값 처리
        // string 가져와 모두다 캐시에 저ㅡ장
        set_cache();
        finish();
    }

    // 후루룹쩝쩝
    // 모두 긁어서 캐시에 저장한다!
    public void set_cache() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_data0", user_name.getText().toString());
        editor.putString("user_data1", user_age.getText().toString());
        editor.putString("user_data2", user_blood_min.getText().toString());
        editor.putString("user_data3", user_blood_max.getText().toString());
        editor.putString("user_data4", user_weight.getText().toString());
        editor.putString("user_data5", user_height.getText().toString());
        editor.apply();
    }
}
