package com.dejia.anju.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIFollowRefreshOffsetCalculator
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout

class MyPullRefresh @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : QMUIPullRefreshLayout(context, attrs, defStyleAttr) {
    private var refreshListener: RefreshListener? = null

    /**
     * 设置自定义的刷新头
     *
     * @return
     */
    override fun createRefreshView(): View {
        return CustomRefresh(context)
    }

    /**
     * @param listener
     */
    fun setRefreshListener(listener: RefreshListener?) {
        refreshListener = listener
    }

    interface RefreshListener {
        fun onRefresh()
    }

    init {
        //设置下拉后位置
        setRefreshOffsetCalculator(QMUIFollowRefreshOffsetCalculator())
        setOnPullListener(object : OnPullListener {
            override fun onMoveTarget(offset: Int) {}
            override fun onMoveRefreshView(offset: Int) {}
            override fun onRefresh() {
                if (refreshListener != null) {
                    refreshListener!!.onRefresh()
                }
            }
        })
    }
}