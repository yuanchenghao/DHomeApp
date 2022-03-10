package com.dejia.anju.api

import android.content.Context
import com.dejia.anju.api.base.BaseCallBackApi
import com.dejia.anju.api.base.BaseCallBackListener
import com.dejia.anju.net.NetWork.Companion.instance
import com.dejia.anju.net.ServerCallback
import com.dejia.anju.net.ServerData

//关注取消关注接口
class IsFollowApi : BaseCallBackApi {
    override fun getCallBack(context: Context, maps: MutableMap<String, Any>, listener: BaseCallBackListener<*>) {
        instance!!.call("follow", "isFollowing", maps, context, ServerCallback { mData: ServerData? -> listener.onSuccess(mData) })
    }
}