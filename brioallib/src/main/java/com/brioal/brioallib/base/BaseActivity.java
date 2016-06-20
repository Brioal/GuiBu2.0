package com.brioal.brioallib.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.brioal.brioallib.interfaces.ActivityFormat;


/**
 * Activity的基类
 * Created by mm on 2016/6/4.
 */

public class BaseActivity extends AppCompatActivity implements ActivityFormat {
    protected static String TAG = "BaseActivity";
    protected Activity mContext;
    protected Runnable mThreadLocal = new Runnable() {
        @Override
        public void run() {
            loadDataLocal();
        }
    };
    protected Runnable mThreadNet = new Runnable() {
        @Override
        public void run() {
            loadDataNet();
        }
    };
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setView();
            Log.i(TAG, "handleMessage: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initData();
        initView(savedInstanceState);
        initBar();
        new Thread(mThreadLocal).start();
        new Thread(mThreadNet).start();
        Log.i(TAG, "onCreate:. ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        initTheme();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override

    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        saveDataLocal();
    }

    @Override
    public void initData() {
        Log.i(TAG, "initData: ");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Log.i(TAG, "initView: ");
    }

    @Override
    public void initBar() {
        Log.i(TAG, "initBar: ");
    }

    @Override
    public void initTheme() {
        Log.i(TAG, "initTheme: ");
    }

    @Override
    public void loadDataLocal() {
        Log.i(TAG, "loadDataLocal: ");
    }

    @Override
    public void loadDataNet() {
        Log.i(TAG, "loadDataNet: ");
    }

    @Override
    public void setView() {
        Log.i(TAG, "setView: ");
    }

    @Override
    public void saveDataLocal() {
        Log.i(TAG, "saveDataLocal: ");
    }

}
