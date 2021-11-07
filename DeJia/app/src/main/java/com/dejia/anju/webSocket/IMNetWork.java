package com.dejia.anju.webSocket;

import android.content.Context;

import com.dejia.anju.MainActivity;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class IMNetWork implements NetStatus {
    public static final String TAG = "IMNetWork";
    private static IMNetWork instance;
    private WebSocket mWebSocket;
    private ReceiveMessageCallBack mMessageCallBack;
    private MessageStatus mMessageStatus;
    private int reconnectNum;
    private int netStatus;
    private Context mContext;
    private boolean flag = false;
    private boolean mStatus = false;
    private WsManager wsManager;

    public void setMessageCallBack(ReceiveMessageCallBack messageCallBack) {
        mMessageCallBack = messageCallBack;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        mMessageStatus = messageStatus;
    }

    private IMNetWork(Context context) {
        mContext = context;
        MainActivity.setNetStatus(this);
    }

    public static IMNetWork getInstance(Context context) {
        if (instance == null) {
            synchronized (IMNetWork.class) {
                if (instance == null) {
                    instance = new IMNetWork(context);
                }
            }
        }
        return instance;
    }

    public void connWebSocket(String url) {
        if (wsManager == null){
            wsManager = new WsManager.Builder(mContext)
                    .client(new OkHttpClient().newBuilder().pingInterval(15, TimeUnit.SECONDS).retryOnConnectionFailure(true).build()).needReconnect(true).wsUrl(url).build();

            wsManager.setWsStatusListener(new WsStatusListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    mWebSocket = webSocket;
                    mMessageStatus.messageStatus(Status.CONNECTED);
                    reconnectNum = 0;
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    mMessageCallBack.webSocketCallBack(mContext, webSocket, text);
                }

                @Override
                public void onMessage(ByteString bytes) {
                    super.onMessage(bytes);
                }

                @Override
                public void onReconnect() {
                    super.onReconnect();
                }

                @Override
                public void onClosing(int code, String reason) {
                }

                @Override
                public void onClosed(int code, String reason) {
                }

                @Override
                public void onFailure(Throwable t, Response response) {
                    super.onFailure(t, response);
                    mMessageStatus.messageStatus(Status.ERROR);
                }
            });

            wsManager.startConnect();
        }
    }

    public void closeWebSocket() {
        if (wsManager != null) {
            wsManager.stopConnect();
            wsManager = null;
        }
    }

    //重连方法
    private void repeatConnectWebSocket() {
        //关闭当前连接
        closeWebSocket();
        if (netStatus != -1) {
            connWebSocket("wss://chats.yuemei.com/");
        }

    }

    @Override
    public void netStatus(int status) {
        netStatus = status;
        if (netStatus == -1) {
            mStatus = true;
        }
        if (netStatus == 1 || netStatus == 0) {
            if (mStatus) {
                repeatConnectWebSocket();
            }
        }


    }
}
