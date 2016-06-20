package com.brioal.guibu20.entity;

import java.util.Comparator;

/**
 * Created by Null on 2016/6/14.
 */
public class StepEntity implements Comparator{
    private long mStepTime ;//记录的时间
    private int mStepCount ; //步数记录

    public StepEntity(long mStepTime, int mStepCount) {
        this.mStepTime = mStepTime;
        this.mStepCount = mStepCount;
    }

    public long getmStepTime() {
        return mStepTime;
    }

    public void setmStepTime(long mStepTime) {
        this.mStepTime = mStepTime;
    }

    public int getmStepCount() {
        return mStepCount;
    }

    public void setmStepCount(int mStepCount) {
        this.mStepCount = mStepCount;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        StepEntity lh = (StepEntity) lhs;
        StepEntity rh = (StepEntity) rhs;
        return (int) (rh.getmStepTime()-lh.getmStepTime());
    }
}
