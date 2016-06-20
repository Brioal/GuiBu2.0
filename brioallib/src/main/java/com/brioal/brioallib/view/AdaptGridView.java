package com.brioal.brioallib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Brioal on 2016/6/3.
 */

public class AdaptGridView extends GridView {
    private final String TAG = "MyGridView";
    public AdaptGridView(Context context) {
        this(context, null);
    }

    public AdaptGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
