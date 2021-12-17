package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.net.NetWork;
import com.dejia.anju.net.ServerCallback;
import com.dejia.anju.net.ServerData;

import java.util.Map;

//获取楼盘信息
public class GetBuildingInfoApi implements BaseCallBackApi {
    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("tour", "getBuilding", maps, new ServerCallback() {
            @Override
            public void onServerCallback(ServerData mData) {
                listener.onSuccess(mData);
            }
        });
    }
}