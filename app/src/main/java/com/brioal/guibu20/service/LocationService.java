package com.brioal.guibu20.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.brioal.guibu20.entity.PointEntity;
import com.brioal.guibu20.util.LogFileUtil;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于后台定位的Service
 * Created by Brioal on 2016/6/19.
 */

public class LocationService extends Service implements AMapLocationListener {
    private final static int GRAY_SERVICE_ID = 1001;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double mSpeed = 0;
    private LatLng mLastLat = null;
    private long mKmTime = 0; //千米耗时
    private double mCalorie = 0;//卡路里的消耗
    private double mAltitude = 0;//海拔值
    private boolean isRunning = false;
    private ArrayList<PointEntity> mList = new ArrayList<>(); //存储坐标内容的数据
    private LatLng mLeftBound = null;
    private LatLng mRightBound = null;
    private LatLngBounds bounds = null;
    private double mDistanceCount = 0;//总距离计数
    private long mTimeCount; //总时间计数
    private int mCurrentKm = 1; //当前的千米数量
    private Timer mTimer;
    private Intent intent = new Intent("android.intent.action.CART_BROADCAST");

    private AlarmManager am;
    private PendingIntent pi;

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.i("LocationService Create");
        initLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, LocationInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static class LocationInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //初始化定位
    private void initLocation() {
        Intent i = new Intent();
        i.setAction("android.intent.action.CART_BROADCAST");
        pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        initOptions();//初始化配置
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        long time = SystemClock.currentThreadTimeMillis();
//        am.cancel(pi);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, 5000, pi);
        //注册用于控制开启的receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.brioal.LocationService");
        BroadcastReceiver runningReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int type = intent.getIntExtra("Type", -1);
                switch (type) {
                    case 0:
                        startRunning();
                        break;
                    case 1:
                        pauseRunning();
                        break;
                    case 2:
                        stopRunning();
                        break;
                    case 3:
                        resumeRunning();
                        break;

                }
            }
        };
        registerReceiver(runningReceiver, filter);
    }

    //初始化定位配置
    private void initOptions() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
    }

    //起点的判断与添加
    public void addStartPoint(AMapLocation location) {
        if (mList.size() == 0) { //添加起点
            mList.add(new PointEntity(new LatLng(location.getLatitude(), location.getLongitude()), System.currentTimeMillis(), 0));
        }
    }


    //调整边界值
    public void changeBounds(LatLng latLng) {
        if (mLeftBound == null || mRightBound == null) {
            mRightBound = mLeftBound = latLng;
        }
        //经度
        if (latLng.longitude < mLeftBound.longitude) {
            double latitude = mLeftBound.latitude;
            mLeftBound = new LatLng(latitude, latLng.longitude);
        }
        //经度
        if (latLng.longitude > mRightBound.longitude) {
            double latitude = mRightBound.latitude;
            mRightBound = new LatLng(latitude, latLng.longitude);
        }
        //纬度
        if (latLng.latitude > mLeftBound.latitude) {
            double longitude = mLeftBound.longitude;
            mLeftBound = new LatLng(latLng.latitude, longitude);
        }
        //纬度
        if (latLng.latitude < mRightBound.latitude) {
            double longitude = mRightBound.longitude;
            mRightBound = new LatLng(latLng.latitude, longitude);
        }
        bounds = new LatLngBounds.Builder()
                .include(mLeftBound).include(mRightBound).build();
    }


    //开始运动
    public void startRunning() {
        mDistanceCount = 0;
        mTimeCount = 0;
        mAltitude = 0;
        mSpeed = 0;
        mKmTime = 0;
        mCalorie = 0;
        KLog.i("ResumeRunning");
        isRunning = true;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimeCount += 1000;
            }
        }, 100, 1000);
    }

    //恢复运动
    public void resumeRunning() {
        KLog.i("ResumeRunning");
        isRunning = true;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimeCount += 1000;
            }
        }, 100, 1000);
    }

    //暂停运动
    public void pauseRunning() {
        KLog.i("PauseRunning");
        isRunning = false;
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    //停止运动
    public void stopRunning() {
        KLog.i("StopRunning");
        isRunning = false;
        if (mTimer != null) {
            mTimer.cancel();
        }
        resert();
    }

    public void resert() {
        mDistanceCount = 0;
        mTimeCount = 0;
        mAltitude = 0;
        mSpeed = 0;
        mKmTime = 0;
        mCalorie = 0;
        mList.clear();
    }

    //判断是否到了千米
    public void pointsAdd(LatLng latLng) {
        int type = 0;
        if (((int) (mDistanceCount / 1000)) == mCurrentKm) { //千米点
            KLog.d("千米距离点");
            LogFileUtil.file("千米距离点");
            type = 1;
            mCurrentKm++;
        } else { //添加普通点
            type = 0;
        }
        addPoint(latLng, type);
    }

    //添加点数据到数组
    public void addPoint(LatLng latLng, int type) {
        mList.add(new PointEntity(latLng, System.currentTimeMillis(), type));
    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null ) {
            mAltitude = location.getAltitude();
            if (isRunning) { //开始运动
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                addStartPoint(location);//起点的判断与添加
                changeBounds(latLng); //边界值调整
                //添加标示点
                pointsAdd(latLng);
                //距离判断
                if (mLastLat == null) {
                    mLastLat = latLng;
                }
                KLog.i("------------------------");
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                KLog.i(format.format(System.currentTimeMillis()));
                //精度筛选
                float accrecy = location.getAccuracy();
                KLog.i("精度:" + accrecy);
                if (accrecy > 20) {
                    return;
                }
                //距离筛选
                double shortDistance = AMapUtils.calculateLineDistance(mLastLat, latLng);
                KLog.i("距离:" + shortDistance);
                KLog.i("------------------------");
                if (shortDistance < 2.5) {
                    return;
                }
                mLastLat = latLng;
                mDistanceCount += shortDistance; //距离相加
                mSpeed = location.getSpeed(); //获取速度
                if (location.getSpeed() == 0) {
                    mKmTime = 0;
                } else {
                    mKmTime = (long) (60 * 60 * 1000 / location.getSpeed() / 1000);
                }//获取千米耗时数据
                //卡路里消耗值
                mCalorie = mDistanceCount / 1000 * 50 * 1.036;
                Bundle bundle = new Bundle();
                bundle.putLong("TimeCount", mTimeCount);
                bundle.putDouble("DistanceCount", mDistanceCount);
                bundle.putDouble("Speed", mSpeed);
                bundle.putLong("KmTime", mKmTime);
                bundle.putDouble("Calorie", mCalorie);
                bundle.putDouble("Altitude", mAltitude);
                bundle.putSerializable("List", mList);
                bundle.putParcelable("Bound", bounds);
                intent.putExtra("Location", bundle);
                KLog.i("发送广播");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            }

        } else {
            KLog.e("AmapError", "location Error, ErrCode:"
                    + location.getErrorCode() + ", errInfo:"
                    + location.getErrorInfo());
        }
    }
}
