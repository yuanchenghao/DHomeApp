package com.dejia.anju.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import com.dejia.anju.R

class FlowLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var mHorizontalSpacing //水平间隔
            = 0
    private var mVerticalSpacing //垂直间隔
            = 0
    var line_height = 0 //每行的高度
    private var maxLine = -1 //限制行数，为负数时无限大
    private val TAG = "FlowLayout"
    private val padding = IntArray(4)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e(TAG, "onMeasure.....")
        assert(MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED)
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        val count = childCount
        var line_height = 0
        var xpos = paddingLeft
        var ypos = paddingTop
        val childHeightMeasureSpec: Int
        childHeightMeasureSpec = if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST)
        } else {
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        }
        var currentLine = 1
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), childHeightMeasureSpec)
                val childw = child.measuredWidth
                line_height = Math.max(line_height, child.measuredHeight + mVerticalSpacing)
                if (xpos + childw > width) {
                    currentLine++
                    xpos = paddingLeft
                    if (maxLine < 0 || currentLine <= maxLine) {
                        ypos += line_height
                    }
                }
                xpos += childw + mHorizontalSpacing
            }
        }
        this.line_height = line_height
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = ypos + line_height
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (ypos + line_height < height) {
                height = ypos + line_height
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val width = r - l
        var xpos = paddingLeft
        var ypos = paddingTop
        var currentLine = 1
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                val childw = child.measuredWidth
                val childh = child.measuredHeight
                val paddingLeft = child.paddingLeft
                val paddingTop = child.paddingTop
                val paddingRight = child.paddingRight
                val paddingBottom = child.paddingBottom
                if (paddingLeft != 0) {
                    padding[0] = paddingLeft
                } else {
                    child.setPadding(padding[0], child.paddingTop, child.paddingRight, child.paddingBottom)
                }
                if (paddingTop != 0) {
                    padding[1] = paddingTop
                } else {
                    child.setPadding(child.paddingLeft, padding[1], child.paddingRight, child.paddingBottom)
                }
                if (paddingRight != 0) {
                    padding[2] = paddingRight
                } else {
                    child.setPadding(child.paddingLeft, child.paddingTop, padding[2], child.paddingBottom)
                }
                if (paddingBottom != 0) {
                    padding[3] = paddingBottom
                } else {
                    child.setPadding(child.paddingLeft, child.paddingTop, child.paddingRight, padding[3])
                }
                if (xpos + childw > width) {
                    xpos = getPaddingLeft()
                    ypos += line_height
                    currentLine++
                }
                xpos += if (maxLine < 0 || currentLine <= maxLine) {
                    child.layout(xpos, ypos, xpos + childw, ypos + childh)
                    childw + mHorizontalSpacing
                } else {
                    break
                }
            }
        }
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p != null
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return LayoutParams(p.width, p.height)
    }

    /**
     * 设置最大行数
     *
     * @param maxLine
     */
    fun setMaxLine(maxLine: Int) {
        this.maxLine = maxLine
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        try {
            mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0)
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0)
        } finally {
            a.recycle()
        }
    }
}