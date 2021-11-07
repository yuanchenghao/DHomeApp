//package com.yuemei.dejia.base.smart;
//
//import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
//
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import com.yuemei.dejia.R;
//import com.scwang.smartrefresh.layout.api.RefreshHeader;
//import com.scwang.smartrefresh.layout.api.RefreshKernel;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.constant.RefreshState;
//import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//
///**
// * 自定义SmartRefreshLayout刷新头
// * 二楼非二楼写在了一起
// */
//public class YMRefreshHeader2 extends RelativeLayout implements RefreshHeader {
//    private final String TAG = "YMRefreshHeader";
//    //刷新完成后的延迟时间
//    private int mRotateAniTime = 300;
//    private AnimationDrawable animationDrawable;
//    //标题文本
//    private TextView mHeaderText;
//    private ImageView iv_loading;
//    public static String isTwoLevel = "0";
//
//    public YMRefreshHeader2(@NonNull Context context) {
//        this(context, null);
//    }
//
//    public YMRefreshHeader2(@NonNull Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public YMRefreshHeader2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initViews(attrs, context);
//    }
//
//    protected void initViews(AttributeSet attrs, Context context) {
////        //设置自定义刷新属性
////        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRefresh1, 0, 0);
////
////        //为自定义属性赋值
////        if (arr != null) {
////            mRotateAniTime = arr.getInt(R.styleable.CustomRefresh1_ptr_rotate_ani_time, mRotateAniTime);
////        }
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(80));
//        params.addRule(CENTER_IN_PARENT);
//        setLayoutParams(params);
//        View headerView = View.inflate(context, R.layout.head_view, null);
//        iv_loading = headerView.findViewById(R.id.head_loading);
//        mHeaderText = headerView.findViewById(R.id.loading_title);
//        addView(headerView, params);
//        //设置高度
//        setMinimumHeight(DensityUtil.dip2px(80));
//    }
//
//    @NonNull
//    @Override
//    public View getView() {
//        return this;
//    }
//
//    /**
//     * 设置主题颜色，平移
//     *
//     * @return
//     */
//    @NonNull
//    @Override
//    public SpinnerStyle getSpinnerStyle() {
//        return SpinnerStyle.Translate;
//    }
//
//    /**
//     * 开始动画（开始刷新或者开始加载动画）
//     *
//     * @param refreshLayout HeaderHeight or FooterHeight
//     * @param height        HeaderHeight or FooterHeight
//     * @param maxDragHeight 最大拖动高度
//     */
//    @Override
//    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//        if (isTwoLevel.equals("0")) {
//            animationDrawable.start();
//            Log.e(TAG, "onStartAnimator.....");
//        }
//    }
//
//    /**
//     * 动画结束
//     *
//     * @param refreshLayout
//     * @param success       数据是否成功刷新或加载
//     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
//     */
//    @Override
//    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
//        Log.e(TAG, "onFinish..... === " + success);
//
//        if (isTwoLevel.equals("0")) {
//            animationDrawable.stop();
//            clearAnimation();
//        }
//        return mRotateAniTime;
//    }
//
//    @Override
//    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
//        if (isTwoLevel.equals("0")) {
//            switch (newState) {
//                case None:
//                case PullDownToRefresh:
//                    Log.e(TAG, "PullDownToRefresh.....");
//                    iv_loading.setImageResource(R.drawable.sx_three_mao_start);
//                    animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
//                    if (onEventClickListener != null) {
//                        onEventClickListener.onPullDownToRefresh();
//                    }
//                    break;
//                case ReleaseToRefresh:
//                    Log.e(TAG, "ReleaseToRefresh.....");
//                    break;
//                case Refreshing:
//                    Log.e(TAG, "Refreshing.....");
//                    iv_loading.setImageResource(R.drawable.sx_three_mao);
//                    animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
//                    break;
//            }
//        } else {
//            switch (newState) {
//                case None:
////                case PullDownToRefresh:
////                    mHeaderText.setText("下拉刷新");
////                    break;
////                case Refreshing:
//                case RefreshReleased:
//                    mHeaderText.setText("刷新中");
//                    break;
////                case ReleaseToRefresh:
////                    mHeaderText.setText("继续下拉有惊喜");
////                    break;
////                case ReleaseToTwoLevel:
////                    mHeaderText.setText("松手得惊喜");
////                    break;
//            }
//        }
//    }
//
//
//    public void setState() {
//        if (isTwoLevel.equals("0")) {
//            iv_loading.setVisibility(VISIBLE);
//            mHeaderText.setVisibility(GONE);
//        } else {
//            iv_loading.setVisibility(GONE);
//            mHeaderText.setVisibility(VISIBLE);
//        }
//    }
//
//
//    /**
//     * 设置主题颜色
//     *
//     * @param colors
//     */
//    @Override
//    public void setPrimaryColors(int... colors) {
//    }
//
//    /**
//     * 尺寸定义初始化完成
//     *
//     * @param kernel        RefreshKernel 核心接口（用于完成高级Header功能）
//     * @param height        HeaderHeight or FooterHeight
//     * @param maxDragHeight 最大拖动高度
//     */
//    @Override
//    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
//    }
//
//    /**
//     * 移动中
//     *
//     * @param isDragging
//     * @param percent
//     * @param offset
//     * @param height
//     * @param maxDragHeight
//     */
//    @Override
//    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
//        if (isTwoLevel.equals("1")) {
//            if(isDragging){
//                if (offset > 0 && offset < DensityUtil.getScreenHeight() * 0.15) {
//                    mHeaderText.setText("下拉刷新");
//                } else if (offset >= DensityUtil.getScreenHeight() * 0.15 && offset < DensityUtil.getScreenHeight() * 0.3) {
//                    mHeaderText.setText("继续下拉有惊喜");
//                } else if (offset >= DensityUtil.getScreenHeight() * 0.3) {
//                    mHeaderText.setText("松手得惊喜");
//                }
//            }
//        }
//    }
//
//    /**
//     * 手指释放之后的持续动画（会连续多次调用，用于实时控制动画关键帧）
//     *
//     * @param refreshLayout
//     * @param height        HeaderHeight or FooterHeight的高度
//     * @param maxDragHeight 最大拖动高度
//     */
//    @Override
//    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
////        Log.e(TAG, "onReleased..... === ");
//    }
//
//    /**
//     * 水平拖动
//     *
//     * @param percentX  水平移动百分比
//     * @param offsetX   水平移动的距离
//     * @param offsetMax 水平移动最大距离
//     */
//    @Override
//    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
//    }
//
//    @Override
//    public boolean isSupportHorizontalDrag() {
//        return false;
//    }
//
//    public interface OnEventClickListener {
//        //查看SKU事件回调
//        void onPullDownToRefresh();
//    }
//
//    private OnEventClickListener onEventClickListener;
//
//    public void setOnStateChangedListener(OnEventClickListener onEventClickListener) {
//        this.onEventClickListener = onEventClickListener;
//    }
//
//}
