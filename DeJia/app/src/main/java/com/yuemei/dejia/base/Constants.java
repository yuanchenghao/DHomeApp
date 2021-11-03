package com.yuemei.dejia.base;

//用于存放一些常量
public class Constants {
    // 正常状态
    public static final int STATE_NORMAL = 0;
    // 从后台回到前台
    public static final int STATE_BACK_TO_FRONT = 1;
    // 从前台进入后台
    public static final int STATE_FRONT_TO_BACK = 2;
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
}
