package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseWebViewFragment;
import com.dejia.anju.event.Event;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.WebSignData;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * @author ych
 */
public class CircleFragment extends BaseWebViewFragment {
    @BindView(R.id.community_web_view)
    SmartRefreshLayout mRefreshWebView;
    @BindView(R.id.ll_web)
    LinearLayout ll_web;

    private BaseWebViewClientMessage clientManager;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 1:
                loadUrl(FinalConstant1.HTML_CIRCLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public static CircleFragment newInstance() {
        Bundle args = new Bundle();
        CircleFragment fragment = new CircleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Xml(layouts = "fragment_circle")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        clientManager = new BaseWebViewClientMessage(mContext);
        mWebView.setWebViewClient(clientManager);
        mWebView.addJavascriptInterface(new JsCallAndroid(mContext), "android");
        ll_web.addView(mWebView);
        //下拉刷新
        mRefreshWebView.setOnRefreshListener(refreshLayout -> initWebVeiw());

        clientManager.setBaseWebViewClientCallback(url -> {
            try {
                showWebDetail(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        initWebVeiw();
    }

    private void showWebDetail(String urlStr) {

    }

    @Override
    protected boolean ymShouldOverrideUrlLoading(WebView view, String url) {
        return true;
    }

    @Override
    protected void onYmProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            mRefreshWebView.finishRefresh();
        }
        super.onYmProgressChanged(view, newProgress);
    }

    @Override
    protected void initData(View view) {

    }

    /**
     * 初始化
     */
    private void initWebVeiw() {
        loadUrl(FinalConstant1.HTML_CIRCLE);
    }

    public void loadUrl(String url) {
        WebSignData addressAndHead = SignUtils.getAddressAndHead(url);
        mWebView.loadUrl(addressAndHead.getUrl(), addressAndHead.getHttpHeaders());
    }
}
