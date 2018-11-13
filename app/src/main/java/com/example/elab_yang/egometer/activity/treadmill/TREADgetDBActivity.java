package com.example.elab_yang.egometer.activity.treadmill;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.adapter.MyRecyclerAdapter2;
import com.example.elab_yang.egometer.model.CardItem2;
import com.example.elab_yang.egometer.activity.database.TREAD_DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TREADgetDBActivity extends AppCompatActivity {
    Context mContext;
    TREAD_DBHelper db;
    SQLiteDatabase sql;

    Button add_btn;

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
        db = new TREAD_DBHelper(this);
        setSpinner();
//        getDB();
        add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(v -> {
//            selectDialog();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(TREADgetDBActivity.this).inflate(R.layout.edit_box2, null, false);
            builder.setView(view);

            final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
            final EditText edit1 = (EditText) view.findViewById(R.id.edit1);
            final EditText edit2 = (EditText) view.findViewById(R.id.edit2);
            final EditText edit3 = (EditText) view.findViewById(R.id.edit3);
            final EditText edit4 = (EditText) view.findViewById(R.id.edit4);
            final EditText edit5 = (EditText) view.findViewById(R.id.edit5);
            final EditText edit6 = (EditText) view.findViewById(R.id.edit6);
            ButtonSubmit.setText("삽입");
            final AlertDialog dialog = builder.create();
            ButtonSubmit.setOnClickListener(v1 -> {
                String strEdit1 = edit1.getText().toString();
                String strEdit2 = edit2.getText().toString();
                String strEdit3 = edit3.getText().toString();
                String strEdit4 = edit4.getText().toString();
                String strEdit5 = edit5.getText().toString();
                String strEdit6 = edit6.getText().toString();
                // 디뽈트값
                lists.add(new CardItem2(strEdit1, strEdit2, strEdit3, strEdit4, strEdit5, strEdit6));
                setDB(strEdit1, strEdit2, strEdit3, strEdit4, strEdit5, strEdit6);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });

            dialog.show();
        });
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

    // DB에 저장하는 메서드
    public void setDB(String str1, String str2, String str3, String str4, String str5, String str6) {
        sql = db.getWritableDatabase();
        sql.execSQL(String.format("INSERT INTO tb_treadmill VALUES(null, '%s', '%s', '%s', '%s', '%s', '%s')", str1, str2, str3, str4, str5, str6));
        sql.close();
    }
}
