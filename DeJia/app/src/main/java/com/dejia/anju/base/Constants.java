package com.dejia.anju.base;

import com.dejia.anju.net.FinalConstant1;

//用于存放一些常量
public class Constants {
    // 正常状态
    public static final int STATE_NORMAL = 0;
    public final static int APP_STATUS_KILLED = 0; // 表示应用是被杀死后在启动的
    public final static int APP_STATUS_NORMAL = 1; // 表示应用时正常的启动流程
    public static int APP_STATUS = APP_STATUS_KILLED; // 记录App的启动状态
    //区分新老用户
    public static final String IS_NEW_OR_OLD = "isneworold";
    // 定位城市
    public static final String DWCITY = "dw_city";
    // 用户id
    public static final String UID = "id";
    public static final String SESSIONID = "ymsessionid";
    // Json code
    public static final String CODE = "code";
    // Json message
    public static final String MESSAGE = "message";
    //用户yuemeiinfo
    public static final String YUEMEIINFO = "yuemeiinfo";
    // 接口地址
    public static final String baseUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_URL + "/";
    public static final String baseUserUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_USER_URL + "/";
    public static final String baseSearchUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_SEARCH_URL + "/";
    public static final String baseTestService = FinalConstant1.WSS + FinalConstant1.SYMBOL1 + FinalConstant1.TEST_CHAT_SOCKET_BASE_URL + "/";
    //消息页面刷新
    public static final String REFRESH_MESSAGE = "com.example.mybroadcast.REFRESH_MESSAGE";

}
