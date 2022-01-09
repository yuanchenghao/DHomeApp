package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.net.NetWork;

import java.util.Map;

//验证码登录
public class VerificationCodeLoginApi implements BaseCallBackApi {

    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("user", "verificationCodeLogin", maps, mData -> listener.onSuccess(mData));
    }
}
