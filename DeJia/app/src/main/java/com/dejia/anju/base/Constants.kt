package com.dejia.anju.base

import com.dejia.anju.net.FinalConstant1

//用于存放一些常量
object Constants {
    // 正常状态
    const val STATE_NORMAL = 0
    const val APP_STATUS_KILLED = 0 // 表示应用是被杀死后在启动的
    const val APP_STATUS_NORMAL = 1 // 表示应用时正常的启动流程
    @JvmField
    var APP_STATUS = APP_STATUS_KILLED // 记录App的启动状态

    //区分新老用户
    const val IS_NEW_OR_OLD = "isneworold"

    // 定位城市
    const val DWCITY = "dw_city"

    // 用户id
    const val UID = "id"
    const val SESSIONID = "ymsessionid"

    // Json code
    const val CODE = "code"

    // Json message
    const val MESSAGE = "message"

    //用户yuemeiinfo
    const val YUEMEIINFO = "yuemeiinfo"

    // 接口地址
    const val baseUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_URL + "/"
    const val baseUserUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_USER_URL + "/"
    const val baseSearchUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_SEARCH_URL + "/"
    const val baseTestService = FinalConstant1.WSS + FinalConstant1.SYMBOL1 + FinalConstant1.TEST_CHAT_SOCKET_BASE_URL + "/"

    //消息页面刷新
    const val REFRESH_MESSAGE = "com.example.mybroadcast.REFRESH_MESSAGE"
}