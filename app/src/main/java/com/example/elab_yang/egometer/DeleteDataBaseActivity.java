package com.example.elab_yang.egometer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class DeleteDataBaseActivity extends AppCompatActivity {
    Context mContext;
    private static final String TAG = "DeleteDataBaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedb);
        mContext = this;
        setToolbar();
        setStatusbar();

        Button btn_ergo = (Button) findViewById(R.id.btn_ergo);
        btn_ergo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("ERGO")
                    .setMessage("저장된 내용을 전부 지울까요?")
                    .setPositiveButton("네", (dialog, which) -> Log.d(TAG, "onClick: ergo 클리어;;;"))
                    .setNegativeButton("아니오", (dialog, which) -> Log.d(TAG, "onClick: ergo 휴 다행"))
                    .show()
                    .create();
        });

        Button btn_tread = (Button) findViewById(R.id.btn_tread);
        btn_tread.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("TREAD")
                    .setMessage("저장된 내용을 전부 지울까요?")
                    .setPositiveButton("네", (dialog, which) -> Log.d(TAG, "onClick: TREAD 클리어;;;"))
                    .setNegativeButton("아니오", (dialog, which) -> Log.d(TAG, "onClick: TREAD 휴 다행"))
                    .show()
                    .create();
        });
    }

    public void setToolbar() {
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("");
        TextView bar_title = (TextView) findViewById(R.id.bar_title);
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }
}
