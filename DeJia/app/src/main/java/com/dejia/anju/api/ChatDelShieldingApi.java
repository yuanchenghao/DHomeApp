package com.dejia.anju.api;

import android.content.Context;

import com.dejia.anju.api.base.BaseCallBackApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.net.NetWork;

import java.util.Map;

/**
 * 文 件 名: ChatDelShieldingApi
 * 创 建 人: 原成昊
 * 创建日期: 2022/5/14 23:35
 * 邮   箱: 188897876@qq.com
 * 修改备注：取消屏蔽
 */

public class ChatDelShieldingApi implements BaseCallBackApi {

    @Override
    public void getCallBack(Context context, Map<String, Object> maps, final BaseCallBackListener listener) {
        NetWork.getInstance().call("chat", "delShielding", maps, context, mData -> listener.onSuccess(mData));
    }
}
