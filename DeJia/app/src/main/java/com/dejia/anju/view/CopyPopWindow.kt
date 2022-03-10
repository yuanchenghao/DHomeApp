package com.dejia.anju.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.dejia.anju.R

//私信页面复制pop
class CopyPopWindow(context: Context?) : PopupWindow(context) {
    private var mOnTextClickListener: OnTextClickListener? = null
    fun setOnTextClickListener(onTextClickListener: OnTextClickListener?) {
        mOnTextClickListener = onTextClickListener
    }

    interface OnTextClickListener {
        fun onTextClick()
    }

    init {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val contentView = LayoutInflater.from(context).inflate(R.layout.chat_copy_pop, null, false)
        setContentView(contentView)
        val textView = contentView.findViewById<TextView>(R.id.copy_pop_text)
        textView.setOnClickListener {
            dismiss()
            if (mOnTextClickListener != null) {
                mOnTextClickListener!!.onTextClick()
            }
        }
    }
}