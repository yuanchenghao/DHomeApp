package com.dejia.anju.api.base;

import android.content.Context;

import java.util.Map;

public interface BaseCallBackApi {
    void getCallBack(Context context, Map<String, Object> maps, BaseCallBackListener listener);
}
