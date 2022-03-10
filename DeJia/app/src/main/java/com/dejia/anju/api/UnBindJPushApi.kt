package com.dejia.anju.api

import android.content.Context
import com.dejia.anju.api.base.BaseCallBackApi
import com.dejia.anju.api.base.BaseCallBackListener
import com.dejia.anju.net.NetWork.Companion.instance
import com.dejia.anju.net.ServerCallback
import com.dejia.anju.net.ServerData

//解绑极光账号id
class UnBindJPushApi : BaseCallBackApi {
    override fun getCallBack(context: Context, maps: MutableMap<String, Any>, listener: BaseCallBackListener<*>) {
        instance!!.call("message", "jPushClose", maps, context, ServerCallback { mData: ServerData? -> listener.onSuccess(mData) })
    }
}