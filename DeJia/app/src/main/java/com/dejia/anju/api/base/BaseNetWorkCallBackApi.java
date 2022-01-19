package com.dejia.anju.api.base;

import android.content.Context;

import com.dejia.anju.net.NetWork;
import com.dejia.anju.net.ServerData;

import java.util.HashMap;

public class BaseNetWorkCallBackApi {

    private HashMap<String, Object> mHashMap;  //传值容器
    private final String mController;
    private final String mMethodName;
    private Context context;
    private String TAG = "BaseNetWorkCallBackApi";

    public BaseNetWorkCallBackApi(String controller, String methodName,Context mContext) {
        mHashMap = new HashMap<>(0);
        this.mController = controller;
        this.mMethodName = methodName;
        this.context = mContext;
    }

    /**
     * 开始请求
     *
     * @param listener
     */
    public void startCallBack(final BaseCallBackListener<ServerData> listener) {
        NetWork.getInstance().call(mController, mMethodName, mHashMap, context, mData -> listener.onSuccess(mData));
    }

    public void addData(String key, String value) {
        mHashMap.put(key, value);
    }

    public HashMap<String, Object> getHashMap() {
        return mHashMap;
    }

}
