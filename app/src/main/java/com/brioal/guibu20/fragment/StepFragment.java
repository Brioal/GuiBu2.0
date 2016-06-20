package com.brioal.guibu20.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.brioal.brioallib.base.BaseFragment;
import com.brioal.guibu20.R;
import com.brioal.guibu20.util.Constants;
import com.brioal.guibu20.view.StepCountView;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Brioal on 2016/6/16.
 */
public class StepFragment extends BaseFragment {
    static {
        TAG = "StepFraInfo";
    }

    private static StepFragment mFragment;
    StepCountView mStepView;

    private int[] sevenStepCounts; //最近七天的步数
    private String[] sevenDate; //最近七天的日期
    private int stepCount; //当前总步数
    private String mCurrentTime; //当前时间

    public static StepFragment newInstance() {
        if (mFragment == null) {
            mFragment = new StepFragment();
        }
        return mFragment;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        mRootView = inflater.inflate(R.layout.fra_step, container, false);
        mStepView = (StepCountView) mRootView.findViewById(R.id.fra_step_stepview);
        //注册广播
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.brioal.service.StepService");
        BroadcastReceiver stepReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                KLog.i("接受到来自StepReceiver的广播");
                stepCount = intent.getIntExtra("StepCount", 0);
                mHandler.sendEmptyMessage(0);
            }
        };
        broadcastManager.registerReceiver(stepReceiver, filter);
        loadData();
    }


    private Timer timer;

    public void loadData() {
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stepCount = Constants.getDataLoader(mContext).getTodayStep();
                mCurrentTime = format.format(System.currentTimeMillis());
                mHandler.sendEmptyMessage(0);
            }
        }, 100, 5000);
    }


    @Override
    public void setView() {
        super.setView();
        mStepView.setData(stepCount, mCurrentTime);
    }

    @Override
    public void saveDataLocal() {
        super.saveDataLocal();
    }
}
