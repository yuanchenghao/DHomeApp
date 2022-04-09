package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.net.NetWork;

import java.util.Map;

/**
 * 文 件 名: GetShareDataApi
 * 创 建 人: 原成昊
 * 创建日期: 2022/4/2 16:42
 * 邮   箱: 188897876@qq.com
 * 修改备注：获取分享数据
 */

public class GetShareDataApi implements BaseCallBackApi {

    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("share", "getShareData", maps, context, mData -> listener.onSuccess(mData));
    }
}