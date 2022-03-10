package com.dejia.anju.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.dejia.anju.R
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout.IRefreshView

/**
 * 自定义下拉刷新头1
 */
class CustomRefresh @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context!!, attrs, defStyleAttr), IRefreshView {
    private val TAG = "CustomRefresh"
    private var mRotateAniTime //刷新旋转时间
            = 0
    private var animationDrawable: AnimationDrawable? = null
    protected fun initViews(attrs: AttributeSet?) {
        //设置自定义刷新属性
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CustomRefresh1, 0, 0)

        //为自定义属性赋值
        if (arr != null) {
            mRotateAniTime = arr.getInt(R.styleable.CustomRefresh1_ptr_rotate_ani_time, mRotateAniTime)
        }
    }

    /**
     * 下拉刷新时的动作
     *
     * @param offset
     * @param total
     * @param overPull
     */
    override fun onPull(offset: Int, total: Int, overPull: Int) {
        clearAnimation()
        setImageResource(R.drawable.sx_three_mao_start)
        animationDrawable = drawable as AnimationDrawable
        animationDrawable!!.start()
    }

    /**
     * 正在刷新
     */
    override fun doRefresh() {
        clearAnimation()
        setImageResource(R.drawable.sx_three_mao)
        animationDrawable = drawable as AnimationDrawable
        animationDrawable!!.start()
    }

    /**
     * 刷新停止
     */
    override fun stop() {
        clearAnimation()
    }

    init {
        initViews(attrs)
    }
}