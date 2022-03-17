package com.dejia.anju.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIFollowRefreshOffsetCalculator;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

public class MyPullRefresh extends QMUIPullRefreshLayout {

    private RefreshListener refreshListener;

    public MyPullRefresh(Context context) {
        this(context, null);
    }

    public MyPullRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPullRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置下拉后位置
        setRefreshOffsetCalculator(new QMUIFollowRefreshOffsetCalculator());
        setOnPullListener(new OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                if (refreshListener != null) {
                    refreshListener.onRefresh();
                }
            }
        });
    }

    /**
     * 设置自定义的刷新头
     *
     * @return
     */
    @Override
    protected View createRefreshView() {
        return new CustomRefresh(getContext());
    }


    /**
     * @param listener
     */
    public void setRefreshListener(RefreshListener listener) {
        this.refreshListener = listener;
    }


    public interface RefreshListener {
        void onRefresh();
    }
}
