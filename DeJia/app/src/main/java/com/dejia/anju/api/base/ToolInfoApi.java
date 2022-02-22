package com.dejia.anju.api.base;

import android.content.Context;

import com.dejia.anju.net.NetWork;

import java.util.Map;

//公用模块控制参数接口
public class ToolInfoApi implements BaseCallBackApi {
    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("ugc", "addpostalert", maps, context, mData -> listener.onSuccess(mData));
    }
}
