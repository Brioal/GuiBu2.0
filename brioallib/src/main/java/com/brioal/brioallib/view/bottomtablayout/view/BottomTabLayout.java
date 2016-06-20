package com.brioal.brioallib.view.bottomtablayout.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.brioal.brioallib.view.bottomtablayout.adapter.BottomBaseAdapter;


/**
 * Created by Brioal on 2016/5/9.
 */
public class BottomTabLayout extends LinearLayout {
    private BottomBaseAdapter mAdapter;
    private int mCurrentCheck = 0;
    private int mCheckedColor; //选中的图标颜色
    private int mNormalColor; //未选中的图标颜色
    private int mNormalTextSize = 14;
    private float mCheckedPerscent = 1.1f; //选中的内容扩大的倍数

    public interface onSelectItem {
        void onSelectedItem(int position);
    }

    private onSelectItem selectItem;

    public void setSelectItem(onSelectItem selectItem) {
        this.selectItem = selectItem;
    }

    public void setmCurrentCheck(int mCurrentCheck) {
        this.mCurrentCheck = mCurrentCheck;
        itemCheck(mCurrentCheck);
    }

    public void setmNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
    }

    public void setmCheckedColor(int mCheckedColor) {
        this.mCheckedColor = mCheckedColor;
    }

    public void setmCheckedPerscent(float mCheckedPerscent) {
        this.mCheckedPerscent = mCheckedPerscent;
    }

    public BottomTabLayout(Context context) {
        this(context, null);
    }

    public BottomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setmAdapter(BottomBaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mAdapter.registerObserver(mObserver);
        mAdapter.notifyDataChanged();
    }

    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            onInvalidated();
            if (mAdapter == null) return;
            int itemCount = mAdapter.getItemCount();
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            for (int i = 0; i < itemCount; i++) {
                addView(getRipple(i), params); //添加所有内容
            }
            super.onChanged();
        }

        @Override
        public void onInvalidated() {
            removeAllViews();
        }
    };

    private View getRipple(final int i) {
        RippleButton button = new RippleButton(getContext());
        button.setGravity(Gravity.CENTER);
        button.setTextSize(mNormalTextSize);
        button.setText(mAdapter.getText(i));
        button.setTextColor(mNormalColor);
        Drawable drawable = mAdapter.getDrawable(i);
        drawable.setBounds(0, 0, 20, 20);
        drawable.setColorFilter(new PorterDuffColorFilter(mNormalColor, PorterDuff.Mode.SRC_IN));
        button.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
                null, null);
        button.setListener(new RippleButton.onBeforeClickListener() {
            @Override
            public void onBeforeClicked(View view) {
                if (selectItem != null && i != mCurrentCheck) {
                    selectItem.onSelectedItem(i);
                }
                itemCheck(i);
            }
        });
        itemCheck(mCurrentCheck);
        return button;
    }

    //选中指定项
    private void itemCheck(int i) {
        mCurrentCheck = i; //选中当前下表
        //遍历解除其他的选中效果
        int itemCount = getChildCount();
        RippleButton button;
        Drawable drawable;
        for (int j = 0; j < itemCount; j++) {
            button = (RippleButton) getChildAt(j);
            drawable = button.getCompoundDrawables()[1]; //上方的drawable
            button.cancel();
            if (j == mCurrentCheck) { //当前选中项
                button.setTextColor(mCheckedColor);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, mNormalTextSize * mCheckedPerscent);

                drawable.setColorFilter(new PorterDuffColorFilter(mCheckedColor, PorterDuff.Mode.SRC_IN));
            } else {
                if (drawable != null) {
                    button.setTextColor(mNormalColor);
                    button.setTextSize(TypedValue.COMPLEX_UNIT_SP, mNormalTextSize);
                    drawable.setColorFilter(new PorterDuffColorFilter(mNormalColor, PorterDuff.Mode.SRC_IN));
                }
            }

        }
    }
}
