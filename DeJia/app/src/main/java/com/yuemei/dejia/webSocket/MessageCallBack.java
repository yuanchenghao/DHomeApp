package com.yuemei.dejia.webSocket;


import com.yuemei.dejia.model.MessageBean;

/**
 * Created by 裴成浩 on 2018/1/11.
 */

public interface MessageCallBack {
    void receiveMessage(MessageBean.DataBean dataBean, String group_id);

    void onFocusCallBack(String txt);
}
