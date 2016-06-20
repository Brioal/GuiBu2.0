package com.brioal.brioallib.view.bottomtablayout.adapter;

import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;

/**
 * Created by Brioal on 2016/5/9.
 */
public abstract class BottomBaseAdapter {
    private DataSetObserver mObserver ;

    public void registerObserver(DataSetObserver mObserver) {
        this.mObserver = mObserver;
    }

    public void unRegisterObserver() {
        this.mObserver = null;
    }

    public void notifyDataChanged() {
        if (mObserver != null) {
            mObserver.onChanged();
        }
    }

    public void notifyDataInvalidate() {
        if (mObserver != null) {
            mObserver.onInvalidated();
        }
    }

    public abstract int getItemCount(); //获取item总数

    public abstract Drawable getDrawable(int position); //获取图像

    public abstract CharSequence getText(int position); //获取蚊子
}
