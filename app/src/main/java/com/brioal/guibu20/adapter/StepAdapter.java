package com.brioal.guibu20.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brioal.brioallib.util.DateUtil;
import com.brioal.guibu20.R;
import com.brioal.guibu20.entity.StepEntity;
import com.brioal.guibu20.util.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 步数记录展示列表适配器
 * Created by Null on 2016/6/14.
 */
public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private Context mContext;
    private List<StepEntity> mList;

    public StepAdapter(Context mContext, List<StepEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position) {
        final StepEntity entity = mList.get(position);
        holder.mStepCount.setText(entity.getmStepCount() + "步");
        long time = entity.getmStepTime();
        holder.mStepTime.setText(DateUtil.formatTimeString(time));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("请确认").setMessage("是否删除当前记录").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mList.remove(position);
                        notifyDataSetChanged();
                        Constants.getDataLoader(mContext).deleteStepRecord(entity.getmStepTime());
                    }
                }).setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_step_count)
        TextView mStepCount;
        @Bind(R.id.item_step_time)
        TextView mStepTime;
        View itemView;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
