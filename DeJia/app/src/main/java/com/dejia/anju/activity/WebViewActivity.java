package com.dejia.anju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dejia.anju.R;
import com.dejia.anju.base.WebViewActivityImpl;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.CommentInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.WebSignData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.view.CommonTopBar;
import com.dejia.anju.view.DiaryCommentDialogView;
import com.dejia.anju.view.MyPullRefresh;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.qmuiteam.qmui.util.QMUIDisplayHelper.getStatusBarHeight;

/**
 * @author ych
 * 公共webview
 */
public class WebViewActivity extends WebViewActivityImpl {

    @BindView(R.id.activity_web_view)
    LinearLayout activityWebView;
    @BindView(R.id.web_view_container)
    FrameLayout mWebViewContainer;
    @BindView(R.id.web_view_refresh_container)
    MyPullRefresh mRefreshWebViewContainer;
    @BindView(R.id.iv)
    ImageView iv;
    private List<String> imgList;
    private DiaryCommentDialogView diaryCommentDialogView;
    private WebViewData mWebViewData;
    private CommentInfo commentInfo;
    public static final String WEB_DATA = "WebData";
    private static final String JS_NAME = "android";
    private CommonTopBar mTopTitle;
    private String linkUrl;
    private WebSignData addressAndHead;
    public static final int REQUEST_FILE_PICKER = 1;
    public ValueCallback<Uri> mFilePathCallback;
    public ValueCallback<Uri[]> mFilePathCallbacks;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 7:
                commentInfo = (CommentInfo) msgEvent.getData();
                if (commentInfo != null) {
                    diaryCommentDialogView = new DiaryCommentDialogView(mContext, null);
                    if (commentInfo.isShowImgBtn()) {
                        diaryCommentDialogView.setPicturesChooseGone(false);
                    } else {
                        diaryCommentDialogView.setPicturesChooseGone(true);
                    }
                    if (!diaryCommentDialogView.isShowing()) {
                        diaryCommentDialogView.showDialog();
                    }
                }
                break;
        }
    }

    @Xml(layouts = "activity_web_view")
    @Override
    protected int getLayoutId() {
        // 获取上个页面传递过来的数据
        mWebViewData = getIntent().getParcelableExtra(WEB_DATA);
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
        imgList = new ArrayList<>();
        iv.setOnClickListener(v -> {
            diaryCommentDialogView = new DiaryCommentDialogView(mContext, null);
            diaryCommentDialogView.showDialog();
        });
    }

    @Override
    protected void initData() {
        if (mWebViewContainer.getChildCount() > 0) {
            mWebViewContainer.removeAllViews();
        }
        //背景颜色
        if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getBgColor())) {
            activityWebView.setBackgroundColor(Color.parseColor(mWebViewData.getBgColor()));
        }
        //是否需要刷新
        if (mWebViewData != null && !TextUtils.isEmpty(mWebViewData.getIsRefresh()) && "1".equals(mWebViewData.getIsRefresh())) {
            mRefreshWebViewContainer.setVisibility(View.VISIBLE);
            mWebViewContainer.setVisibility(View.GONE);
            mRefreshWebViewContainer.addView(mWebView);
            mRefreshWebViewContainer.setRefreshListener(() -> loadLink());
        } else {
            mRefreshWebViewContainer.setVisibility(View.GONE);
            mWebViewContainer.setVisibility(View.VISIBLE);
            mWebViewContainer.addView(mWebView);
        }
        loadLink();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCallBack(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 1:
                loadLink();
                break;
        }
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void initWebView() {
        BaseWebViewClientMessage clientManager = new BaseWebViewClientMessage(this);
        mWebView.setWebViewClient(clientManager);
        mWebView.addJavascriptInterface(new JsCallAndroid(mContext), JS_NAME);
        //主题设置
        if (mWebViewData != null
                && !TextUtils.isEmpty(mWebViewData.getEnableSafeArea())
                && "1".equals(mWebViewData.getEnableSafeArea())) {
            //状态栏黑色
            QMUIStatusBarHelper.setStatusBarLightMode(mContext);
        } else {
            //沉浸式布局
            QMUIStatusBarHelper.translucent(mContext);
            QMUIStatusBarHelper.setStatusBarLightMode(mContext);
        }
        //是否需要标题
        if (mWebViewData != null
                && !TextUtils.isEmpty(mWebViewData.getIsHide())
                && "0".equals(mWebViewData.getIsHide())) {
            //不隐藏标题头
            mTopTitle = new CommonTopBar(WebViewActivity.this);
            //是否显示返回键
            if (mWebViewData != null
                    && !TextUtils.isEmpty(mWebViewData.getIs_back())
                    && "1".equals(mWebViewData.getIs_back())) {
                mTopTitle.setLeftImgVisibility(View.VISIBLE);
            } else {
                mTopTitle.setLeftImgVisibility(View.GONE);
            }
            //是否显示分享
            if (mWebViewData != null
                    && !TextUtils.isEmpty(mWebViewData.getIs_share())
                    && "1".equals(mWebViewData.getIs_share())) {
                mTopTitle.setRightImgVisibility(View.VISIBLE);
            } else {
                mTopTitle.setRightImgVisibility(View.GONE);
            }
            int statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) activityWebView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.topMargin = statusbarHeight;
                activityWebView.setLayoutParams(layoutParams);
            }
            activityWebView.addView(mTopTitle, 0);
        }
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
                    && !TextUtils.isEmpty(mWebViewData.getIsRefresh())
                    && "1".equals(mWebViewData.getIsRefresh())) {
                mRefreshWebViewContainer.finishRefresh();
            }
        }
    }


    @Override
    protected void onYmPageFinished(WebView view, String url) {
    }

    @Override
    protected void onYmShowFileChooserChanged(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        if (mContext == null || mContext.isFinishing()) {
            return;
        }
        mFilePathCallbacks = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
    }

    /**
     * Activity回调处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 处理相机相册选择
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_PICKER) {
            if (mFilePathCallback != null) {
                Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
                if (result != null) {
                    mFilePathCallback.onReceiveValue(result);
                } else {
                    mFilePathCallback.onReceiveValue(null);
                }
            }
            if (mFilePathCallbacks != null) {
                Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
                if (result != null) {
                    mFilePathCallbacks.onReceiveValue(new Uri[]{result});
                } else {
                    mFilePathCallbacks.onReceiveValue(null);
                }
            }
            mFilePathCallback = null;
            mFilePathCallbacks = null;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST_COMMENT:
                case PictureConfig.REQUEST_CAMERA_COMMENT:
                    // 结果回调
                    List<LocalMedia> chooseResult = PictureSelector.obtainMultipleResult(data);
                    if (chooseResult != null && chooseResult.size() > 0) {
                        if (diaryCommentDialogView != null) {
                            imgList.add(chooseResult.get(0).getCutPath());
                            diaryCommentDialogView.setmResults(imgList);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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


    /**
     * 加载URL
     */
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
            addressAndHead = SignUtils.getAddressAndHead(url, JSONUtil.getMapForJson(mWebViewData.getRequest_param()));
        } else {
            addressAndHead = SignUtils.getAddressAndHead(url);
        }
        mWebView.loadUrl(addressAndHead.getUrl(), addressAndHead.getHttpHeaders());
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
