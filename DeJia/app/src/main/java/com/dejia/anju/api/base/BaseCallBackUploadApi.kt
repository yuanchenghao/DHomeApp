package com.dejia.anju.api.base

import android.content.Context
import com.lzy.okgo.model.HttpParams

interface BaseCallBackUploadApi {
    fun getCallBack(context: Context, maps: MutableMap<String, Any>, uploadPamas: HttpParams, listener: BaseCallBackListener<*>)
}