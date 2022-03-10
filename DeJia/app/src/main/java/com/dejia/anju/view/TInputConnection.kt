package com.dejia.anju.view

import android.view.KeyEvent
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper

class TInputConnection
/**
 * Initializes a wrapper.
 *
 *
 *
 * **Caveat:** Although the system can accept `(InputConnection) null` in some
 * places, you cannot emulate such a behavior by non-null [InputConnectionWrapper] that
 * has `null` in `target`.
 *
 * @param target  the [InputConnection] to be proxied.
 * @param mutable set `true` to protect this object from being reconfigured to target
 * another [InputConnection].  Note that this is ignored while the target is `null`.
 */
(target: InputConnection?, mutable: Boolean) : InputConnectionWrapper(target, mutable) {
    private var mBackspaceListener: BackspaceListener? = null

    interface BackspaceListener {
        /**
         * @return true 代表消费了这个事件
         */
        fun onBackspace(): Boolean
    }

    /**
     * 当软键盘删除文本之前，会调用这个方法通知输入框，我们可以重写这个方法并判断是否要拦截这个删除事件。
     * 在谷歌输入法上，点击退格键的时候不会调用[.sendKeyEvent]，
     * 而是直接回调这个方法，所以也要在这个方法上做拦截；
     */
    override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
        if (mBackspaceListener != null) {
            if (mBackspaceListener!!.onBackspace()) {
                return true
            }
        }
        return super.deleteSurroundingText(beforeLength, afterLength)
    }

    fun setBackspaceListener(backspaceListener: BackspaceListener?) {
        mBackspaceListener = backspaceListener
    }

    /**
     * 当在软件盘上点击某些按钮（比如退格键，数字键，回车键等），该方法可能会被触发（取决于输入法的开发者），
     * 所以也可以重写该方法并拦截这些事件，这些事件就不会被分发到输入框了
     */
    override fun sendKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            if (mBackspaceListener != null && mBackspaceListener!!.onBackspace()) {
                return true
            }
        }
        return super.sendKeyEvent(event)
    }
}