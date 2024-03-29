package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.api.base.BaseCallBackUploadApi;
import com.dejia.anju.net.NetWork;
import com.dejia.anju.net.ServerCallback;
import com.dejia.anju.net.ServerData;
import com.lzy.okgo.model.HttpParams;

import java.util.Map;

//上传头像接口
public class UpLoadImgApi implements BaseCallBackUploadApi {
    @Override
    public void getCallBack(Context context, Map<String, Object> maps, HttpParams uploadPamas, final BaseCallBackListener listener) {
        NetWork.getInstance().call("user", "uploadImg", maps, uploadPamas, new ServerCallback() {
            @Override
            public void onServerCallback(ServerData mData) {
                listener.onSuccess(mData);
            }
        });
    }
}
