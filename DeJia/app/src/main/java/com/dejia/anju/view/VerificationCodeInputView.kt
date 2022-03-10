package com.dejia.anju.view

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blankj.utilcode.util.SizeUtils
import com.dejia.anju.R
import com.dejia.anju.utils.AsteriskPasswordTransformationMethod
import com.dejia.anju.utils.Util
import java.util.*
import java.util.regex.Pattern

class VerificationCodeInputView : RelativeLayout {
    private var mContext: Context? = null
    private var onInputListener: OnInputListener? = null
    private var mLinearLayout: LinearLayout? = null
    private lateinit var mRelativeLayouts: Array<RelativeLayout?>
    private lateinit var mTextViews: Array<TextView?>
    private lateinit var mUnderLineViews: Array<View?>
    private lateinit var mCursorViews: Array<View?>
    private var mEditText: EditText? = null
    private var mPopupWindow: PopupWindow? = null
    private var valueAnimator: ValueAnimator? = null
    private val mCodes: MutableList<String> = ArrayList()

    /**
     * 输入框数量
     */
    private var mEtNumber = 0

    /**
     * 输入框类型
     */
    private var mEtInputType: VCInputType? = null

    /**
     * 输入框的宽度
     */
    private var mEtWidth = 0

    /**
     * 输入框的高度
     */
    private var mEtHeight = 0

    /**
     * 文字颜色
     */
    private var mEtTextColor = 0

    /**
     * 文字大小
     */
    private var mEtTextSize = 0f

    /**
     * 输入框间距
     */
    private var mEtSpacing = 0

    /**
     * 平分后的间距
     */
    private var mEtBisectSpacing = 0

    /**
     * 判断是否平分,默认平分
     */
    private var isBisect = false

    /**
     * 输入框宽度
     */
    private var mViewWidth = 0

    /**
     * 下划线默认颜色,焦点颜色,高度,是否展示
     */
    private var mEtUnderLineDefaultColor = 0
    private var mEtUnderLineFocusColor = 0
    private var mEtUnderLineHeight = 0
    private var mEtUnderLineShow = false

    /**
     * 光标宽高,颜色
     */
    private var mEtCursorWidth = 0
    private var mEtCursorHeight = 0
    private var mEtCursorColor = 0

    /**
     * 输入框的背景色、焦点背景色、是否有焦点背景色
     */
    private var mEtBackground = 0
    private var mEtFocusBackground = 0
    private var isFocusBackgroud = false

    enum class VCInputType {
        /**
         * 数字类型
         */
        NUMBER,

        /**
         * 数字密码
         */
        NUMBERPASSWORD,

        /**
         * 文字
         */
        TEXT,

        /**
         * 文字密码
         */
        TEXTPASSWORD
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInputView)
        mEtNumber = typedArray.getInteger(R.styleable.VerificationCodeInputView_vciv_et_number, 4)
        val inputType = typedArray.getInt(R.styleable.VerificationCodeInputView_vciv_et_inputType, VCInputType.NUMBER.ordinal)
        mEtInputType = VCInputType.values()[inputType]
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_width, SizeUtils.dp2px(40f))
        mEtHeight = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_height, SizeUtils.dp2px(40f))
        mEtTextColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_text_color, Color.BLACK)
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_text_size, SizeUtils.sp2px(14f)).toFloat()
        mEtBackground = typedArray.getResourceId(R.styleable.VerificationCodeInputView_vciv_et_background, -1)
        if (mEtBackground < 0) {
            mEtBackground = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_background, Color.WHITE)
        }
        isFocusBackgroud = typedArray.hasValue(R.styleable.VerificationCodeInputView_vciv_et_foucs_background)
        mEtFocusBackground = typedArray.getResourceId(R.styleable.VerificationCodeInputView_vciv_et_foucs_background, -1)
        if (mEtFocusBackground < 0) {
            mEtFocusBackground = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_foucs_background, Color.WHITE)
        }
        isBisect = typedArray.hasValue(R.styleable.VerificationCodeInputView_vciv_et_spacing)
        if (isBisect) {
            mEtSpacing = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_spacing, 0)
        }
        mEtCursorWidth = typedArray.getDimensionPixelOffset(R.styleable.VerificationCodeInputView_vciv_et_cursor_width, SizeUtils.dp2px(2f))
        mEtCursorHeight = typedArray.getDimensionPixelOffset(R.styleable.VerificationCodeInputView_vciv_et_cursor_height, SizeUtils.dp2px(30f))
        mEtCursorColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_cursor_color, Color.parseColor("#C3C3C3"))
        mEtUnderLineHeight = typedArray.getDimensionPixelOffset(R.styleable.VerificationCodeInputView_vciv_et_underline_height, SizeUtils.dp2px(1f))
        mEtUnderLineDefaultColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_underline_default_color, Color.parseColor("#F0F0F0"))
        mEtUnderLineFocusColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_underline_focus_color, Color.parseColor("#C3C3C3"))
        mEtUnderLineShow = typedArray.getBoolean(R.styleable.VerificationCodeInputView_vciv_et_underline_show, false)
        initView()
        typedArray.recycle()
    }

    private fun initView() {
        mRelativeLayouts = arrayOfNulls(mEtNumber)
        mTextViews = arrayOfNulls(mEtNumber)
        mUnderLineViews = arrayOfNulls(mEtNumber)
        mCursorViews = arrayOfNulls(mEtNumber)
        mLinearLayout = LinearLayout(mContext)
        mLinearLayout!!.orientation = LinearLayout.HORIZONTAL
        mLinearLayout!!.gravity = Gravity.CENTER_HORIZONTAL
        mLinearLayout!!.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        for (i in 0 until mEtNumber) {
            val relativeLayout = RelativeLayout(mContext)
            relativeLayout.layoutParams = getEtLayoutParams(i)
            setEtBackground(relativeLayout, mEtBackground)
            mRelativeLayouts[i] = relativeLayout
            val textView = TextView(mContext)
            initTextView(textView)
            relativeLayout.addView(textView)
            mTextViews[i] = textView
            val cursorView = View(mContext)
            initCursorView(cursorView)
            relativeLayout.addView(cursorView)
            mCursorViews[i] = cursorView
            if (mEtUnderLineShow) {
                val underLineView = View(mContext)
                initUnderLineView(underLineView)
                relativeLayout.addView(underLineView)
                mUnderLineViews[i] = underLineView
            }
            mLinearLayout!!.addView(relativeLayout)
        }
        addView(mLinearLayout)
        mEditText = EditText(mContext)
        initEdittext(mEditText!!)
        addView(mEditText)
        setCursorColor()
    }

    private fun initTextView(textView: TextView) {
        val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        textView.layoutParams = lp
        textView.textAlignment = TEXT_ALIGNMENT_CENTER
        textView.gravity = Gravity.CENTER
        textView.setTextColor(mEtTextColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEtTextSize)
        setInputType(textView)
        textView.setPadding(0, 0, 0, 0)
    }

    private fun initCursorView(view: View) {
        val layoutParams = LayoutParams(mEtCursorWidth, mEtCursorHeight)
        layoutParams.addRule(CENTER_IN_PARENT)
        view.layoutParams = layoutParams
    }

    private fun initUnderLineView(view: View) {
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mEtUnderLineHeight)
        layoutParams.addRule(ALIGN_PARENT_BOTTOM)
        view.layoutParams = layoutParams
        view.setBackgroundColor(mEtUnderLineDefaultColor)
    }

    private fun initEdittext(editText: EditText) {
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(ALIGN_TOP, mLinearLayout!!.id)
        layoutParams.addRule(ALIGN_BOTTOM, mLinearLayout!!.id)
        editText.layoutParams = layoutParams
        setInputType(editText)
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.setTextColor(Color.TRANSPARENT)
        editText.isCursorVisible = false
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable != null && editable.length > 0) {
                    mEditText!!.setText("")
                    code = editable.toString()
                }
            }
        })
        // 监听验证码删除按键
        editText.setOnKeyListener { view: View?, keyCode: Int, keyEvent: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN && mCodes.size > 0) {
                mCodes.removeAt(mCodes.size - 1)
                showCode()
                return@setOnKeyListener true
            }
            false
        }
        editText.setOnLongClickListener { v: View? ->
            showPaste()
            false
        }
        getEtFocus(editText)
    }

    private fun initPopupWindow() {
        mPopupWindow = PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val tv = TextView(mContext)
        tv.text = "粘贴"
        tv.textSize = 14.0f
        tv.setTextColor(Color.BLACK)
        tv.setBackgroundResource(R.drawable.vciv_paste_bg)
        tv.setPadding(30, 10, 30, 10)
        tv.setOnClickListener { v: View? ->
            code = clipboardString
            mPopupWindow!!.dismiss()
        }
        mPopupWindow!!.contentView = tv
        mPopupWindow!!.width = LinearLayout.LayoutParams.WRAP_CONTENT // 设置菜单的宽度
        mPopupWindow!!.height = LinearLayout.LayoutParams.WRAP_CONTENT
        mPopupWindow!!.isFocusable = true // 获取焦点
        mPopupWindow!!.isTouchable = true // 设置PopupWindow可触摸
        mPopupWindow!!.isOutsideTouchable = true // 设置非PopupWindow区域可触摸
        //设置点击隐藏popwindow
        val dw = ColorDrawable(Color.TRANSPARENT)
        mPopupWindow!!.setBackgroundDrawable(dw)
    }

    private fun setEtBackground(rl: RelativeLayout?, background: Int) {
        if (background > 0) {
            rl!!.setBackgroundResource(background)
        } else {
            rl!!.setBackgroundColor(background)
        }
    }

    //获取剪贴板中第一条数据
    private val clipboardString: String?
        private get() {
            val clipboardManager = mContext!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            //获取剪贴板中第一条数据
            if (clipboardManager != null && clipboardManager.hasPrimaryClip() && clipboardManager.primaryClipDescription!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                val itemAt = clipboardManager.primaryClip!!.getItemAt(0)
                if (!(itemAt == null || TextUtils.isEmpty(itemAt.text))) {
                    return itemAt.text.toString()
                }
            }
            return null
        }

    private fun getEtLayoutParams(i: Int): LinearLayout.LayoutParams {
        val layoutParams = LinearLayout.LayoutParams(mEtWidth, mEtHeight)
        var spacing: Int
        if (!isBisect) {
            spacing = mEtBisectSpacing / 2
        } else {
            spacing = mEtSpacing / 2
            //如果大于最大平分数，将设为最大值
            if (mEtSpacing > mEtBisectSpacing) {
                spacing = mEtBisectSpacing / 2
            }
        }
        if (i == 0) {
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = spacing
        } else if (i == mEtNumber - 1) {
            layoutParams.leftMargin = spacing
            layoutParams.rightMargin = 0
        } else {
            layoutParams.leftMargin = spacing
            layoutParams.rightMargin = spacing
        }
        return layoutParams
    }

    private fun setInputType(textView: TextView) {
        when (mEtInputType) {
            VCInputType.NUMBERPASSWORD -> {
                textView.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                textView.transformationMethod = AsteriskPasswordTransformationMethod()
            }
            VCInputType.TEXT -> textView.inputType = InputType.TYPE_CLASS_TEXT
            VCInputType.TEXTPASSWORD -> {
                textView.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                textView.transformationMethod = AsteriskPasswordTransformationMethod()
            }
            else -> textView.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    /**
     * 展示自定义的粘贴板
     */
    private fun showPaste() {
        //去除输入框为数字模式，但粘贴板不是数字模式
        if ((mEtInputType == VCInputType.NUMBER || mEtInputType == VCInputType.NUMBERPASSWORD) && !isNumeric(clipboardString)) {
            return
        }
        if (!TextUtils.isEmpty(clipboardString)) {
            if (mPopupWindow == null) {
                initPopupWindow()
            }
            mPopupWindow!!.showAsDropDown(mTextViews[0], 0, 20)
            Util.hideSoftKeyboard(context as Activity)
        }
    }

    /**
     * 判断粘贴板上的是不是数字
     *
     * @param str
     * @return
     */
    private fun isNumeric(str: String?): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return if (!isNum.matches()) {
            false
        } else true
    }

    private fun showCode() {
        for (i in 0 until mEtNumber) {
            val textView = mTextViews[i]
            if (mCodes.size > i) {
                textView!!.text = mCodes[i]
            } else {
                textView!!.text = ""
            }
        }
        setCursorColor() //设置高亮跟光标颜色
        setCallBack() //回调
    }

    /**
     * 设置焦点输入框底部线、光标颜色、背景色
     */
    private fun setCursorColor() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
        }
        for (i in 0 until mEtNumber) {
            val cursorView = mCursorViews[i]
            cursorView!!.setBackgroundColor(Color.TRANSPARENT)
            if (mEtUnderLineShow) {
                val underLineView = mUnderLineViews[i]
                underLineView!!.setBackgroundColor(mEtUnderLineDefaultColor)
            }
            if (isFocusBackgroud) {
                setEtBackground(mRelativeLayouts[i], mEtBackground)
            }
        }
        if (mCodes.size < mEtNumber) {
            setCursorView(mCursorViews[mCodes.size])
            if (mEtUnderLineShow) {
                mUnderLineViews[mCodes.size]!!.setBackgroundColor(mEtUnderLineFocusColor)
            }
            if (isFocusBackgroud) {
                setEtBackground(mRelativeLayouts[mCodes.size], mEtFocusBackground)
            }
        }
    }

    /**
     * 设置焦点色变换动画
     *
     * @param view
     */
    private fun setCursorView(view: View?) {
        valueAnimator = ObjectAnimator.ofInt(view, "backgroundColor", mEtCursorColor, android.R.color.transparent)
        (valueAnimator as ObjectAnimator?)?.setDuration(1500)
        (valueAnimator as ObjectAnimator?)?.setRepeatCount(-1)
        (valueAnimator as ObjectAnimator?)?.setRepeatMode(ValueAnimator.RESTART)
        (valueAnimator as ObjectAnimator?)?.setEvaluator(TypeEvaluator { fraction: Float, startValue: Any?, endValue: Any? -> if (fraction <= 0.5f) startValue else endValue })
        (valueAnimator as ObjectAnimator?)?.start()
    }

    private fun setCallBack() {
        if (onInputListener == null) {
            return
        }
        if (mCodes.size == mEtNumber) {
            onInputListener!!.onComplete(code)
        } else {
            onInputListener!!.onInput(code)
        }
    }

    /**
     * 获得验证码
     *
     * @return 验证码
     */
    private var code: String?
        private get() {
            val sb = StringBuilder()
            for (code in mCodes) {
                sb.append(code)
            }
            return sb.toString()
        }
        private set(code) {
            if (TextUtils.isEmpty(code)) {
                return
            }
            for (i in 0 until code!!.length) {
                if (mCodes.size < mEtNumber) {
                    mCodes.add(code[i].toString())
                }
            }
            showCode()
        }

    /**
     * 清空验证码
     */
    fun clearCode() {
        mCodes.clear()
        showCode()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewWidth = measuredWidth
        updateETMargin()
    }

    private fun updateETMargin() {
        //平分Margin，把第一个TextView跟最后一个TextView的间距同设为平分
        mEtBisectSpacing = (mViewWidth - mEtNumber * mEtWidth) / (mEtNumber - 1)
        for (i in 0 until mEtNumber) {
            mLinearLayout!!.getChildAt(i).layoutParams = getEtLayoutParams(i)
        }
    }

    private fun getEtFocus(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        Util.showKeyBoard(context, editText)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Util.hideSoftKeyboard(context as Activity)
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
        }
    }

    //定义回调
    interface OnInputListener {
        fun onComplete(code: String?)
        fun onInput(code: String?)
    }

    fun setOnInputListener(onInputListener: OnInputListener?) {
        this.onInputListener = onInputListener
    }
}