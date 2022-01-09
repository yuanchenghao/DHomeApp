package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.api.base.BaseCallBackUploadApi;
import com.dejia.anju.net.NetWork;
import com.lzy.okgo.model.HttpParams;

import java.util.Map;

//上传文章图片接口
public class UpLoadUgcImageApi implements BaseCallBackUploadApi {
    @Override
    public void getCallBack(Context context, Map<String, Object> maps, HttpParams uploadPamas, final BaseCallBackListener listener) {
        NetWork.getInstance().call("ugc", "uploadImage", maps, uploadPamas, mData -> listener.onSuccess(mData));
    }
}
