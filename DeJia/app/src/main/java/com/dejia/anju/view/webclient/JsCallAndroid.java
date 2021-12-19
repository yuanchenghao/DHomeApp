package com.dejia.anju.view.webclient;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
public class JsCallAndroid {

    private String TAG = "JsCallAndroid";
    private Activity mContext;
    /**
     * WebView调用相机拍照的requestCode值
     */
    public static final int CAMERA_REQUEST_CODE = 1111;
    public static final int CHOOSE_FILE_REQUEST_CODE = 2222;

    public JsCallAndroid(Activity context) {
        mContext = context;
        Log.d(TAG, "JsCallAndroid: ");
    }

    @JavascriptInterface
    public int getStatusBarHeight(Context context) {
        Log.e(TAG, "状态栏高度 === " + QMUIStatusBarHelper.getStatusbarHeight(context));
        return QMUIStatusBarHelper.getStatusbarHeight(context);
    }

    /**
     * 刷新当前页面
     */
    @JavascriptInterface
    public void refreshWebView() {
    }

}
