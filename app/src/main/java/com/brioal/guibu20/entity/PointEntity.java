package com.brioal.guibu20.entity;

import com.amap.api.maps2d.model.LatLng;

import java.io.Serializable;

/**存储位置点的数据实例
 * Created by Brioal on 2016/6/18.
 */

public class PointEntity implements Serializable{
    private LatLng mLatLng; //经纬度
    private long mTime; //时间
    private int mType ; //标示 0为普通点 ,数字为标示

    public PointEntity(LatLng latLng, long time , int mType) {
        mLatLng = latLng;
        mTime = time;
        this.mType = mType;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
