package com.brioal.guibu20.util;

import android.content.Context;

/**
 * Created by Null on 2016/6/14.
 */
public class Constants {
    private static DataLoader mDataLoader  ;

    public static synchronized DataLoader getDataLoader(Context context) {
        if (mDataLoader == null) {
            mDataLoader = new DataLoader(context);
        }

        return mDataLoader;
    }

}
