package com.dejia.anju.api

import android.content.Context
import com.dejia.anju.api.base.BaseCallBackListener
import com.dejia.anju.api.base.BaseCallBackUploadApi
import com.dejia.anju.net.NetWork.Companion.instance
import com.dejia.anju.net.ServerCallback
import com.dejia.anju.net.ServerData
import com.lzy.okgo.model.HttpParams

//上传头像接口
class UpLoadImgApi : BaseCallBackUploadApi {
    override fun getCallBack(context: Context, maps: MutableMap<String, Any>, uploadPamas: HttpParams, listener: BaseCallBackListener<*>) {
        instance!!.call("user", "uploadImg", maps, uploadPamas, context, ServerCallback { mData: ServerData? -> listener.onSuccess(mData) })
    }
}