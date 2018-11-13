package com.example.elab_yang.egometer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.elab_yang.egometer.activity.database.ERGO_DBHelper;
import com.example.elab_yang.egometer.activity.database.TREAD_DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteDataBaseActivity extends AppCompatActivity implements IActivityBasicSetting {
    private static final String TAG = "DeleteDataBaseActivity";
    Context mContext;
    ERGO_DBHelper ERGODbHelper;  // ergo
    TREAD_DBHelper TREADDbHelper;  // tread
    SQLiteDatabase sql;

    @BindView(R.id.txt_ergo)
    TextView txt_ergo;

    @BindView(R.id.txt_tread)
    TextView txt_tread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedb);
        initSetting();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initSetting() {
        bindView();
        setStatusbar();
        mContext = this;
        ERGODbHelper = new ERGO_DBHelper(this);
        TREADDbHelper = new TREAD_DBHelper(this);
    }

    //    public void setToolbar() {
//        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(mytoolbar);
//        getSupportActionBar().setTitle("");
//    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    @OnClick(R.id.txt_ergo)
    void click1() {
        showYourDialog(0);
    }

    @OnClick(R.id.txt_tread)
    void click2() {
        showYourDialog(1);
    }

    public void showYourDialog(final int flag) {
        if (flag == 0) {
            // ergo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("에르고미터")
                    .setCancelable(false)
                    .setMessage("저장된 내용을 전부 지우겠습니까?")
                    .setPositiveButton("네", (dialog, which) -> {
                        Log.d(TAG, "onClick: ergo 클리어;;;");
                        sql = ERGODbHelper.getWritableDatabase();
                        ERGODbHelper.onUpgrade(sql, 1, 2);
                    })
                    .setNegativeButton("아니오", (dialog, which) -> Log.d(TAG, "onClick: ergo 휴 다행"))
                    .show()
                    .create();
        } else {
            //tread
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("트레드밀")
                    .setCancelable(false)
                    .setMessage("저장된 내용을 전부 지우겠습니까?")
                    .setPositiveButton("네", (dialog, which) -> {
                        Log.d(TAG, "onClick: TREAD 클리어;;;");
                        sql = TREADDbHelper.getWritableDatabase();
                        TREADDbHelper.onUpgrade(sql, 1, 2);
                    })
                    .setNegativeButton("아니오", (dialog, which) -> Log.d(TAG, "onClick: TREAD 휴 다행"))
                    .show()
                    .create();
        }
    }
}
