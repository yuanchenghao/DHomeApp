package com.dejia.anju.api.base;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;

import java.util.Map;


public interface BaseCallBackUploadApi {
    void getCallBack(Context context, Map<String, Object> maps, HttpParams uploadPamas, BaseCallBackListener listener);
}
