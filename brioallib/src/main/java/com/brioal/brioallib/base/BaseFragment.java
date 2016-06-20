package com.brioal.brioallib.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brioal.brioallib.interfaces.FragmentFormat;


/**
 * Fragment基类
 * Created by mm on 2016/6/4.
 */

public class BaseFragment extends Fragment implements FragmentFormat {
    protected Activity mContext;
    protected View mRootView;
    protected LayoutInflater inflater;
    protected ViewGroup container;
    protected Bundle saveInstanceState;
    protected static String TAG = "BaseFragment";
    protected Runnable mRunnableLocal=new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: 加载本地数据");
            loadDataLocal();
        }
    };
    protected Runnable mRunnableNet = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: 加载网络数据");
            loadDataNet();      //加载数据
        }
    };

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: 数据显示到布局");
            setView(); //数据显示到布局
        }
    };

    protected ProgressDialog mProgressDialog ;
    protected AlertDialog mNoticeDialog ;

    //显示进度条
    public void showProgressDialog(String title, String message) {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    //显示提醒框
    public void showNoticeDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        mNoticeDialog = builder.setTitle(title).setMessage(message).setCancelable(false).create();
        mNoticeDialog.show();
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        this.inflater = inflater;
        this.container = container;
        this.saveInstanceState = savedInstanceState;
        initData();
        initView();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        new Thread(mRunnableLocal).start();
        new Thread(mRunnableNet).start();
    }

    @Override
    public void initData() {
        Log.i(TAG, "initData: ");
    }

    @Override
    public void initView() {
        Log.i(TAG, "initView: ");
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
