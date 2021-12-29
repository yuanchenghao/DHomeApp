package com.dejia.anju.view.webclient;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import com.dejia.anju.AppLog;
import com.dejia.anju.utils.ToastUtils;

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
        ((Activity)mContext).finish();
    }

}
