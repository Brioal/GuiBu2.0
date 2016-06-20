package com.brioal.guibu20.util;

import android.os.Environment;

import com.socks.library.KLog;

/**
 * Created by Brioal on 2016/6/17.
 */

public class LogFileUtil {
    public static void file(String s) {
        KLog.file("步数记录", Environment.getExternalStorageDirectory(), "跬步.txt", s);
    }
}
