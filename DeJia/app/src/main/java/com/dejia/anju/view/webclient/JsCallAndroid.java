package com.dejia.anju.view.webclient;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.dejia.anju.AppLog;
import com.dejia.anju.activity.UgcImgDialogActivity;
import com.dejia.anju.model.UgcImgInfo;
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;

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
        ToastUtils.toast(mContext, "正在开发中").show();
    }

    /**
     * 返回
     */
    @JavascriptInterface
    public void appGoBack() {
        ((Activity) mContext).finish();
    }


    /**
     * 图片浮层
     */
    @JavascriptInterface
    public void showUgcImg(String img) {
        if (!TextUtils.isEmpty(img)) {
            UgcImgInfo ugcImgInfo = JSONUtil.TransformSingleBean(img, UgcImgInfo.class);
            if (ugcImgInfo != null && ugcImgInfo.getImgList().size() > 0) {
                UgcImgDialogActivity.invoke(mContext,ugcImgInfo);
            }
        }
    }
}
