package com.brioal.guibu20.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.brioal.brioallib.base.BaseFragment;
import com.brioal.brioallib.util.DateUtil;
import com.brioal.guibu20.R;
import com.brioal.guibu20.activity.RunningDetailActivity;
import com.brioal.guibu20.activity.TimeCountActivity;
import com.brioal.guibu20.entity.PointEntity;
import com.brioal.guibu20.entity.RunningRecordEntity;
import com.brioal.guibu20.service.LocationService;
import com.brioal.guibu20.util.Constants;
import com.socks.library.KLog;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Brioal on 2016/5/11.
 */
public class RunningFragment extends BaseFragment implements View.OnClickListener, LocationSource,
        AMapLocationListener, RadioGroup.OnCheckedChangeListener {
    private static RunningFragment mFragment;

    @Bind(R.id.fra_running_map)
    MapView mMapView;

    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @Bind(R.id.fra_running_btnPasuse)
    Button mBtnPause;
    @Bind(R.id.fra_running_btnStart)
    RadioButton mBtnStart;
    @Bind(R.id.fra_running_btnStop)
    Button mBtnStop;

    @Bind(R.id.fra_running_altitude)
    TextView mTvAltitude;
    @Bind(R.id.fra_running_calorie)
    TextView mTvCalorie;
    @Bind(R.id.fra_running_speed)
    TextView mTvSpeed;
    @Bind(R.id.fra_running_kmcount)
    TextView mTvKmTime;
    @Bind(R.id.fra_main_distance)
    TextView mTvDistance;
    @Bind(R.id.fra_main_time)
    TextView mTvTime;
    private int[] marks = new int[]{
            R.mipmap.ic_mark1,
            R.mipmap.ic_mark2,
            R.mipmap.ic_mark3,
            R.mipmap.ic_mark4,
            R.mipmap.ic_mark5,
            R.mipmap.ic_mark6,
            R.mipmap.ic_mark7,
            R.mipmap.ic_mark8,
            R.mipmap.ic_mark9,
            R.mipmap.ic_mark10
    };

    private double mDistanceCount = 0; //距离总和
    private long mTimeCount = 0; //时间总和
    private Random mRandom; //产生随机颜色
    private int mColor; //绘制当前线段的颜色
    private double mSpeed;
    private long mKmTime; //当前千米耗时
    private long mStartTime; //
    private long mEndTime;
    DecimalFormat df = new DecimalFormat("##0.00");
    private boolean isAdjust = false;
    private long mTime = 0;
    private Timer mTimer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                showDataOnView();
                mTime = mTimeCount;
            } else if (msg.what == 1) {
                startRunning();
            } else if (msg.what == 2) { //棘突成功,跳转界面
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                aMap.clear();
            } else if (msg.what == 3) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                showNoticeDialog("错误", "数据获取失败,请重试");
            } else if (msg.what == 4) {
                mTime += 1000;
                mTvTime.setText(DateUtil.formatTimeCount(mTime));
            }

        }
    };

    public RunningFragment() {
    }

    public static RunningFragment newInstance() {
        if (mFragment == null) {
            mFragment = new RunningFragment();
        }
        return mFragment;
    }

    @Override
    public void initData() {
        super.initData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            init();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fra_running, container, false);
        mapView = (MapView) mRootView.findViewById(R.id.fra_running_map);
        ButterKnife.bind(this, mRootView);
        mMapView.onCreate(savedInstanceState);
        init();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
            setUpMap();
        }
    }

    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_current));// 设置小蓝点的图标
        myLocationStyle.strokeWidth(0.01f);
        myLocationStyle.strokeColor(Color.argb(225, 0, 0, 0));
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationStyle(myLocationStyle);
    }


    @Override
    public void initView() {
        super.initView();
        mBtnStart.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mRandom = new Random(); //产生随机颜色
        changeColor(); //绘制当前线段的颜色
    }

    public void changeColor() {
        int r = mRandom.nextInt(255);
        int g = mRandom.nextInt(255);
        int b = mRandom.nextInt(255);
        mColor = Color.rgb(r, g, b);
    }

    @Override
    public void loadDataLocal() {
        super.loadDataLocal();
    }

    @Override
    public void loadDataNet() {
        super.loadDataNet();
    }

    @Override
    public void setView() {
        super.setView();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fra_running_btnStart:
                startRunning();
                break;
            case R.id.fra_running_btnPasuse:
                pauseRunning();
                break;
            case R.id.fra_running_btnStop:
                stopRunning();
                break;
        }
    }

    Intent mIntent = new Intent("com.brioal.LocationService");

    //开始运动
    public void startRunning() {
        if (isAdjust) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(4);
                }
            }, 100, 1000);
            hideBtn(mBtnStart); //按钮显示
            showBtn(mBtnPause, mBtnStop);
            //开始运动
            mIntent.putExtra("Type", 3);
            mContext.sendBroadcast(mIntent);
        } else {
            //启动倒计时
            startActivityForResult(new Intent(mContext, TimeCountActivity.class), 0);
            //启动Service
            Intent intent = new Intent(getActivity(), LocationService.class);
            getActivity().startService(intent);
            //注册广播
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.CART_BROADCAST");
            BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    KLog.i("onReceive");
                    //拿到进度，更新UI
                    Bundle bundle = intent.getBundleExtra("Location");
                    if (bundle != null) {
                        mTimeCount = bundle.getLong("TimeCount");
                        mDistanceCount = bundle.getDouble("DistanceCount");
                        mSpeed = mDistanceCount*60*60*1000/mTimeCount;
                        mKmTime = (long) (mTimeCount*1000/mDistanceCount);
                        mCalorie = bundle.getDouble("Calorie");
                        mAltitude = bundle.getDouble("Altitude");
                        mNewList = (List<PointEntity>) bundle.getSerializable("List");
                        mBounds = bundle.getParcelable("Bound");
                        handler.sendEmptyMessage(0);
                    }

                }
            };
            broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
        }
    }

    //暂停运动
    public void pauseRunning() {
        mIntent.putExtra("Type", 1);
        mContext.sendBroadcast(mIntent);
        if (mTimer != null) {
            mTimer.cancel();
        }
        hideBtn(mBtnPause, mBtnStop);
        showBtn(mBtnStart);
        mBtnStart.setText("继续");
    }

    //停止运动是重置数据与地图显示
    public void resert() {
        mTvTime.setText("00:00:00");
        mTvAltitude.setText("0");
        mTvCalorie.setText("0.00");
        mTvDistance.setText("00.00");
        mTvSpeed.setText("00.00");
        mTvKmTime.setText("00:00:00");
        mBtnStart.setText("开始");
        isAdjust = false;
        hideBtn(mBtnPause, mBtnStop);
        showBtn(mBtnStart);
    }

    //停止运动
    public void stopRunning() {
        addPoint(mCurrentPoint, R.mipmap.ic_end);
        mIntent.putExtra("Type", 2);
        mContext.sendBroadcast(mIntent);
        if (mTimer != null) {
            mTimer.cancel();
        }
        resert();
//        if (mDistanceCount < 50) { //如果小于100米,则不计入跑步
//            ToastUtils.showToast(mContext, "距离不超过50米,不算入跑步");
//            return;
//        }
        mEndTime = System.currentTimeMillis();
        showProgressDialog("请稍等", "正在获取数据中...");
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
                                  @Override
                                  public void onMapScreenShot(Bitmap bitmap) {
                                      try {
                                          // 保存在SD卡根目录下，图片为png格式。
                                          String path = Environment.getExternalStorageDirectory() + "/KuiBuPic/" + mEndTime + ".png";
                                          FileOutputStream fos = new FileOutputStream(path);
                                          boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                          try {
                                              fos.flush();
                                          } catch (IOException e) {
                                              e.printStackTrace();
                                          }
                                          try {
                                              fos.close();
                                          } catch (IOException e) {
                                              e.printStackTrace();
                                          }
                                          if (b) {
                                              handler.sendEmptyMessage(2);//截图成功,跳转界面
                                              RunningRecordEntity entity = new RunningRecordEntity(mTimeCount, mDistanceCount, mDistanceCount*60*60*1000/mTimeCount, (long) (mTimeCount*1000/mDistanceCount), mCalorie, path, mStartTime, mEndTime);
                                              Constants.getDataLoader(mContext).addRunningDataLocal(entity);
                                              RunningDetailActivity.interRunningDetail(mContext, entity.getEndTime());
                                          } else {
                                              handler.sendEmptyMessage(3);//截图失败,提醒重试
                                          }
                                      } catch (FileNotFoundException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              }

        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            isAdjust = true;
            mStartTime = System.currentTimeMillis();
            //开始运动
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(4);

                }
            }, 100, 1000);
            //通知Service开始运动
            mIntent.putExtra("Type", 0);
            mContext.sendBroadcast(mIntent);
            hideBtn(mBtnStart); //按钮显示
            showBtn(mBtnPause, mBtnStop);
        }
    }

    private double mCalorie = 0; //卡路里消耗值
    private double mAltitude = 0; //海拔值
    private List<PointEntity> mOldList = new ArrayList<>();
    private List<PointEntity> mNewList = null;

    private LatLng mCurrentPoint ;
    public void showDataOnView() { //把数据更新到界面上
        mTvTime.setText(DateUtil.formatTimeCount(mTimeCount)); //耗时
        mTvDistance.setText(df.format(mDistanceCount / 1000)); //距离
        mTvSpeed.setText(df.format(mSpeed / 1000)); //速度
        mTvKmTime.setText(DateUtil.formatTimeCount(mKmTime)); //配速
        mTvAltitude.setText(df.format(mAltitude)); //海拔
        mTvCalorie.setText(df.format(mCalorie)); //卡路里
        //划线
        int flag = 0;
        if (mOldList.size() == 0) {
            flag = 0;
        } else {
            flag = mOldList.size();
        }
        LatLng[] points = new LatLng[mNewList.size() - flag];
        for (int i = flag; i < mNewList.size(); i++) {
            points[i] = mNewList.get(i).getLatLng();
            int type = mNewList.get(i).getType();
            if (i == 0 && type == 0) {
                addPoint(points[i], R.mipmap.ic_start); //添加起点
            } else {
                if (type == -1) {
                    addPoint(points[i], R.mipmap.ic_end); //添加终点
                } else if (type != 0) {
                    addPoint(points[i], marks[mNewList.get(i).getType() % marks.length]); //添加千米标示点
                }
            }
        }
        if (mOldList.size() != mNewList.size()) {
            aMap.addPolyline(new PolylineOptions().add(points).color(mColor).width(5));//划线
            for (int i = 0; i < flag; i++) {
                mOldList.add(mNewList.get(i));
            }
        }
        mCurrentPoint = mNewList.get(mNewList.size() - 1).getLatLng();
        //调整边界
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBounds, 10));
    }

    private LatLngBounds mBounds;


    //显示按钮
    public void showBtn(View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(View.VISIBLE);
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.trans_in_down);
        animation.setDuration(1000);

        for (int i = 0; i < views.length; i++) {
            views[i].startAnimation(animation);
        }
    }

    //隐藏按钮
    public void hideBtn(final View... views) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.trans_out_down);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (int i = 0; i < views.length; i++) {
                    views[i].setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        for (int i = 0; i < views.length; i++) {
            views[i].startAnimation(animation);
        }
    }


    //添加制定位添加点标记点
    public Marker addPoint(LatLng latLng, int resID) {
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(resID)));
        return marker;
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (mListener != null && location != null) {
            if (location != null
                    && location.getErrorCode() == 0) {
                mListener.onLocationChanged(location);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + location.getErrorCode() + ": " + location.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }


    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }

    }
}
