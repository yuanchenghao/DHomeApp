package com.dejia.anju.view

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.dejia.anju.R

//用户头像
class SelectUserAvatarPopWindow(context: Activity?) : PopupWindow(context) {
    private var mOnTextClickListener: OnTextClickListener? = null
    fun setOnTextClickListener(onTextClickListener: OnTextClickListener?) {
        mOnTextClickListener = onTextClickListener
    }

    interface OnTextClickListener {
        fun onTextClick()
        fun onTextClick2()
    }

    init {
        val layout = View.inflate(context, R.layout.item_select_user_avatar, null)
        animationStyle = R.style.AnimBottom
        isClippingEnabled = false
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(BitmapDrawable())
        isFocusable = true
        isTouchable = true
        isOutsideTouchable = true
        contentView = layout
        update()
        //取消
        val tv_cancel = layout.findViewById<TextView>(R.id.tv_cancel)
        val tv_take_photo = layout.findViewById<TextView>(R.id.tv_take_photo)
        val tv_select = layout.findViewById<TextView>(R.id.tv_select)
        val v = layout.findViewById<View>(R.id.view)
        v.setOnClickListener { dismiss() }
        tv_cancel.setOnClickListener { dismiss() }
        tv_take_photo.setOnClickListener {
            dismiss()
            if (mOnTextClickListener != null) {
                mOnTextClickListener!!.onTextClick()
            }
        }
        tv_select.setOnClickListener {
            dismiss()
            if (mOnTextClickListener != null) {
                mOnTextClickListener!!.onTextClick2()
            }
        }
    }
}