package com.dejia.anju.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 可以回调滚动的webView
 */
public class ScrollWebView extends WebView {

    private OnScrollChangeListener mOnScrollChangeListener;

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            // webview的高度
            float webcontent = getContentHeight() * getScale();
            // 当前webview的高度
            float webnow = getHeight() + getScrollY();
            if (Math.abs(webcontent - webnow) < 1) {
                //处于底端
                mOnScrollChangeListener.onPageEnd(l, t, oldl, oldt);
            } else if (getScrollY() == 0) {
                //处于顶端
                mOnScrollChangeListener.onPageTop(l, t, oldl, oldt);
            } else {
                mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
            }

        }
    }


    /**
     * 滚动监听
     *
     * @param listener
     */
    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener {
        void onPageEnd(int l, int t, int oldl, int oldt);

        void onPageTop(int l, int t, int oldl, int oldt);

        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

}
