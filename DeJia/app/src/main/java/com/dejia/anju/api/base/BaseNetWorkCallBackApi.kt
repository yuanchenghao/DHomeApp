package com.dejia.anju.api.base

import android.content.Context
import com.dejia.anju.net.NetWork.Companion.instance
import com.dejia.anju.net.ServerData
import java.util.*

class BaseNetWorkCallBackApi(controller: String, methodName: String, mContext: Context) {
    val hashMap //传值容器
            : HashMap<String, Any>
    private val mController: String
    private val mMethodName: String
    private val context: Context
    private val TAG = "BaseNetWorkCallBackApi"

    /**
     * 开始请求
     *
     * @param listener
     */
    fun startCallBack(listener: BaseCallBackListener<ServerData?>) {
        instance!!.call(mController, mMethodName, hashMap, context) { mData: ServerData? -> listener.onSuccess(mData) }
    }

    fun addData(key: String, value: String) {
        hashMap[key] = value
    }

    init {
        hashMap = HashMap(0)
        mController = controller
        mMethodName = methodName
        context = mContext
    }
}