//package com.dejia.anju.activity
//
//import android.content.Intent
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import com.dejia.anju.MainActivity
//import com.dejia.anju.R
//import com.dejia.anju.api.base.BaseNetWorkCallBackApi
//import com.dejia.anju.base.BaseActivity
//import com.dejia.anju.utils.DialogUtils
//import com.qmuiteam.qmui.util.QMUIStatusBarHelper
//import com.zhangyue.we.x2c.ano.Xml
//
////注销账号
//class CancellationActivity : BaseActivity() {
//    //发起注销接口
//    private var postlogoutApi: BaseNetWorkCallBackApi? = null
//    private var isSheck: Boolean = false
//    private val binding: ActivityCellationBinding? = null
//    @Xml(layouts = ["activity_cancellation"])
//    override fun getLayoutId(): Int {
//        return R.layout.activity_cancellation
//    }
//
//    override fun initView() {
//        QMUIStatusBarHelper.translucent(this)
//        QMUIStatusBarHelper.setStatusBarLightMode(this)
//        val marginLayoutParams = root.layoutParams as ViewGroup.MarginLayoutParams
//        marginLayoutParams.topMargin = QMUIStatusBarHelper.getStatusbarHeight(this)
//
//        ll_back.setOnClickListener {
//            finish()
//        }
//
//        ll_check.setOnClickListener {
//            if (isSheck) {
//                iv_check?.setImageResource(R.mipmap.unchecked)
//                button?.setBackgroundResource(R.drawable.shape_cccccc_24)
//                isSheck = false;
//            } else {
//                iv_check?.setImageResource(R.mipmap.checked)
//                button?.setBackgroundResource(R.drawable.shape_gradient2_ff3535_24)
//                isSheck = true;
//            }
//        }
//
//        button.setOnClickListener {
//            if (isSheck) {
//                //弹框提示
////                DialogUtils.showCancellationDialog(mContext,
////                        "您确认已阅读并同意注销告知内容并注销账号？",
////                        "确认注销",
////                        "我再想想", object : DialogUtils.CallBack2 {
////                    override fun onYesClick() {
////                        postIslogout();
////                    }
////
////                    override fun onNoClick() {
////                        DialogUtils.closeDialog()
////                    }
////                })
//            } else {
//                //没勾选
//                Toast.makeText(mContext, "需要您先勾选同意告知内容", Toast.LENGTH_SHORT).show()
//
//            }
//        }
//        button2.setOnClickListener {
////            Utils.clearUserData()
////            MainTableActivity.mainBottomBar?.setCheckedPos(0)
//            val intent = Intent(mContext, MainActivity::class.java)
//            mContext.startActivity(intent)
//        }
//    }
//
//    override fun initData() {
////        postlogoutApi = BaseNetWorkCallBackApi(FinalConstant1.USERNEW, "logout")
////        getIslogout();
//    }
//
//    //确认注销
//    fun postIslogout() {
//        postlogoutApi!!.startCallBack {
//            if ("1".equals(it.code)) {
//                DialogUtils.closeDialog();
//                //注销成功
//                ll_layout1?.visibility = View.GONE
//                ll_layout3?.visibility = View.VISIBLE
//            } else {
//                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//
//}