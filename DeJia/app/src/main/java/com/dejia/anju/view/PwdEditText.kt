package com.dejia.anju.view

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText
import com.dejia.anju.view.TInputConnection.BackspaceListener

class PwdEditText : AppCompatEditText {
    private var inputConnection: TInputConnection? = null

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inputConnection = TInputConnection(null, true)
    }

    /**
     * 当输入法和EditText建立连接的时候会通过这个方法返回一个InputConnection。
     * 我们需要代理这个方法的父类方法生成的InputConnection并返回我们自己的代理类。
     */
    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        inputConnection!!.setTarget(super.onCreateInputConnection(outAttrs))
        return inputConnection!!
    }

    fun setBackSpaceListener(backSpaceLisetener: BackspaceListener?) {
        inputConnection!!.setBackspaceListener(backSpaceLisetener)
    }
}