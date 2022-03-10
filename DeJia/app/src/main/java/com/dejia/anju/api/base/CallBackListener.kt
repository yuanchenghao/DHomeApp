package com.dejia.anju.api.base

interface CallBackListener<T> {
    fun onSuccess(t: T)
    fun onError(t: T)
}