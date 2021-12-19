package com.dejia.anju.fragment;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseWebViewFragment;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.view.webclient.BaseWebViewClientCallback;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class CircleFragment extends BaseWebViewFragment {
    @BindView(R.id.community_web_view)
    SmartRefreshLayout mRefreshWebView;
    @BindView(R.id.ll_web)
    LinearLayout ll_web;
    @BindView(R.id.ll)
    LinearLayout ll;
    private BaseWebViewClientMessage clientManager;

    @Xml(layouts = "fragment_circle")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        clientManager = new BaseWebViewClientMessage(mContext);
        mWebView.addJavascriptInterface(new JsCallAndroid(getActivity()), "android");
        mWebView.setWebViewClient(clientManager);
        ll_web.addView(mWebView);
        //下拉刷新
        mRefreshWebView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initWebVeiw();
            }
        });

        clientManager.setBaseWebViewClientCallback(new BaseWebViewClientCallback() {
            @Override
            public void otherJump(String url) {
                try {
                    showWebDetail(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
}
