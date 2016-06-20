package com.brioal.guibu20.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.brioal.brioallib.base.BaseActivity;
import com.brioal.guibu20.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimeCountActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.act_time_count_tv_time)
    TextView mTvTime;
    @Bind(R.id.act_time_count_tv_start)
    TextView mTvStart;
    @Bind(R.id.act_time_count_btn_add)
    Button mBtnAdd;

    private long mTime = 10000; //10秒
    private Timer mTimer; //计时器

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.act_time_count);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mTvStart.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        startCount();
    }

    @Override
    public void setView() {
        super.setView();
        if (mTime == 0) { //结束
            stop();
        } else {
            mTvTime.setText(mTime / 1000 + "s");
        }
    }

    public void startCount() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTime -= 1000;
                mHandler.sendEmptyMessage(0);
            }
        }, 100, 1000);
    }

    //停止Activity
    public void stop() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.trans_in_down, R.anim.trans_out_down);
    }

    //添加5秒
    public void addTime() {
        mTime += 5000;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_time_count_tv_start:
                stop();
                break;
            case R.id.act_time_count_btn_add:
                addTime();
                break;
        }
    }
}
