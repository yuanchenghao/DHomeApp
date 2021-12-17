package com.dejia.anju.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseWebViewFragment;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class CircleFragment extends BaseWebViewFragment {
    @BindView(R.id.ll_title)
    RelativeLayout ll_title;
    @BindView(R.id.ll_web)
    LinearLayout ll_web;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    public WebView docDetWeb;

    @Xml(layouts = "fragment_circle")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        initWebView();
//        mWebView.addJavascriptInterface(new JsCallAndroid(getActivity()), "android");
//        BaseWebViewClientMessage baseWebViewClientMessage = new BaseWebViewClientMessage(getActivity());
//        mWebView.setWebViewClient(baseWebViewClientMessage);
//        ll_web.addView(mWebView);
//        smartRefreshLayout.setEnableLoadMore(false);
//        smartRefreshLayout.autoRefresh();
//        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//            }
//
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                loadUrl("https://sjapp.yuemei.com/homenew/newhtmlhome/");
//            }
//        });
    }

    @Override
    protected void initData(View view) {

    }


    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView() {
        docDetWeb = new WebView(getActivity());
//        docDetWeb.setBackgroundColor(Color.parseColor("#000000"));
        docDetWeb.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_web.removeAllViews();
        ll_web.addView(docDetWeb);
        BaseWebViewClientMessage baseWebViewClientMessage = new BaseWebViewClientMessage(getActivity());
        docDetWeb.setHorizontalScrollBarEnabled(false);//水平滚动条不显示
        docDetWeb.setVerticalScrollBarEnabled(false); //垂直滚动条不显示
        docDetWeb.setLongClickable(true);
        docDetWeb.setScrollbarFadingEnabled(true);
        docDetWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        docDetWeb.setDrawingCacheEnabled(true);
        docDetWeb.setWebViewClient(baseWebViewClientMessage);
//        docDetWeb.addJavascriptInterface(new MagicMirrorJSCallBack(MagicMirrorWebActivity.this), "android");
        //设置 缓存模式
        WebSettings settings = docDetWeb.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        settings.setSaveFormData(true);    //设置webview保存表单数据
        settings.setSavePassword(true);    //设置webview保存密码
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
        settings.setSupportZoom(false);    //支持缩放
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.supportMultipleWindows();
        settings.setNeedInitialFocus(true);
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        docDetWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                //为解决6.0系统上，这个api会调用两次，而且第一次是显示url的系统bug
//                if (null != title && !docDetWeb.getUrl().contains(title)) {
//                    htmlTitle = title;
//                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
//                    smartRefreshLayout.finishRefresh();
                }
            }

            //重写WebChromeClient的onGeolocationPermissionsShowPrompt
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });
        docDetWeb.loadUrl(FinalConstant1.HTML_CIRCLE);
//        docDetWeb.loadData(SignUtils.getAddressAndHead(FinalConstant1.HTML_CIRCLE).getUrl(),"text/html", "utf-8");
    }
}
