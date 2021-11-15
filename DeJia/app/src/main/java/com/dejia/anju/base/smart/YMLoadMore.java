package com.dejia.anju.base.smart;

import android.content.Context;

import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 自定义SmartRefreshLayout加载更多
 * <p>
 * SmartRefreshLayout.finishLoadMoreWithNoMoreData();   没有更多了：设置之后，将不会再触发加载事件
 * <p>
 * SmartRefreshLayout.finishLoadMore();                 可以加载更多：设置这个后，可以加载更多
 * <p>
 * SmartRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);  想要没有更多状态显示，在这个之前必须设置为true
 * <p>
 */
public class YMLoadMore extends LinearLayout implements RefreshFooter {

    private static final String REFRESH_FOOTER_LOADING = "正在加载中...";
    private static final String REFRESH_FOOTER_NOTHING = "这是美的底线~";
    private final String TAG = "YMLoadMore";
    private final Context mContext;
    private final int mRotateAniTime = 0;     //加载后的延迟时间
    private ImageView footerLoading;
    private TextView loadingTitle;
    private RotateAnimation animation;
    protected boolean mNoMoreData = false;

    public YMLoadMore(Context context) {
        this(context, null);
    }

    public YMLoadMore(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YMLoadMore(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        //设置垂直布局
        setBackgroundColor(Color.parseColor("#00000000"));
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);             //内部居中
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;   //外部居中
        setLayoutParams(layoutParams);

        footerLoading = new ImageView(mContext);
        footerLoading.setBackgroundResource(R.mipmap.loading_more);
        LayoutParams paramsIV = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        footerLoading.setLayoutParams(paramsIV);

        loadingTitle = new TextView(mContext);
        LayoutParams paramsTV = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTV.leftMargin = SizeUtils.dp2px(10);
        loadingTitle.setLayoutParams(paramsTV);

        addView(footerLoading);
        addView(loadingTitle);

        //设置高度
        setMinimumHeight(DensityUtil.dp2px(40));

        loadingTitle.setText(REFRESH_FOOTER_LOADING);
        loadingTitle.setEnabled(false);

        //创建旋转动画
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(100);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
    }

    /**
     * 开始旋转动画
     */
    private void startAnimation() {
        footerLoading.startAnimation(animation);//开始动画
    }

    /**
     * 结束旋转动画
     */
    private void stopAnimation() {
        footerLoading.clearAnimation();
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    /**
     * 设置主题颜色，平移
     *
     * @return
     */
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    /**
     * 开始动画（开始刷新或者开始加载动画）
     *
     * @param refreshLayout HeaderHeight or FooterHeight
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        Log.e(TAG, "onStartAnimator...");
        footerLoading.setVisibility(VISIBLE);
        startAnimation();
    }

    /**
     * 动画结束
     *
     * @param refreshLayout
     * @param success       数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        Log.e(TAG, "onFinish...");
        footerLoading.setVisibility(GONE);
        stopAnimation();
        return mRotateAniTime;
    }

    /**
     * 没有更多
     *
     * @param noMoreData
     * @return true:没有更多状态存在，false：没有更多不存在
     */
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        Log.e(TAG, "setNoMoreData == " + noMoreData);
        if (noMoreData) {
            loadingTitle.setText(REFRESH_FOOTER_NOTHING);
            footerLoading.setVisibility(GONE);
            stopAnimation();
        } else {
            loadingTitle.setText(REFRESH_FOOTER_LOADING);
        }
        return noMoreData;
    }


    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (!mNoMoreData) {
            switch (newState) {
                case None:
                case Loading:
                case LoadReleased:
                    Log.e(TAG, "Loading....LoadReleased");
                    break;
            }
        }
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}
