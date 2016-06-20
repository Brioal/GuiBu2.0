package com.brioal.brioallib.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司显示类.
 */
public class ToastUtils {
    public static Toast mToast;
    //显示字符串
    public static void showToast(final Context context, final String message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    //显示资源文件
    public static void showToast(final Context context, final int messageResId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, messageResId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(messageResId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
