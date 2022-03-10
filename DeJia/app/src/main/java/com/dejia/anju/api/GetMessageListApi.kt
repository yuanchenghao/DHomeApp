package com.dejia.anju.api

import android.content.Context
import com.dejia.anju.api.base.BaseCallBackApi
import com.dejia.anju.api.base.BaseCallBackListener
import com.dejia.anju.net.NetWork
import com.dejia.anju.net.ServerData

//获取消息列表
class GetMessageListApi : BaseCallBackApi {
    override fun getCallBack(context: Context, maps: MutableMap<String, Any>, listener: BaseCallBackListener<*>) {
        NetWork.instance?.call("chat", "list", maps, context) { mData: ServerData? -> listener.onSuccess(mData) }
    }
}