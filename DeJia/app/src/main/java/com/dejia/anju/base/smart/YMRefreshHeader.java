package com.dejia.anju.base.smart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.dejia.anju.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * 自定义SmartRefreshLayout刷新头
 */
public class YMRefreshHeader extends AppCompatImageView implements RefreshHeader {
    private final String TAG = "YMRefreshHeader";
    private int mRotateAniTime = 300;     //刷新完成后的延迟时间
    private AnimationDrawable animationDrawable;

    public YMRefreshHeader(@NonNull Context context) {
        this(context, null);
    }

    public YMRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YMRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        //设置自定义刷新属性
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRefresh1, 0, 0);

        //为自定义属性赋值
        if (arr != null) {
            mRotateAniTime = arr.getInt(R.styleable.CustomRefresh1_ptr_rotate_ani_time, mRotateAniTime);
        }

        //设置高度
        setMinimumHeight(DensityUtil.dp2px(80));
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
        animationDrawable.start();
        Log.e(TAG, "onStartAnimator.....");
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
        Log.e(TAG, "onFinish..... === " + success);

        animationDrawable.stop();
        clearAnimation();

        return mRotateAniTime;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                setImageResource(R.drawable.sx_three_mao_start);
                animationDrawable = (AnimationDrawable) getDrawable();
                if(onEventClickListener != null){
                    onEventClickListener.onPullDownToRefresh();
                }
                break;
            case ReleaseToRefresh:
                break;
            case Refreshing:
                setImageResource(R.drawable.sx_three_mao);
                animationDrawable = (AnimationDrawable) getDrawable();
                break;
        }
    }

    /**
     * 设置主题颜色
     *
     * @param colors
     */
    @Override
    public void setPrimaryColors(int... colors) {
    }

    /**
     * 尺寸定义初始化完成
     *
     * @param kernel        RefreshKernel 核心接口（用于完成高级Header功能）
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
    }

    /**
     * 移动中
     *
     * @param isDragging
     * @param percent
     * @param offset
     * @param height
     * @param maxDragHeight
     */
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
    }

    /**
     * 手指释放之后的持续动画（会连续多次调用，用于实时控制动画关键帧）
     *
     * @param refreshLayout
     * @param height        HeaderHeight or FooterHeight的高度
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//        Log.e(TAG, "onReleased..... === ");
    }

    /**
     * 水平拖动
     *
     * @param percentX  水平移动百分比
     * @param offsetX   水平移动的距离
     * @param offsetMax 水平移动最大距离
     */
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    public interface OnEventClickListener {
        //查看SKU事件回调
        void onPullDownToRefresh();
    }

    private OnEventClickListener onEventClickListener;

    public void setOnStateChangedListener(OnEventClickListener onEventClickListener) {
        this.onEventClickListener = onEventClickListener;
    }

}
