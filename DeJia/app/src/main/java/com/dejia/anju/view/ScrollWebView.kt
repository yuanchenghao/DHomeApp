package com.dejia.anju.view

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

/**
 * 可以回调滚动的webView
 */
class ScrollWebView : WebView {
    private var mOnScrollChangeListener: OnScrollChangeListener? = null

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?) : super(context!!) {}

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mOnScrollChangeListener != null) {
            // webview的高度
            val webcontent = contentHeight * scale
            // 当前webview的高度
            val webnow = (height + scrollY).toFloat()
            if (Math.abs(webcontent - webnow) < 1) {
                //处于底端
                mOnScrollChangeListener!!.onPageEnd(l, t, oldl, oldt)
            } else if (scrollY == 0) {
                //处于顶端
                mOnScrollChangeListener!!.onPageTop(l, t, oldl, oldt)
            } else {
                mOnScrollChangeListener!!.onScrollChanged(l, t, oldl, oldt)
            }
        }
    }

    /**
     * 滚动监听
     *
     * @param listener
     */
    fun setOnScrollChangeListener(listener: OnScrollChangeListener?) {
        mOnScrollChangeListener = listener
    }

    interface OnScrollChangeListener {
        fun onPageEnd(l: Int, t: Int, oldl: Int, oldt: Int)
        fun onPageTop(l: Int, t: Int, oldl: Int, oldt: Int)
        fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int)
    }
}