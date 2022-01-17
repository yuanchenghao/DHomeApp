package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseWebViewFragment;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhangyue.we.x2c.ano.Xml;

import butterknife.BindView;

/**
 * @author ych
 */
public class CircleFragment extends BaseWebViewFragment {
    @BindView(R.id.community_web_view)
    SmartRefreshLayout mRefreshWebView;
    @BindView(R.id.ll_web)
    LinearLayout ll_web;
    @BindView(R.id.ll)
    LinearLayout ll;
    private BaseWebViewClientMessage clientManager;

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
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        clientManager = new BaseWebViewClientMessage(mContext);
        mWebView.setWebViewClient(clientManager);
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
        postUrl(FinalConstant1.HTML_CIRCLE);
    }
}
