package com.dejia.anju.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dejia.anju.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * 文 件 名: DHomeVideoPlayer
 * 创 建 人: 原成昊
 * 创建日期: 2022/7/3 22:56
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */

public class DHomeVideoPlayer extends StandardGSYVideoPlayer {
    private boolean isLinkScroll = false;

    public DHomeVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public DHomeVideoPlayer(Context context) {
        super(context);
    }

    public DHomeVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void init(Context context) {
        super.init(context);
        post(new Runnable() {
            @Override
            public void run() {
                gestureDetector = new GestureDetector(getContext().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        touchDoubleUp(e);
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness
                                && mCurrentState != CURRENT_STATE_ERROR
                        ) {
                            onClickUiToggle(e);
                        }
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                    }
                });
            }
        });
    }

    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.sample_video_land;
        }
        return R.layout.sample_video_normal;
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            super.updateStartImage();
        }
    }

    @Override
    public int getEnlargeImageRes() {
        return R.drawable.custom_enlarge;
    }

    @Override
    public int getShrinkImageRes() {
        return R.drawable.custom_shrink;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLinkScroll && !isIfCurrentIsFullscreen()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        DHomeVideoPlayer landLayoutVideo = (DHomeVideoPlayer) gsyVideoPlayer;
        landLayoutVideo.dismissProgressDialog();
        landLayoutVideo.dismissVolumeDialog();
        landLayoutVideo.dismissBrightnessDialog();
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    public void setLinkScroll(boolean linkScroll) {
        isLinkScroll = linkScroll;
    }


    /**
     * 定义结束后的显示
     */
    @Override
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();
        setTextAndProgress(0, true);
        //changeUiToNormal();
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        setTextAndProgress(0, true);
        //changeUiToNormal();
    }
}
