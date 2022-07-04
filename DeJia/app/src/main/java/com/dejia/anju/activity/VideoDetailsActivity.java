package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dejia.anju.R;
import com.dejia.anju.api.VideoAddViewApi;
import com.dejia.anju.api.VideoIndexApi;
import com.dejia.anju.base.WebViewActivityImpl;
import com.dejia.anju.event.Event;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.WebSignData;
import com.dejia.anju.video.DHomeVideoPlayer;
import com.dejia.anju.video.MyJzvdStd;
import com.dejia.anju.video.OnVideoPlayStatusListener;
import com.dejia.anju.view.webclient.BaseWebViewClientMessage;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.shuyu.aliplay.AliPlayerManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.HashMap;

import androidx.annotation.NonNull;
import butterknife.BindView;

//视频页
//原生视频详情接口
//https://api.dejiaapp.com/video/index/?article_id=76298&act=61D7EF3DB114B66D&Paul=1
//视频播放加次数的接口
//https://api.dejiaapp.com/video/addViews/?article_id=76298&act=61D7EF3DB114B66D&Paul=1
public class VideoDetailsActivity extends WebViewActivityImpl implements OnClickListener {
    @BindView(R.id.fl_web)
    FrameLayout fl_web;
    //    @BindView(R.id.iv_video)
//    MyJzvdStd iv_video;
    @BindView(R.id.iv_video)
    DHomeVideoPlayer iv_video;

    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    private VideoAddViewApi videoAddViewApi;
    private VideoIndexApi videoIndexApi;
    private BaseWebViewClientMessage clientManager;
    private String mArticle_id;
    private boolean isPlay;
    private boolean isPause;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            iv_video.getCurrentPlayer().release();
        }
//        if (orientationUtils != null)
//            orientationUtils.releaseListener();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        mArticle_id = getIntent().getStringExtra("mArticle_id");
        if (TextUtils.isEmpty(mArticle_id)) {
            finish();
            return;
        }
        getVideoInfo();
    }

    private void getVideoInfo() {
//        videoIndexApi = new VideoIndexApi();
//        HashMap<String, Object> maps = new HashMap<>(1);
//        maps.put("article_id", mArticle_id);
//        videoIndexApi.getCallBack(mContext, maps, new BaseCallBackListener() {
//            @Override
//            public void onSuccess(Object o) {
        initWebView();
        initVideo();
//            }
//        });
    }

    private void initVideo() {
//        iv_video.setUp("https://player.yuemei.com/video/6ea2/201810/c3e214deeefe3b46774b07b9acc8495a.mp4", "", JzvdStd.SCREEN_NORMAL);
//        iv_video.setHeightTag(1);
////        iv_video.posterImageView.setImageBitmap("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
//        iv_video.setOnVideoPlayStatusListener(new OnVideoPlayStatusListener() {
//            @Override
//            public void onStatePreparing(boolean isFirstPlay) {
//
//            }
//
//            @Override
//            public void onStatePlaying() {
//
//            }
//
//            @Override
//            public void onAutoPlayEnd() {
////                addVideoPlayCount();
//            }
//        });
        //aliplay 内核，默认模式
        PlayerFactory.setPlayManager(AliPlayerManager.class);
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
        CacheFactory.setCacheManager(ProxyCacheManager.class);
//        iv_video.findViewById(R.id.lock_screen).setVisibility(View.GONE);
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView);
        gsyVideoOption.setIsTouchWiget(true);
        gsyVideoOption.setRotateViewAuto(false);
        gsyVideoOption.setLockLand(false);
        gsyVideoOption.setAutoFullWithSize(false);
        gsyVideoOption.setShowFullAnimation(false);
        gsyVideoOption.setNeedLockFull(true);
        gsyVideoOption.setUrl("https://player.yuemei.com/video/6ea2/201810/c3e214deeefe3b46774b07b9acc8495a.mp4");
        gsyVideoOption.setCacheWithPlay(false);
        gsyVideoOption.setVideoTitle("");
        gsyVideoOption.setNeedOrientationUtils(false);
        gsyVideoOption.setVideoAllCallBack(new GSYSampleCallBack() {

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                if (iv_video.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    GSYVideoManager.backFromWindowFull(mContext);
//                    upVideoEndUi();
                }
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                isPlay = true;
            }

        });
        //不需要旋转
        gsyVideoOption.build(iv_video);
        iv_video.getCurrentPlayer().getFullscreenButton().setOnClickListener(v -> iv_video.startWindowFullscreen(mContext, true, true));
        iv_video.getCurrentPlayer().findViewById(R.id.back).setOnClickListener(v -> {
            if (!iv_video.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                onBackPressed();
            }
        });
        //自动播放
        iv_video.getCurrentPlayer().startPlayLogic();
    }

    @Override
    public void onBackPressed() {
        // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
        // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
//        if (orientationUtils != null) {
//            orientationUtils.backToProtVideo();
//        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        iv_video.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        iv_video.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }


    /**
     * orientationUtils 和  detailPlayer.onConfigurationChanged 方法是用于触发屏幕旋转的
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
//        if (isPlay && !isPause) {
//            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
//        }
    }

    private void addVideoPlayCount() {
        videoAddViewApi = new VideoAddViewApi();
        HashMap<String, Object> maps = new HashMap<>(1);
        maps.put("article_id", mArticle_id);
        videoIndexApi.getCallBack(mContext, maps, o -> {

        });
    }

    private void initWebView() {
        clientManager = new BaseWebViewClientMessage(mContext);
        mWebView.setWebViewClient(clientManager);
        mWebView.addJavascriptInterface(new JsCallAndroid(mContext), "android");
        fl_web.addView(mWebView);
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
//            mRefreshWebView.finishRefresh();
        }
        super.onYmProgressChanged(view, newProgress);
    }

    @Override
    protected void initData() {
        setMultiOnClickListener();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.ll_back:
//                finish();
//                break;

        }
    }

//    @Override
//    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Jzvd.releaseAllVideos();
//    }

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

    public static void invoke(Context context, String article_id) {
        Intent intent = new Intent(context, VideoDetailsActivity.class);
        intent.putExtra("mArticle_id", article_id);
        context.startActivity(intent);
    }

}
