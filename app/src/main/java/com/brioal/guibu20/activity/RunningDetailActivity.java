package com.brioal.guibu20.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brioal.brioallib.base.BaseActivity;
import com.brioal.brioallib.util.DateUtil;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ToastUtils;
import com.brioal.guibu20.R;
import com.brioal.guibu20.entity.RunningRecordEntity;
import com.brioal.guibu20.util.Constants;
import com.brioal.guibu20.util.LogFileUtil;
import com.socks.library.KLog;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 显示跑步详情的Activity
 * 传入的是RunningRecordEntity以及KmTimeCountEntity的list，以及一张地图的截图
 */
public class RunningDetailActivity extends BaseActivity {
    @Bind(R.id.fra_running_detail_distance)
    TextView mTvDistance;
    @Bind(R.id.fra_running_detail_time)
    TextView mTvTime;
    @Bind(R.id.fra_running_detail_kmCount)
    TextView mTvKmCount;
    @Bind(R.id.fra_running_detail_speed)
    TextView mTvSpeed;
    @Bind(R.id.fra_running_detail_cal)
    TextView mTvCal;
    @Bind(R.id.fra_running_detail_startTime)
    TextView mTvStartTime;
    @Bind(R.id.fra_running_detail_endTime)
    TextView mTvEndTime;
    @Bind(R.id.fra_running_detail_mapPic)
    ImageView mMapPic;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.act_running_detail)
    ScrollView mActRunningDetail;

    DecimalFormat df = new DecimalFormat("##0.00");
    private boolean isSave = true;
    private long mEndTime = 0;
    private RunningRecordEntity mEntity;

    @Override
    public void initData() {
        super.initData();
        try {
            mEndTime = getIntent().getLongExtra("EndTime", 0);
            mEntity = Constants.getDataLoader(mContext).getRunningDataLocal(mEndTime);
        } catch (Exception e) {
            KLog.e("getIntent", e.toString());
            mEntity = new RunningRecordEntity(0, 0, 0, 0, 0, "", 0, 0);
            ToastUtils.showToast(mContext, "数据获取失败");
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.act_running_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void initBar() {
        super.initBar();
        mToolbar.setTitle("跑步详情");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initTheme() {
        super.initTheme();
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void loadDataLocal() {
        super.loadDataLocal();
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void setView() {
        super.setView();
        mTvDistance.setText(df.format(mEntity.getDistanceCount() / 1000));
        mTvTime.setText(DateUtil.formatTimeCount(mEntity.getTimeCount()));
        mTvSpeed.setText("时速:" + df.format(mEntity.getSpeed() / 1000));
        mTvKmCount.setText("配速:" + DateUtil.formatTimeCount(mEntity.getKmTime()));
        mTvCal.setText("卡里路:" + df.format(mEntity.getCalorie()));
        mTvStartTime.setText(DateUtil.formatTimeString(mEntity.getStartTime()));
        mTvEndTime.setText(DateUtil.formatTimeString(mEntity.getEndTime()));
        mMapPic.setImageBitmap(BitmapFactory.decodeFile(mEntity.getPicPath()));
    }


    public static void interRunningDetail(Activity activity, long endTime) {
        try {
            Intent intent = new Intent(activity, RunningDetailActivity.class);
            intent.putExtra("EndTime", endTime);
            activity.startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(activity, "出现错误，请重试");
            LogFileUtil.file(e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_running_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //默认保存数据:
                setResult(RESULT_OK);
                finish();
            case R.id.action_delete_record: //不保存数据
                isSave = false;
                //删除数据
                Constants.getDataLoader(mContext).deleteRunningRecoed(mEntity.getEndTime());
                setResult(RESULT_CANCELED);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
