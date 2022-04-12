package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.net.NetWork;

import java.util.Map;

//关注取消关注接口
public class FollowAndCancelApi implements BaseCallBackApi {
    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("follow", "following", maps, context, mData -> listener.onSuccess(mData));
    }
}
