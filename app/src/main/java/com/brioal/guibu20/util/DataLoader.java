package com.brioal.guibu20.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.brioal.brioallib.database.DBHelper;
import com.brioal.brioallib.util.DateUtil;
import com.brioal.guibu20.entity.RunningRecordEntity;
import com.brioal.guibu20.entity.StepEntity;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 数据加载器
 * Created by Null on 2016/6/14.
 */
public class DataLoader {
    private Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String TAG = "DataInfo";

    public DataLoader(Context mContext) {
        this.mContext = mContext;
        mDBHelper = getDBHelper();
        mDb = getmDb();
        preferences = mContext.getSharedPreferences("StepRecord", Context.MODE_PRIVATE);
    }

    private DBHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(mContext, "Kuibu.db3", null, 1);
        }
        return mDBHelper;
    }

    private SQLiteDatabase getmDb() {
        if (mDb == null) {
            mDb = getDBHelper().getReadableDatabase();
        }
        return mDb;
    }

    //获取本地的步数记录
    public List<StepEntity> getStepLocal() {
        List<StepEntity> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getmDb().rawQuery("select * from Step", null);
            while (cursor.moveToNext()) {
                StepEntity entity = new StepEntity(cursor.getLong(1), cursor.getInt(2));
                list.add(entity);
            }
            Log.i(TAG, "获取本地的步数记录成功" + list.size());
        } catch (Exception e) {
            Log.i(TAG, "获取本地的步数记录失败" + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        Collections.sort(list, new StepTimeCompare());
        return list;
    }

    //删除步数记录
    public void deleteStepRecord(long l) {
        try {
            getmDb().execSQL("delete from Step where mStepTime = '" + l + "'");
            KLog.i("删除数据成功");
        } catch (Exception e) {
            KLog.i("删除数据失败" + e.toString());
            e.printStackTrace();

        }
    }

    //添加步数记录到本地
    public void addStepLocal(StepEntity entity) {
        try {
            getmDb().execSQL("insert into Step values ( null , ? , ? )", new Object[]{
                    entity.getmStepTime(),
                    entity.getmStepCount()
            });
        } catch (Exception e) {
            Log.i(TAG, "添加步数到本地失败" + e.toString());
            e.printStackTrace();
        }
    }

    //从本地读取跑步记录
    public List<RunningRecordEntity> getRunningDataLocal() {
        List<RunningRecordEntity> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getmDb().rawQuery("select * from Running", null);
            while (cursor.moveToNext()) {
                RunningRecordEntity entity = new RunningRecordEntity(cursor.getLong(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getLong(4), cursor.getDouble(5), cursor.getString(6), cursor.getLong(7), cursor.getLong(8));
                list.add(entity);
            }
            Log.i(TAG, "加载本地的跑步记录成功" + list.size());
        } catch (Exception e) {
            Log.i(TAG, "加载本地的跑步记录失败" + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    //添加跑步记录到本地
    public void addRunningDataLocal(RunningRecordEntity entity) {
        try {

            getmDb().execSQL("insert  into Running values (null , ? , ? , ? , ? , ? , ? , ? , ? )",
                    new Object[]{
                            entity.getTimeCount(),
                            entity.getDistanceCount(),
                            entity.getSpeed(),
                            entity.getKmTime(),
                            entity.getCalorie(),
                            entity.getPicPath(),
                            entity.getStartTime(),
                            entity.getEndTime()
                    });
            Log.i(TAG, "保存跑步数据到本地成功");
        } catch (Exception e) {
            Log.i(TAG, "保存跑步数据到本地失败" + e.toString());
            e.printStackTrace();
        }
    }


    private SharedPreferences preferences;

    //读取当前走了多少部
    public int getTodayStep() {
        //如果是同一天,则继续计数,不是同一天则重新开始计数
        long time = 0;
        int stepCount = 0;
        if (preferences == null) {
            preferences = mContext.getSharedPreferences("StepRecord", Context.MODE_PRIVATE);
        }
        time = preferences.getLong("Time", 0);
        stepCount = preferences.getInt("Step", 0);
        if (DateUtil.isSameDay(time, System.currentTimeMillis())) { //同一天
            return stepCount;
        } else {//不是同一天
            preferences.edit().putLong("Time", System.currentTimeMillis()).apply();
            preferences.edit().putLong("Step", 0).apply();
            Constants.getDataLoader(mContext).addStepLocal(new StepEntity(time, stepCount)); //保存昨天的数据
            return 0;
        }
    }

    //存储当前的步数
    public void saveStepCount(int count) {
        if (preferences == null) {
            preferences = mContext.getSharedPreferences("StepRecord", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Step", count);
        editor.putLong("Time", System.currentTimeMillis());
        editor.apply();
    }

    //删除指定记录
    public void deleteRunningRecoed(long l) {
        try {
            getmDb().execSQL("delete from Running where mEndTime = '" + l + "'");
            KLog.d("删除跑步数据成功");
        } catch (Exception e) {
            KLog.e("删除跑步数据失败" + e.toString());
            e.printStackTrace();
            KLog.e(e.toString());
        }
    }

    public RunningRecordEntity getRunningDataLocal(long endTime) {
        Cursor cursor = null;
        RunningRecordEntity entity = null;
        try {
            cursor = getmDb().rawQuery("select * from Running where mEndTime = '" + endTime + "'", null);
            while (cursor.moveToNext()) {
                entity = new RunningRecordEntity(cursor.getLong(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getLong(4), cursor.getDouble(5), cursor.getString(6), cursor.getLong(7), cursor.getLong(8));
            }
            Log.i(TAG, "加载本地的跑步记录成功");
        } catch (Exception e) {
            Log.i(TAG, "加载本地的跑步记录失败" + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return entity;
    }


    //步数比较器
    class StepTimeCompare implements Comparator<StepEntity> {
        @Override
        public int compare(StepEntity lhs, StepEntity rhs) {
            return (int) (rhs.getmStepTime() - lhs.getmStepTime());
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }


}
