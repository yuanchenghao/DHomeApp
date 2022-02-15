package com.dejia.anju.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseWebViewFragment;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.WebSignData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author ych
 * 搜索结果
 */
public class SearchResultFragment extends BaseWebViewFragment {
    @BindView(R.id.fl)
    FrameLayout fl;
    //webview封装参数
    private WebViewData mWebViewData;
    private String linkUrl;
    private WebSignData addressAndHead;

    public static SearchResultFragment newInstance(WebViewData webViewData) {
        Bundle args = new Bundle();
        args.putParcelable("webdata", webViewData);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Xml(layouts = "fragment_search_result")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initView(View view) {
        mWebViewData = getArguments().getParcelable("webdata");
        if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getLinkisJoint()) && "1".equals(mWebViewData.getLinkisJoint())) {
            //拼接
            if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getLink())) {
                if (mWebViewData.getRequest_param() != null && !TextUtils.isEmpty(mWebViewData.getRequest_param())) {
                    Map<String, Object> map = JSONUtil.getMapForJson(mWebViewData.getRequest_param());
                    StringBuilder builder = new StringBuilder();
                    List<String> keys = new ArrayList<>(map.keySet());
                    for (int i = 0; i < keys.size(); i++) {
                        String key = keys.get(i);
                        String value = map.get(key) + "";
                        builder.append(key).append("/").append(value).append("/");
                    }
                    linkUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.TEST_BASE_URL
                            + mWebViewData.getLink() + builder;
                } else {
                    linkUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.TEST_BASE_URL
                            + mWebViewData.getLink();
                }
            }
        } else {
            //不需要拼接
            if (mWebViewData.getRequest_param() != null && !TextUtils.isEmpty(mWebViewData.getRequest_param())) {
                Map<String, Object> map = JSONUtil.getMapForJson(mWebViewData.getRequest_param());
                StringBuilder builder = new StringBuilder();
                List<String> keys = new ArrayList<>(map.keySet());
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    String value = (String) map.get(key);
                    builder.append(key).append("/").append(value).append("/");
                }
                if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getLink())) {
                    linkUrl = mWebViewData.getLink() + builder;
                }
            } else {
                if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getLink())) {
                    linkUrl = mWebViewData.getLink();
                }
            }
        }
        fl.addView(mWebView);
        BaseWebViewClientMessage clientManager = new BaseWebViewClientMessage(mContext);
        mWebView.setWebViewClient(clientManager);
        mWebView.addJavascriptInterface(new JsCallAndroid(mContext), "android");
        loadLink();
    }

    @Override
    protected void initData(View view) {

    }

    private void loadLink() {
        if (mWebView != null && mWebViewData != null) {
            // 跳转并进行页面加载
            if (!TextUtils.isEmpty(linkUrl)) {
                loadUrl(linkUrl);
            }
        }
    }

    public void loadUrl(String url) {
        if (mWebViewData.getRequest_param() != null && !TextUtils.isEmpty(mWebViewData.getRequest_param())) {
            addressAndHead = SignUtils.getAddressAndHead(url,JSONUtil.getMapForJson(mWebViewData.getRequest_param()));
        }else{
            addressAndHead = SignUtils.getAddressAndHead(url);
        }
        mWebView.loadUrl(addressAndHead.getUrl(),addressAndHead.getHttpHeaders());
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
        DestroyWebView();
        super.onDestroy();
    }

    private void DestroyWebView() {
        if (mWebView != null) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.clearFormData();
            mWebView.clearMatches();
            mWebView.clearSslPreferences();
            mWebView.clearDisappearingChildren();
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.freeMemory();
            mWebView.clearAnimation();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

}
