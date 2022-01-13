package com.dejia.anju.api.base;

import com.dejia.anju.net.NetWork;
import com.dejia.anju.net.ServerData;

import java.util.HashMap;

public class BaseNetWorkCallBackApi {

    private HashMap<String, Object> mHashMap;  //传值容器
    private final String mController;
    private final String mMethodName;
    private String TAG = "BaseNetWorkCallBackApi";

    public BaseNetWorkCallBackApi(String controller, String methodName) {
        mHashMap = new HashMap<>();
        this.mController = controller;
        this.mMethodName = methodName;
    }

    /**
     * 开始请求
     *
     * @param listener
     */
    public void startCallBack(final BaseCallBackListener<ServerData> listener) {
        NetWork.getInstance().call(mController, mMethodName, mHashMap, mData -> listener.onSuccess(mData));
    }

    public void addData(String key, String value) {
        mHashMap.put(key, value);
    }

    public HashMap<String, Object> getHashMap() {
        return mHashMap;
    }

}
