package com.dejia.anju.api.base

import android.content.Context

interface BaseCallBackApi {
    fun getCallBack(context: Context, maps: MutableMap<String, Any>, listener: BaseCallBackListener<*>)
}