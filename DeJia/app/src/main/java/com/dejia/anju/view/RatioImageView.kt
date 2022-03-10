package com.dejia.anju.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.dejia.anju.R

/**
 * 文 件 名: RatioImageView
 * 创 建 人: 原成昊
 * 创建日期: 2019-12-01 20:51
 * 邮   箱: 188897876@qq.com
 * 修改备注：一个能保持比例的Imageview
 */
class RatioImageView : AppCompatImageView {
    private var mRatio = 0f

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(context, attrs)
    }

    private fun getAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView)
            mRatio = mTypedArray.getFloat(R.styleable.RatioImageView_ratio, 0f)
            mTypedArray.recycle()
        }
    }

    var ratio: Float
        get() = mRatio
        set(ratio) {
            mRatio = ratio
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mRatio > 0) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            var height = MeasureSpec.getSize(heightMeasureSpec)

            // 现在只支持固定宽度
            if (width > 0 && mRatio > 0) {
                height = (width.toFloat() * mRatio).toInt()
            }
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}