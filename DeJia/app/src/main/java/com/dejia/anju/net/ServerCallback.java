package com.dejia.anju.net;

public interface ServerCallback {

    //status 表示网络请求状态，bindData表示当前请求相关参数，record表示返回数据
    void onServerCallback(ServerData mData);
}
