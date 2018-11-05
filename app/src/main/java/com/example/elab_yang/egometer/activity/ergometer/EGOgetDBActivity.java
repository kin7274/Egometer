package com.example.elab_yang.egometer.activity.ergometer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.adapter.MyRecyclerAdapter;
import com.example.elab_yang.egometer.model.CardItem;
import com.example.elab_yang.egometer.model.DB;

import java.util.ArrayList;
import java.util.List;

public class EGOgetDBActivity extends AppCompatActivity {
    private static final String TAG = "getDBActivity";

    Context mContext;
    DB db;
    SQLiteDatabase sql;
    String data;

    Button add_btn;

    List<CardItem> lists;
    private MyRecyclerAdapter mAdapter;
    RecyclerView recycler_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdb);
        setToolbar();
        setStatusbar();
        mContext = this;
        setRecyclerView();

        add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(v -> {
//            selectDialog();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(EGOgetDBActivity.this)
                    .inflate(R.layout.edit_box, null, false);
            builder.setView(view);

            final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
            final EditText edit1 = (EditText) view.findViewById(R.id.edit1);
            final EditText edit2 = (EditText) view.findViewById(R.id.edit2);
            final EditText edit3 = (EditText) view.findViewById(R.id.edit3);
            final EditText edit4 = (EditText) view.findViewById(R.id.edit4);
            final EditText edit5 = (EditText) view.findViewById(R.id.edit5);
            final EditText edit6 = (EditText) view.findViewById(R.id.edit6);
            final EditText edit7 = (EditText) view.findViewById(R.id.edit7);
            final EditText edit8 = (EditText) view.findViewById(R.id.edit8);
            final EditText edit9 = (EditText) view.findViewById(R.id.edit9);
            ButtonSubmit.setText("삽입");
            final AlertDialog dialog = builder.create();
            ButtonSubmit.setOnClickListener(v1 -> {
                String strEdit1 = edit1.getText().toString();
                String strEdit2 = edit2.getText().toString();
                String strEdit3 = edit3.getText().toString();
                String strEdit4 = edit4.getText().toString();
                String strEdit5 = edit5.getText().toString();
                String strEdit6 = edit6.getText().toString();
                String strEdit7 = edit7.getText().toString();
                String strEdit8 = edit8.getText().toString();
                String strEdit9 = edit9.getText().toString();
                // 디뽈트값
                lists.add(new CardItem(strEdit1, strEdit1, strEdit2, strEdit3, strEdit4, strEdit5, strEdit6, strEdit7, strEdit8, strEdit9));
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });

            dialog.show();
        });
        db = new DB(this);
        getDB();
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

    public void setRecyclerView() {
        // 객체 생성
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 반대로 쌓기
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(layoutManager);
        // 배열 null 예외처리
        try {
            lists = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new MyRecyclerAdapter(lists);
        recycler_view.setAdapter(mAdapter);
    }

    public void getDB() {
        sql = db.getReadableDatabase();
        // 화면 clear
        data = "";
        Cursor cursor;
        cursor = sql.rawQuery("select*from tb_egometer", null);
        while (cursor.moveToNext()) {
            data += cursor.getString(1) + ","
                    + cursor.getString(2) + ","
                    + cursor.getString(3) + ","
                    + cursor.getString(4) + ","
                    + cursor.getString(5) + ","
                    + cursor.getString(6) + ","
                    + cursor.getString(7) + ","
                    + cursor.getString(8) + ","
                    + cursor.getString(9) + ","
                    + cursor.getString(10) + "\n";
            lists.add(new CardItem(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10)));
        }
        mAdapter.notifyDataSetChanged();
        cursor.close();
        sql.close();
        Toast.makeText(getApplicationContext(), "조회하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public void set_setDB() {
        int cnt = lists.size();
//        Toast.makeText(getApplicationContext(), "cnt = " + cnt, Toast.LENGTH_SHORT).show();
        sql = db.getWritableDatabase();
        db.onUpgrade(sql, 1, 2);
        for (int i = 0; i < cnt; i++) {
            Log.d(TAG, i + " = " + lists.get(i).getDate());
            Log.d(TAG, i + " = " + lists.get(i).getTime());
            Log.d(TAG, i + " = " + lists.get(i).getSpeed());
            Log.d(TAG, i + " = " + lists.get(i).getDistance());
            Log.d(TAG, i + " = " + lists.get(i).getBpm());
            Log.d(TAG, i + " = " + lists.get(i).getKcal());
            Log.d(TAG, i + " = " + lists.get(i).getKcal());
            Log.d(TAG, i + " = " + lists.get(i).getKcal());
            Log.d(TAG, i + " = " + lists.get(i).getBefore_bloodsugar());
            Log.d(TAG, i + " = " + lists.get(i).getAfter_bloodsugar());
            Log.d(TAG, i + " = " + lists.get(i).getNum());
            Log.d(TAG, i + " = " + lists.get(i).getMemo());
            setDB(lists.get(i).getDate(), lists.get(i).getTime(), lists.get(i).getSpeed(), lists.get(i).getDistance(), lists.get(i).getBpm(), lists.get(i).getKcal(), lists.get(i).getBefore_bloodsugar()
                    , lists.get(i).getAfter_bloodsugar(), lists.get(i).getNum(), lists.get(i).getMemo());
        }
    }

    // DB에 저장하는 메서드
    public void setDB(String date, String time, String speed, String distance, String bpm, String kcal, String before_bloodsugar, String after_bloodsugar, String num, String memo) {
        sql = db.getWritableDatabase();
        sql.execSQL(String.format("INSERT INTO tb_egometer VALUES(null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", date, time, speed, distance, bpm, kcal, before_bloodsugar, after_bloodsugar, num, memo));
        sql.close();
    }

    //
    @Override
    public void onBackPressed() {
        set_setDB();
        finish();
    }
}
