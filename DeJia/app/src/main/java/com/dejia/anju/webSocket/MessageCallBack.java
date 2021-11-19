package com.dejia.anju.webSocket;


import com.dejia.anju.model.MessageBean;

public interface MessageCallBack {
    void receiveMessage(MessageBean.DataBean dataBean, String group_id);

    void onFocusCallBack(String txt);
}
