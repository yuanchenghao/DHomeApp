package com.dejia.anju.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import com.dejia.anju.R;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import androidx.appcompat.widget.AppCompatImageView;


public class CustomRefresh extends AppCompatImageView implements QMUIPullRefreshLayout.IRefreshView {

    private String TAG = "CustomRefresh";
    private int mRotateAniTime;     //刷新旋转时间
    private AnimationDrawable animationDrawable;

    public CustomRefresh(Context context) {
        this(context, null);
    }

    public CustomRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    /**
     * 下拉刷新时的动作
     *
     * @param offset
     * @param total
     * @param overPull
     */
    @Override
    public void onPull(int offset, int total, int overPull) {
        clearAnimation();
        setImageResource(R.drawable.sx_three_mao_start);
        animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
    }

    /**
     * 正在刷新
     */
    @Override
    public void doRefresh() {
        clearAnimation();
        setImageResource(R.drawable.sx_three_mao);
        animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
    }

    /**
     * 刷新停止
     */
    @Override
    public void stop() {
        clearAnimation();
    }
}
