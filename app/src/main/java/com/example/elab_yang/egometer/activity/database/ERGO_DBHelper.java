package com.example.elab_yang.egometer.activity.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ERGO_DBHelper extends SQLiteOpenHelper {
    public ERGO_DBHelper(Context context) {
        super(context, "egometer", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_egometer(_id Integer primary key autoincrement, date text, time text, speed text, distance text, bpm text, kcal text, before_bloodsugar text, after_bloodsugar text, num text, memo text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tb_egometer");
        onCreate(db);
    }
}