package com.dejia.anju.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.dejia.anju.R;
import com.dejia.anju.utils.Util;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.kymjs.aframe.utils.DensityUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


/**
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 * Created by Nathen on 2017/7/2.
 */
public class MyJzvdStd extends JzvdStd {

    private String videoSize;
    private ImageView shadeImageView;
    private ImageView iv_silence;//静音按钮
    private int heightTag;//1:新房相册大图预览页用
    private LinearLayout ll_net_type_tip;
    private TextView tv_net_type_tip;
    private Context context;
    private TextView tv_bottom_line;
    private OnVideoPlayStatusListener onVideoPlayStatusListener;
    private boolean isPlayComplete;
    private boolean isBottomVisibility;//true底部隐藏

    public void setBottomVisibility(boolean bottomVisibility) {
        isBottomVisibility = bottomVisibility;
    }

    public MyJzvdStd(Context context) {
        super(context);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(new JzViewOutlineProvider(DensityUtils.dip2px(context, 10)));
            this.setClipToOutline(true);
        }
        Jzvd.WIFI_TIP_DIALOG_SHOWED = false;
    }

    public MyJzvdStd(Context context, int radius) {
        super(context);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(new JzViewOutlineProvider(DensityUtils.dip2px(context, radius)));
            this.setClipToOutline(true);
        }
        Jzvd.WIFI_TIP_DIALOG_SHOWED = false;
    }

    public MyJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        //重播遮罩
        shadeImageView = findViewById(R.id.shade);
        //静音图标
        iv_silence = findViewById(R.id.iv_silence);
        iv_silence.setOnClickListener(this);
        //网络状态
        ll_net_type_tip = findViewById(R.id.ll_net_type_tip);
        tv_net_type_tip = findViewById(R.id.tv_net_type_tip);
        tv_bottom_line = findViewById(R.id.tv_bottom_line);
    }

    public void setRadius(int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(new JzViewOutlineProvider(DensityUtil.dp2px(radius)));
            this.setClipToOutline(true);
        }
        Jzvd.WIFI_TIP_DIALOG_SHOWED = false;
    }

    @Override
    public void setUp(String url, String title, int screen) {
        super.setUp(url, title, screen);
        titleTextView.setVisibility(View.VISIBLE);
        topContainer.setVisibility(VISIBLE);
    }

    public void setUp(String url, String title, int screen, String videoSize) {
        super.setUp(url, title, screen);
        titleTextView.setVisibility(View.VISIBLE);
        this.videoSize = videoSize;
    }

    public void setUp(String url, String title, int screen, String videoSize, String flag) {
        super.setUp(url, title, screen);
        if (this.screen == SCREEN_FULLSCREEN) {
            titleTextView.setVisibility(View.VISIBLE);
            tv_bottom_line.setVisibility(GONE);
        } else {
            titleTextView.setVisibility(View.INVISIBLE);
            tv_bottom_line.setVisibility(VISIBLE);
        }
        topContainer.setVisibility(VISIBLE);
        this.videoSize = videoSize;


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == cn.jzvd.R.id.fullscreen) {
            Log.i(TAG, "onClick: fullscreen button");
        } else if (i == R.id.start) {
            Log.i(TAG, "onClick: start button");
        } else if (i == R.id.iv_silence) {
            Log.i(TAG, "onClick: iv_silence button");
            v.setSelected(!v.isSelected());
            if (v.isSelected()) {
                mediaInterface.setVolume(0f, 0f);
            } else {
                mediaInterface.setVolume(1f, 1f);
            }
        } else if (i == R.id.surface_container) {
            if (onVideoPlayStatusListener != null) {
                onVideoPlayStatusListener.onStatePlaying();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        int id = v.getId();
        if (id == cn.jzvd.R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (mChangePosition) {
                        Log.i(TAG, "Touch screen seek position");
                    }
                    if (mChangeVolume) {
                        Log.i(TAG, "Touch screen change volume");
                    }
                    break;
            }
        }

        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.lib_newhouse_jz_layout_std_my;
    }

    @Override
    public void startVideo() {
        super.startVideo();
        Log.i(TAG, "startVideo");
        if (screen == SCREEN_FULLSCREEN) {
            Log.d(TAG, "startVideo [" + this.hashCode() + "] ");
            JZMediaInterface.SAVED_SURFACE = null;
            addTextureView();
            AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            JZUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            JZMediaPlayer.instance().positionInList = positionInList;
            onStatePreparing();
        } else {
            super.startVideo();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        Log.i(TAG, "Seek position ");
    }

    @Override
    public void autoFullscreen(float x) {
        super.autoFullscreen(x);
        Log.i(TAG, "auto Fullscreen");
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        Log.i(TAG, "click blank");
    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (onVideoPlayStatusListener != null) {
            onVideoPlayStatusListener.onStatePreparing(!isPlayComplete);
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        Log.i(TAG, "Auto complete");
        if (onVideoPlayStatusListener != null) {
            onVideoPlayStatusListener.onAutoPlayEnd();
        }
    }

    @Override
    public void updateStartImage() {
        super.updateStartImage();
        if (state == STATE_PLAYING) {
            shadeImageView.setVisibility(GONE);
        } else if (state == STATE_ERROR) {
            shadeImageView.setVisibility(GONE);
        } else if (state == STATE_AUTO_COMPLETE) {
            isPlayComplete = true;
            startButton.setImageResource(R.mipmap.lib_newhouse_nh_replay);
            shadeImageView.setVisibility(VISIBLE);
        } else {
            shadeImageView.setVisibility(GONE);
        }

    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        titleTextView.setVisibility(View.INVISIBLE);
        fullscreenButton.setImageResource(R.mipmap.lib_newhouse_enlarger);
        if (heightTag == 1) {
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(240)));
        }
    }

    /**
     * 退出全屏后，竖屏播放的高度满屏不符合需求，限定返回全屏的高度
     *
     * @param heightTag
     */
    public void setHeightTag(int heightTag) {
        this.heightTag = heightTag;
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        titleTextView.setVisibility(View.VISIBLE);
        //进入全屏之后要保证原来的播放状态和ui状态不变，改变个别的ui
        fullscreenButton.setImageResource(R.mipmap.lib_newhouse_shrink);
    }

    @Override
    public void showWifiDialog() {
        if (state == STATE_PAUSE) {
            startButton.performClick();
            return;
        } else {
            startVideo();
        }
        String netWork = Util.GetNetworkType(getContext());
        if (TextUtils.isEmpty(netWork)) {
            return;
        }
        if (!"2G".equals(netWork) && !"3G".equals(netWork) && !"4G".equals(netWork)) {
            netWork = "移动";
        }
        ll_net_type_tip.setVisibility(VISIBLE);
        String tips = "";
        if (TextUtils.isEmpty(videoSize)) {
            tips = "当前您使用" + netWork + "流量，观看完本视频需要消耗流量";
            tv_net_type_tip.setText(tips);
        } else {
            tips = "当前您使用" + netWork + "流量，观看完本视频需要 " + videoSize;
            int position = tips.lastIndexOf(" ");
            SpannableString spannableString = new SpannableString(tips);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FDB631"));
            spannableString.setSpan(colorSpan, position, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_net_type_tip.setText(spannableString);
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //更新主线程UI
                if (((Activity) getContext()) != null) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll_net_type_tip.setVisibility(GONE);
                        }
                    });
                }
            }
        }, 2000);//延时2s执行
    }

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        mDialogVolumeProgressBar.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.lib_newhouse_my_jz_bottom_seek_progress));
    }

    @Override
    public void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
        mDialogBrightnessProgressBar.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.lib_newhouse_my_jz_bottom_seek_progress));
    }

    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        if (isBottomVisibility) {
            bottomContainer.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        if (isBottomVisibility) {
            bottomContainer.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    public void setOnVideoPlayStatusListener(OnVideoPlayStatusListener onVideoPlayStatusListener) {
        this.onVideoPlayStatusListener = onVideoPlayStatusListener;
    }


}
