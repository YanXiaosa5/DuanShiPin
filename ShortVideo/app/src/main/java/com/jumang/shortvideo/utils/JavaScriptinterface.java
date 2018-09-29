package com.jumang.shortvideo.utils;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by steven on 2017/5/14.
 * Email-songzhonghua_1987@msn.com
 */

public class JavaScriptinterface {
    Context context;

    public JavaScriptinterface(Context c) {
        context = c;
    }

    /**
     * 弹出Toast提示框
     */
    @JavascriptInterface
    public void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    /**
     * 弹出Toast提示框
     */
    @JavascriptInterface
    public void finish() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

}
