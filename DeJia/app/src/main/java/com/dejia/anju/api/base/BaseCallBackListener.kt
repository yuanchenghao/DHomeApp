package com.dejia.anju.api.base

import com.dejia.anju.net.ServerData

interface BaseCallBackListener<T> {
    fun onSuccess(t: ServerData?)
}