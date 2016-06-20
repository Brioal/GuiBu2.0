package com.brioal.brioallib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 * Created by Brioal on 2016/5/12.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final String table_Running = "create table Running ( _id integer  primary key , mTimeCount long   , mDistanceCount double, mSpeed double , mKmTime long   , mCalories double , mapPic , mStartTime long , mEndTime long )"; //跑步事件数据表
    private final String table_Step = "create table Step ( _id integer primary key autoincrement , mStepTime long , mStepCount integer ) "; //计步器的数据表

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_Running);
        db.execSQL(table_Step);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
