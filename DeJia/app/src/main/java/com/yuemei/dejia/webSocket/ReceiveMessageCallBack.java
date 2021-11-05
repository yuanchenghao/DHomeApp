package com.yuemei.dejia.webSocket;

import android.content.Context;

import okhttp3.WebSocket;

public interface ReceiveMessageCallBack {
    void webSocketCallBack(Context context, WebSocket webSocket, String text);
}
