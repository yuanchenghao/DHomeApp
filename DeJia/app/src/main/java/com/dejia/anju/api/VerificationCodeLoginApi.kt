package com.dejia.anju.api

import android.content.Context
import com.dejia.anju.api.base.BaseCallBackApi
import com.dejia.anju.api.base.BaseCallBackListener
import com.dejia.anju.net.NetWork.Companion.instance
import com.dejia.anju.net.ServerCallback
import com.dejia.anju.net.ServerData

//验证码登录
class VerificationCodeLoginApi : BaseCallBackApi {
    override fun getCallBack(context: Context, maps: MutableMap<String, Any>, listener: BaseCallBackListener<*>) {
        instance!!.call("user", "verificationCodeLogin", maps, context, ServerCallback { mData: ServerData? -> listener.onSuccess(mData) })
    }
}