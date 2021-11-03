package com.yuemei.dejia.api.base;

import com.yuemei.dejia.net.NetWork;
import com.yuemei.dejia.net.ServerCallback;
import com.yuemei.dejia.net.ServerData;

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
        NetWork.getInstance().call(mController, mMethodName, mHashMap, new ServerCallback() {
            @Override
            public void onServerCallback(ServerData mData) {
                    listener.onSuccess(mData);
            }
        });
    }

    public void addData(String key, String value) {
        mHashMap.put(key, value);
    }

    public HashMap<String, Object> getHashMap() {
        return mHashMap;
    }

}
