package com.dejia.anju.view

import android.app.Activity
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.PopupWindow
import com.dejia.anju.R
import com.dejia.anju.adapter.HotCityAdapter
import com.dejia.anju.model.CityInfo
import com.dejia.anju.model.CityInfo.HotCity
import com.dejia.anju.utils.Util

class BaseCityPopWindow(private val mActivity: Activity, private val mView: View, private val mCityInfo: CityInfo?) : PopupWindow() {
    private var layout: View? = null
    private var gv_city: MyGridView? = null
    private var iv_close: ImageView? = null
    private var view_other: View? = null
    private val hotCityAdapter: HotCityAdapter? = null
    private fun setHotList() {
        if (mCityInfo != null && mCityInfo.getHot_city() != null && mCityInfo.getHot_city().size > 0) {
            gv_city!!.visibility = View.VISIBLE
            val hotCityAdapter = HotCityAdapter(mActivity,
                    mCityInfo.getHot_city())
            gv_city!!.adapter = hotCityAdapter
        } else {
            gv_city!!.visibility = View.GONE
        }
        //周边城市点击
        gv_city!!.onItemClickListener = AdapterView.OnItemClickListener { arg0: AdapterView<*>?, arg1: View?, pos: Int, arg3: Long ->
            if (onAllClickListener != null) {
                onAllClickListener!!.onAllClick(mCityInfo!!.getHot_city()[pos].getName(), mCityInfo.getHot_city()[pos])
            }
        }
    }

    /**
     * 展开
     */
    fun showPop() {
        val location = IntArray(2)
        mView.getLocationOnScreen(location)
        val rawY = location[1] //当前组件到屏幕顶端的距离
        //根据不同版本显示
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            // 适配 android 7.0
            showAtLocation(mView, Gravity.NO_GRAVITY, 0, rawY + mView.height)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                //7.1及以上系统高度动态设置
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    //7.1及以上系统高度动态设置
                    height = if (Util.isNavigationBarVisible(mActivity)) {
                        Util.getScreenSize(mActivity)[1] - rawY - mView.height - Util.getNavigationBarHeight(mActivity)
                    } else {
                        Util.getScreenSize(mActivity)[1] - rawY - mView.height
                    }
                }
            }
            showAsDropDown(mView)
        }
    }

    fun showPop(rawY: Int) {
        val location = IntArray(2)
        mView.getLocationOnScreen(location)
        //根据不同版本显示
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            // 适配 android 7.0
            showAtLocation(mView, Gravity.NO_GRAVITY, 0, rawY + mView.height)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                //7.1及以上系统高度动态设置
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    //7.1及以上系统高度动态设置
                    height = if (Util.isNavigationBarVisible(mActivity)) {
                        Util.getScreenSize(mActivity)[1] - rawY - mView.height - Util.getNavigationBarHeight(mActivity)
                    } else {
                        Util.getScreenSize(mActivity)[1] - rawY - mView.height
                    }
                }
            }
            showAtLocation(mView, Gravity.NO_GRAVITY, 0, rawY + mView.height)
        }
    }

    //城市选择回调
    private var onAllClickListener: OnAllClickListener? = null

    interface OnAllClickListener {
        fun onAllClick(city: String?, hotCity: HotCity?)
    }

    fun setOnAllClickListener(onAllClickListener: OnAllClickListener?) {
        this.onAllClickListener = onAllClickListener
    }

    init {
        if (mCityInfo != null && mCityInfo.getHot_city() != null && mCityInfo.getHot_city().size > 0) {
            layout = View.inflate(mActivity, R.layout.pop_city, null)
            animationStyle = R.style.PopupTopAnim
            gv_city = layout?.findViewById(R.id.gv_city)
            iv_close = layout?.findViewById(R.id.iv_close)
            view_other = layout?.findViewById(R.id.view_other)
            isClippingEnabled = false
            width = ViewGroup.LayoutParams.MATCH_PARENT
            //7.1以下系统高度直接设置，7.1及7.1以上的系统需要动态设置
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            //            setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9000")));
            //多加这一句，问题就解决了！这句的官方文档解释是：让窗口背景后面的任何东西变暗
//            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            isFocusable = true
            isTouchable = true
            isOutsideTouchable = true
            contentView = layout
            update()
            setHotList()
            iv_close?.setOnClickListener(View.OnClickListener { v12: View? -> dismiss() })
            view_other?.setOnClickListener(View.OnClickListener { v1: View? -> dismiss() })
        }
    }
}