package com.example.elab_yang.egometer.activity.treadmill;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.adapter.MyRecyclerAdapter2;
import com.example.elab_yang.egometer.model.CardItem2;
import com.example.elab_yang.egometer.model.DB;
import com.example.elab_yang.egometer.model.DB2;

import java.util.ArrayList;
import java.util.List;

public class TREADgetDBActivity extends AppCompatActivity {
    Context mContext;
    DB2 db;
    SQLiteDatabase sql;

    String data;
    String abc[] = {"", "", "", "", "", "", ""};
    List<CardItem2> lists;
    private MyRecyclerAdapter2 mAdapter;
    RecyclerView recycler_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbcheck);
        mContext = this;
        setStatusbar();
        setRecyclerView();
        db = new DB2(this);
        setSpinner();
//        getDB();
    }

    public void setStatusbar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurle));
    }

    public void setSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        String[] list2 = new String[3];
        list2[0] = "사용자1";
        list2[1] = "사용자2";
        list2[2] = "사용자3";
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list2);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String user_id = spinner.getItemAtPosition(position).toString().substring(3, 4);
                Toast.makeText(TREADgetDBActivity.this.getApplicationContext(), "선택한 아이템 : " + user_id, Toast.LENGTH_SHORT).show();
                getDB(user_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        mAdapter = new MyRecyclerAdapter2(lists);
        recycler_view.setAdapter(mAdapter);
    }

    public void getDB(String user_id) {
        sql = db.getReadableDatabase();
        // 화면 clear
        data = "";
        Cursor cursor;
        lists.clear();
        cursor = sql.rawQuery("select*from tb_treadmill", null);
        while (cursor.moveToNext()) {
            data += cursor.getString(0) + ","
                    + cursor.getString(1) + ","
                    + cursor.getString(2) + ","
                    + cursor.getString(3) + ","
                    + cursor.getString(4) + ","
                    + cursor.getString(5) + ","
                    + cursor.getString(6) + "\n";
            if (cursor.getString(1).contains(user_id)) {
                lists.add(new CardItem2(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            }
        }
        mAdapter.notifyDataSetChanged();
        cursor.close();
        sql.close();
        Toast.makeText(getApplicationContext(), "조회하였습니다.", Toast.LENGTH_SHORT).show();
    }
}
