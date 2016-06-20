package com.brioal.guibu20.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brioal.brioallib.base.BaseFragment;
import com.brioal.guibu20.R;
import com.brioal.guibu20.adapter.RunningRecordAdapter;
import com.brioal.guibu20.entity.RunningRecordEntity;
import com.brioal.guibu20.util.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 跑步记录Fra
 * Created by Brioal on 2016/5/11.
 */
public class RunningRecordFragment extends BaseFragment {
    static {
        TAG = "RunningRecordInfo";
    }

    private static RunningRecordFragment mFragment;
    @Bind(R.id.fra_running_refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.fra_running_recyclerView)
    RecyclerView mRecyclerView;

    private RunningRecordAdapter mAdapter;
    private List<RunningRecordEntity> mList;

    public static RunningRecordFragment newInstance() {
        if (mFragment == null) {
            mFragment = new RunningRecordFragment();
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
        mRootView = inflater.inflate(R.layout.fra_running_record, container, false);
        ButterKnife.bind(this, mRootView);
        mRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.RED);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataLocal();
            }
        });
    }

    @Override
    public void loadDataLocal() {
        super.loadDataLocal();
        mList = Constants.getDataLoader(mContext).getRunningDataLocal();
        Collections.sort(mList,new RecordSort());
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void setView() {
        super.setView();
        if (mList.size() == 0) {
            mRefreshLayout.setVisibility(View.GONE);
        } else {
            mRefreshLayout.setVisibility(View.VISIBLE);
            mAdapter = new RunningRecordAdapter(mContext, mList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(mAdapter);
        }
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }


    class RecordSort implements Comparator<RunningRecordEntity> {


        @Override
        public int compare(RunningRecordEntity lhs, RunningRecordEntity rhs) {

            return (int) (rhs.getEndTime() - lhs.getStartTime());
        }
    }

}
