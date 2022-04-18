package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.net.NetWork;

import java.util.Map;

/**
 * 文 件 名: ChatReportApi
 * 创 建 人: 原成昊
 * 创建日期: 2022/4/18 23:05
 * 邮   箱: 188897876@qq.com
 * 修改备注：私信举报
 */

public class ChatReportApi implements BaseCallBackApi {

    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("chat", "report", maps, context, mData -> listener.onSuccess(mData));
    }
}
