package com.dejia.anju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.dejia.anju.R;
import com.dejia.anju.base.WebViewActivityImpl;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.view.CommonTopBar;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import butterknife.BindView;

import static com.qmuiteam.qmui.util.QMUIDisplayHelper.getStatusBarHeight;

public class WebViewActivity extends WebViewActivityImpl {

    @BindView(R.id.activity_web_view)
    LinearLayout activityWebView;
    @BindView(R.id.web_view_container)
    FrameLayout mWebViewContainer;
    @BindView(R.id.web_view_refresh_container)
    SmartRefreshLayout mRefreshWebViewContainer;
    @BindView(R.id.smartRefreshHeader)
    ClassicsHeader smartRefreshHeader;
    private WebViewData mWebViewData;
    public static final String WEB_DATA = "WebData";
    private static final String JS_NAME = "android";
    private CommonTopBar mTopTitle;
    private BaseWebViewClientMessage clientManager;

    @Override
    protected int getLayoutId() {
        // 获取上个页面传递过来的数据
        mWebViewData = getIntent().getParcelableExtra(WEB_DATA);
        //主题设置
        if(mWebViewData != null
                && !TextUtils.isEmpty(mWebViewData.getEnableSafeArea())
                && "1".equals(mWebViewData.getEnableSafeArea())){
            setTheme(R.style.AppThemeprice);
        }
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {
        super.initView();
        // WebView初始化
        initWebView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initData() {
//        if (mWebViewContainer.getChildCount() > 0) {
//            mWebViewContainer.removeAllViews();
//        }
        if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getEnableSafeArea()) && "1".equals(mWebViewData.getEnableSafeArea())) {
            setStatusBarView(WebViewActivity.this);
        }
        if(mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getBgColor())){
            activityWebView.setBackgroundColor(Color.parseColor(mWebViewData.getBgColor()));
        }
        if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getIsRefresh()) && "1".equals(mWebViewData.getIsRefresh())) {
            mRefreshWebViewContainer.setVisibility(View.VISIBLE);
            smartRefreshHeader.setVisibility(View.VISIBLE);
            mWebViewContainer.setVisibility(View.GONE);
//            mRefreshWebViewContainer.addView(mWebView);
            mRefreshWebViewContainer.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    loadUrl();
                }
            });
        } else {
            mRefreshWebViewContainer.setVisibility(View.GONE);
            mWebViewContainer.setVisibility(View.VISIBLE);
            mWebViewContainer.addView(mWebView);
        }
        loadUrl();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCallBack(WebViewData webViewData) {

    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void initWebView() {
        clientManager = new BaseWebViewClientMessage(mContext);
        mWebView.addJavascriptInterface(new JsCallAndroid(mContext), JS_NAME);
        mWebView.setWebViewClient(clientManager);
        mWebViewContainer.addView(mWebView);
        //是否需要标题
        if (mWebViewData != null
                && !TextUtils.isEmpty(mWebViewData.getIsHide())
                && "0".equals(mWebViewData.getIsHide())) {
            //不隐藏
            mTopTitle = new CommonTopBar(WebViewActivity.this);
            int statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) activityWebView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.topMargin = statusbarHeight;
                activityWebView.setLayoutParams(layoutParams);
            }
            activityWebView.addView(mTopTitle, 0);
        }
    }

    @Override
    protected void onYmReceivedTitle(WebView view, String title) {
        if (mTopTitle != null) {
            if (mWebViewData != null && !TextUtils.isEmpty(title)) {
                mTopTitle.setCenterText(title);
            } else {
                if (title.startsWith("http")) {
                    mTopTitle.setCenterText("得家");
                } else {
                    mTopTitle.setCenterText(title);
                }
            }
        }
    }

    @Override
    protected void onYmProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            //刷新关闭
            if (mRefreshWebViewContainer != null
                    && mWebViewData != null
                    && TextUtils.isEmpty(mWebViewData.getIsRefresh())
                    && "1".equals(mWebViewData.getIsRefresh())) {
                mRefreshWebViewContainer.finishRefresh();
            }
        }
    }


    @Override
    protected void onYmPageFinished(WebView view, String url) {
    }





    @Override
    public void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mWebView != null) {
            mWebView.onResume();
        }
        super.onResume();
    }


    @Override
    public void onDestroy() {
        if (mWebViewContainer != null && mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (mWebView != null && (keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
//            if (mWebViewData != null && TextUtils.isEmpty(mWebViewData.get)) {
//                mWebView.goBack(); // 浏览网页历史记录 goBack()和goForward()
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    /**
     * 加载URL
     */
    private void loadUrl() {
        if (mWebView != null && mWebViewData != null) {
            // 跳转并进行页面加载
//            mWebView.loadUrl();
        }
    }


    /**
     * 添加View到状态栏，在沉浸式状态下不侵入状态栏
     */
    public void setStatusBarView(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 生成一个状态栏大小的矩形
            View StatusView = createStatusView(activity);
            // 添加statusView到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(StatusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) (((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
            rootView.setFitsSystemWindows(true);
        }
    }

    private View createStatusView(Activity activity) {
        int statusBarHeight = getStatusBarHeight(activity);
        View view = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }


}
