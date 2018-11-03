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

import com.example.elab_yang.egometer.model.DB;
import com.example.elab_yang.egometer.model.DB2;

public class DeleteDataBaseActivity extends AppCompatActivity {
    private static final String TAG = "DeleteDataBaseActivity";
    Context mContext;
    DB db;  // ergo
    DB2 db2;  // tread
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedb);
        mContext = this;
//        setToolbar();
        setStatusbar();
        db = new DB(this);
        db2 = new DB2(this);

        TextView txt_ergo = (TextView) findViewById(R.id.txt_ergo);
        txt_ergo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("ERGO")
                    .setMessage("저장된 내용을 전부 지울까요?")
                    .setPositiveButton("네", (dialog, which) -> {
                        Log.d(TAG, "onClick: ergo 클리어;;;");
                        sql = db.getWritableDatabase();
                        db.onUpgrade(sql, 1, 2);
                    })
                    .setNegativeButton("아니오", (dialog, which) -> Log.d(TAG, "onClick: ergo 휴 다행"))
                    .show()
                    .create();
        });
        TextView txt_tread = (TextView) findViewById(R.id.txt_tread);
        txt_tread.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("TREAD")
                    .setMessage("저장된 내용을 전부 지울까요?")
                    .setPositiveButton("네", (dialog, which) -> {
                        Log.d(TAG, "onClick: TREAD 클리어;;;");
                        sql = db2.getWritableDatabase();
                        db2.onUpgrade(sql, 1, 2);
                    })
                    .setNegativeButton("아니오", (dialog, which) -> Log.d(TAG, "onClick: TREAD 휴 다행"))
                    .show()
                    .create();
        });
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
}
