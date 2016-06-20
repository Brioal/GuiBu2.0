package com.brioal.guibu20.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brioal.brioallib.base.BaseFragment;
import com.brioal.guibu20.R;
import com.brioal.guibu20.adapter.StepAdapter;
import com.brioal.guibu20.entity.StepEntity;
import com.brioal.guibu20.util.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 步数记录数据展示
 * Created by Null on 2016/6/14.
 */
public class StepRecordFragment extends BaseFragment {
    static {
        TAG = "StepInfo";
    }

    private static StepRecordFragment mFragment;
    public static synchronized StepRecordFragment newInstance() {
        if (mFragment == null) {
            mFragment = new StepRecordFragment();
        }
        return mFragment;
    }

    @Bind(R.id.fra_step_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.fra_step_refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private List<StepEntity> mList;
    private StepAdapter mAdapter;


    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        mRootView = inflater.inflate(R.layout.fra_step_record, container, false);
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
        mList = Constants.getDataLoader(mContext).getStepLocal();
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void setView() {
        super.setView();

        if (mList.size() == 0) {
            mRefreshLayout.setVisibility(View.GONE);
        } else {
            mRefreshLayout.setVisibility(View.VISIBLE);
            mAdapter = new StepAdapter(mContext, mList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(mAdapter);

        }
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void saveDataLocal() {
        super.saveDataLocal();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
