package com.brioal.guibu20.entity;

import java.io.Serializable;

/**
 * 存储运动事件的实体类
 * Created by Brioal on 2016/5/11.
 */
public class RunningRecordEntity implements Serializable , Comparable{
    private long mTimeCount; //跑步耗时
    private double mDistanceCount; //总距离
    private double mSpeed;//平均每小时距离
    private long mKmTime; //平均每千米耗时
    private double mCalorie; //卡路里消耗值
    private String mPicPath; //地图图片的路径
    private long mStartTime; //开始时间
    private long mEndTime;//终止时间


    public RunningRecordEntity(long timeCount, double distanceCount, double speed, long kmTime, double calorie, String picPath, long startTime, long endTime) {
        mTimeCount = timeCount;
        mDistanceCount = distanceCount;
        mSpeed = speed;
        mKmTime = kmTime;
        mCalorie = calorie;
        mPicPath = picPath;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public long getTimeCount() {
        return mTimeCount;
    }

    public void setTimeCount(long timeCount) {
        mTimeCount = timeCount;
    }

    public double getDistanceCount() {
        return mDistanceCount;
    }

    public void setDistanceCount(double distanceCount) {
        mDistanceCount = distanceCount;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    public long getKmTime() {
        return mKmTime;
    }

    public void setKmTime(long kmTime) {
        mKmTime = kmTime;
    }

    public double getCalorie() {
        return mCalorie;
    }

    public void setCalorie(double calorie) {
        mCalorie = calorie;
    }

    public String getPicPath() {
        return mPicPath;
    }

    public void setPicPath(String picPath) {
        mPicPath = picPath;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long endTime) {
        mEndTime = endTime;
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
