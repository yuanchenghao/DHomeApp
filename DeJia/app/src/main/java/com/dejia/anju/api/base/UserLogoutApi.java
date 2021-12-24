package com.dejia.anju.api.base;

import android.content.Context;

import com.dejia.anju.net.NetWork;
import com.dejia.anju.net.ServerCallback;
import com.dejia.anju.net.ServerData;

import java.util.Map;

//注销接口
public class UserLogoutApi implements BaseCallBackApi {
    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("user", "logout", maps, new ServerCallback() {
            @Override
            public void onServerCallback(ServerData mData) {
                listener.onSuccess(mData);
            }
        });
    }
}
