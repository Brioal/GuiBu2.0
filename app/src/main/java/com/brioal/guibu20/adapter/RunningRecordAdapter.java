package com.brioal.guibu20.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brioal.brioallib.util.DateUtil;
import com.brioal.guibu20.R;
import com.brioal.guibu20.activity.RunningDetailActivity;
import com.brioal.guibu20.entity.RunningRecordEntity;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brioal on 2016/5/11.
 */
public class RunningRecordAdapter extends RecyclerView.Adapter<RunningRecordAdapter.RunningViewHolder> {


    private Context mContext;
    private List<RunningRecordEntity> mList;

    public RunningRecordAdapter(Context mContext, List<RunningRecordEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public RunningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_runningrecord, parent, false);
        return new RunningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RunningViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("##0.00");
        final RunningRecordEntity model = mList.get(position);
        holder.mDistance.setText(df.format(model.getDistanceCount()/1000));
        holder.mTime.setText(DateUtil.formatTimeCount(model.getTimeCount()));
        holder.mSpeedhour.setText("时速:" + df.format(model.getSpeed()/1000));
        holder.mKmtime.setText("配速" + DateUtil.formatTimeCount(model.getKmTime()));
        holder.mKaluli.setText("卡路里:" + df.format(model.getCalorie()));
        holder.mStarttime.setText(DateUtil.formatTimeString(model.getStartTime()));
        holder.mEndtime.setText(DateUtil.formatTimeString(model.getEndTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunningDetailActivity.interRunningDetail((Activity) mContext, model.getEndTime());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RunningViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_runningrecord_distance)
        TextView mDistance;
        @Bind(R.id.item_runningrecord_time)
        TextView mTime;
        @Bind(R.id.item_runningrecord_speedhour)
        TextView mSpeedhour;
        @Bind(R.id.item_runningrecord_kmtime)
        TextView mKmtime;
        @Bind(R.id.item_runningrecord_kaluli)
        TextView mKaluli;
        @Bind(R.id.item_runningrecord_starttime)
        TextView mStarttime;
        @Bind(R.id.item_runningrecord_endtime)
        TextView mEndtime;

        View itemView;

        public RunningViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
