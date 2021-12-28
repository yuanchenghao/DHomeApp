package com.dejia.anju.view.webclient;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import com.dejia.anju.AppLog;

public class JsCallAndroid {
    public Activity mContext;

    public JsCallAndroid(Activity context) {
        mContext = context;
    }


    /**
     * 分享
     */
    @JavascriptInterface
    public void appToShare() {

    }

    /**
     * 返回
     */
    @JavascriptInterface
    public void appGoBack() {
        AppLog.i("js调用成功");
        ((Activity)mContext).finish();
    }
}
