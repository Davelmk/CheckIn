package com.dave.checkin.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CheckinDBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static  final String sql="create table checkin(id text primary key, title text, position text, owner text, num text, description text, createdDate text)";

    public CheckinDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        Log.d("DataBase","创建签到sql表");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
