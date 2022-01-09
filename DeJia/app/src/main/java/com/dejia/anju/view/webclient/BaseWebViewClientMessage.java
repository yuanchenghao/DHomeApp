package com.dejia.anju.view.webclient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dejia.anju.AppLog;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.WebSignData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

public class BaseWebViewClientMessage extends WebViewClient {

    private String mObjid;
    private String mSource;
    private Activity mActivity;
    private String TAG = "BaseWebViewClient";
    private View contentLy;
    private BaseWebViewClientCallback baseWebViewClientCallback;
    private BaseWebViewTel baseWebViewTel;
    private WebViewTypeOutside webViewTypeOutside;
    private boolean isTypeOutside = false;
    private String ztid5983 = "0";
    private String objType5983 = "0";
    private String title5983 = "";
    private String img5983 = "";
    private String url = "";
    private String mYmClass = "";
    private String mYmId = "";
    private WebView mWebView;
    private JSONObject mJsonObject;


    public BaseWebViewClientMessage(Activity activity) {
        this(activity, "0");
    }

    public BaseWebViewClientMessage(Activity activity, String source) {
        this(activity, source, "0");
    }

    public BaseWebViewClientMessage(Activity activity, String source, String objid) {
        this.mActivity = activity;
        this.mSource = source;
        this.mObjid = objid;
    }

    public BaseWebViewClientMessage(Activity activity, String source, String objid, WebView webView) {
        this.mActivity = activity;
        this.mSource = source;
        this.mObjid = objid;
        this.mWebView = webView;
    }

    /**
     * 加载资源
     *
     * @param view
     * @param url
     */
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    /**
     * 应该重写Url加载
     *
     * @param view
     * @param url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(@NotNull WebView view, String url) {
        AppLog.i("shouldOverrideUrlLoading......");
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        if (null == hitTestResult)
            return false;
        int type = hitTestResult.getType();
        switch (type) {
            case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                break;
            case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                break;
            case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                break;
            case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                WebUrlJumpManager.getInstance().invoke(mActivity, url, null);
                break;
            case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项 }
                return true;
            case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                break;
        }
//        if (isTypeOutside) {
//            if (webViewTypeOutside != null) {
//                webViewTypeOutside.typeOutside(view, url);
//            }
//        } else {
//            if (url.startsWith("type")) {
//                showWebDetail(url);
//            } else if (url.startsWith("tel")) {
//                if (baseWebViewTel != null) {
//                    baseWebViewTel.tel(view, url);
//                }
//            } else {
//                WebUrlTypeUtil.getInstance(mActivity).urlToApp(url, mSource, mObjid);
//            }
//        }
        return true;
    }


    /**
     * 加载开始
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.e(TAG, "onPageStarted......");
    }

    /**
     * 加载完成
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        Log.e(TAG, "onPageFinished......");
//        view.loadUrl("javascript:window.getShareData.OnGetShareData(" + "document.querySelector('meta[name=\"reply_info\"]').getAttribute('content')" + ");");
        super.onPageFinished(view, url);
    }

    /**
     * 在收到Ssl错误
     *
     * @param view
     * @param handler
     * @param error
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log.e(TAG, "onReceivedSslError......");
        handler.proceed();
    }

    /**
     * 在收到错误
     *
     * @param view
     * @param request
     * @param error
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "onReceivedError111......" + request.toString());
        Log.e(TAG, "onReceivedError222......" + error.toString());
        super.onReceivedError(view, request, error);
    }


    /**
     * 处理webview里面的按钮
     *
     * @param urlStr
     */
    public void showWebDetail(String urlStr) {
        Log.e(TAG, "onReceivedError111......" + urlStr.toString());
    }


    public void loadUrl(WebView webView, String mUrl, HashMap<String, Object> mSingStr) {
        WebSignData addressAndHead = SignUtils.getAddressAndHead(mUrl, mSingStr);
        webView.loadUrl(addressAndHead.getUrl(), addressAndHead.getHttpHeaders());
    }

    /**
     * 这里432、5466、6080、6011、441
     */
    public void setView(View view) {
        this.contentLy = view;
    }

    /**
     * 其他跳转
     *
     * @param baseWebViewClientCallback
     */
    public void setBaseWebViewClientCallback(BaseWebViewClientCallback baseWebViewClientCallback) {
        this.baseWebViewClientCallback = baseWebViewClientCallback;
    }

    /**
     * 页面刷新回调
     *
     * @param baseWebViewReload
     */
    public void setBaseWebViewReload(BaseWebViewReload baseWebViewReload) {
    }

    /**
     * 第二种跳转回调
     *
     * @param baseWebViewTel
     */
    public void setBaseWebViewTel(BaseWebViewTel baseWebViewTel) {
        this.baseWebViewTel = baseWebViewTel;
    }


    /**
     * 设置回调
     */
    public void setTypeOutside(boolean typeOutside) {
        isTypeOutside = typeOutside;
    }

    /**
     * 全部shouldOverrideUrlLoading处理的回调
     *
     * @param webViewTypeOutside
     */
    public void setWebViewTypeOutside(WebViewTypeOutside webViewTypeOutside) {
        this.webViewTypeOutside = webViewTypeOutside;
    }

}
