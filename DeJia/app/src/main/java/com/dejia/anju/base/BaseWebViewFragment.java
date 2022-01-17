package com.dejia.anju.base;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.WebSignData;

import org.apache.http.util.EncodingUtils;

import java.util.HashMap;

import androidx.annotation.Nullable;
@SuppressLint("Registered")
public abstract class BaseWebViewFragment extends BaseFragment {

    protected WebView mWebView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView() {
        mWebView = new WebView(mContext);
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.setHorizontalScrollBarEnabled(false);              //水平滚动条不显示
        mWebView.setVerticalScrollBarEnabled(false);                //垂直滚动条不显示
        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);
        mWebView.setLayerType(View.LAYER_TYPE_NONE, null);
        mWebView.requestFocus();

        WebSettings settings = mWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setDomStorageEnabled(true);//启用H5 DOM API （默认false）
        settings.setBuiltInZoomControls(true);// 设置支持缩放
        settings.setSupportZoom(false);// 不支持缩放
        settings.setUseWideViewPort(false);// 将图片调整到适合webview大小 设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//支持缓存
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        settings.setGeolocationEnabled(true);
        settings.setSaveFormData(true);                                     //设置webview保存表单数据
        settings.setSavePassword(true);                                     //设置webview保存密码
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);            //设置中等像素密度，medium=160dpi
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.supportMultipleWindows();  //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        settings.setBuiltInZoomControls(true); //设置支持缩放
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片

        /*********************************************************/
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        // 开启 DOM storage API 功能
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);    //支持缩放
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        /********************************************************/

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                onYmLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("data:text/plain")){
                    return true;
                }
                return ymShouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                onYmPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                onYmReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                onYmReceivedError(view, request, error);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                onYmReceivedTitle(view, title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return onYmJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return onYmJsConfirm(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                onYmProgressChanged(view, newProgress);
            }
        });
    }

    protected void onYmLoadResource(WebView view, String url) {

    }

    protected boolean ymShouldOverrideUrlLoading(WebView view, String request) {
        return false;
    }

    protected void onYmPageFinished(WebView view, String url) {

    }

    protected void onYmReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

    }

    protected void onYmReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }


    /**
     * onJsAlert回调
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    protected boolean onYmJsAlert(WebView view, String url, String message, JsResult result) {
        return true;
    }

    /**
     * onJsConfirm回调
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    protected boolean onYmJsConfirm(WebView view, String url, String message, JsResult result) {
        return true;
    }

    /**
     * onProgressChanged回调
     *
     * @param view
     * @param newProgress
     */
    protected void onYmProgressChanged(WebView view, int newProgress) {
    }

    /**
     * 获取title
     *
     * @param view
     * @param title
     */
    protected void onYmReceivedTitle(WebView view, String title) {
    }

//    /**
//     * 加载webView
//     *
//     * @param url
//     * @param paramMap
//     * @param headMap
//     */
//    protected void loadUrl(String url, Map<String, Object> paramMap, Map<String, Object> headMap) {
//        WebSignData addressAndHead = SignUtils.getAddressAndHead(url, paramMap, headMap);
//        mWebView.loadUrl(addressAndHead.getUrl(), addressAndHead.getHttpHeaders());
//    }
//
//    protected void loadUrl(String url, Map<String, Object> paramMap) {
//        WebSignData addressAndHead = SignUtils.getAddressAndHead(url, paramMap);
//        mWebView.loadUrl(addressAndHead.getUrl(), addressAndHead.getHttpHeaders());
//    }
//
//    protected void loadUrl(String url) {
//        WebSignData addressAndHead = SignUtils.getAddressAndHead(url);
//        mWebView.loadUrl(addressAndHead.getUrl(), addressAndHead.getHttpHeaders());
//    }

    protected void postUrl(String url) {
        WebSignData addressAndHead = SignUtils.getAddressAndHead(url);
        HashMap<String, Object> addressAndHeadMap = new HashMap<>(0);
        mWebView.postUrl(url, EncodingUtils.getBytes(SignUtils.buildHttpParam4(addressAndHeadMap), "UTF-8"));
    }


}