package com.yuemei.dejia.api;

import android.content.Context;

import com.yuemei.dejia.api.base.BaseCallBackApi;
import com.yuemei.dejia.api.base.BaseCallBackListener;
import com.yuemei.dejia.net.NetWork;
import com.yuemei.dejia.net.ServerCallback;
import com.yuemei.dejia.net.ServerData;

import java.util.Map;

//获取验证码
public class GetCodeApi implements BaseCallBackApi {

    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("verificationcode", "getLoginVerificationCode", maps, new ServerCallback() {
            @Override
            public void onServerCallback(ServerData mData) {
                listener.onSuccess(mData);
            }
        });
    }
}
