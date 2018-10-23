package com.example.elab_yang.egometer.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TREADMILL_DB extends SQLiteOpenHelper {
    public TREADMILL_DB(Context context) {
        super(context, "treadmill", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_treadmill(_id Integer primary key autoincrement, user_code text, date text, time text, distance text, speed text, bpm text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tb_treadmill");
        onCreate(db);
    }
}