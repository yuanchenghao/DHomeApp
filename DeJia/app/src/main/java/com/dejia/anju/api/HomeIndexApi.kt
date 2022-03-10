package com.dejia.anju.api

import android.content.Context
import com.dejia.anju.api.base.BaseCallBackApi
import com.dejia.anju.api.base.BaseCallBackListener
import com.dejia.anju.net.NetWork
import com.dejia.anju.net.ServerData

//首页数据
class HomeIndexApi : BaseCallBackApi {
    override fun getCallBack(context: Context, maps: MutableMap<String, Any>, listener: BaseCallBackListener<*>) {
        NetWork.instance?.call("home", "index", maps, context) { mData: ServerData? -> listener.onSuccess(mData) }
    }
}