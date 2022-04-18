package com.dejia.anju.net

import com.blankj.utilcode.util.AppUtils
import com.dejia.anju.DeJiaApp
import com.dejia.anju.R
import com.dejia.anju.net.NetWork.Companion.instance

object FinalConstant1 {
    // token
    const val YUEMEI_APP_KEY = "84ea4af0199361f0b15f1ac1a0d87e57"
    const val YUEMEI_DEBUG = false

    //公共参数key
    const val CITY = "city"
    const val UID = "uid"
    const val APPKEY = "appkey"
    const val VER = "ver"
    const val DEVICE = "device"
    const val MARKET = "market"
    const val IMEI = "android_imei"
    const val ANDROID_OAID = "android_oaid"
    const val ANDROID_ID = "android_id"
    const val ONLYKEY = "only_key"
    const val TIME = "time"
    const val APPFROM = "app_from"
    const val ISFIRSTACTIVE = "is_first_active"

    //公共参数value
    //版本号
    @JvmField
    val YUEMEI_VER = AppUtils.getAppVersionName()
    const val APP_FROM = "1"
    const val SESSIONID = "sessionid"

    //android还是ios（设备标识）
    const val YUEMEI_DEVICE = "android"

    //自己写的渠道
    @JvmField
    val MYAPP_MARKET = DeJiaApp.getContext().getString(R.string.marketv)

    //    private static final String NETWORK_MARKET = Cfg.loadStr(DeJiaApp.getContext(), SplashActivity.NETWORK_MARKET, "");//接口返回的渠道
    //    public static String YUEMEI_MARKET = TextUtils.isEmpty(NETWORK_MARKET) ? (VASDOLLY_MARKET == null ? MYAPP_MARKET : VASDOLLY_MARKET) : NETWORK_MARKET;          //市场渠道
    const val SEARCH_HISTORY = "search_history"

    //协议
    const val HTTPS = "https"
    const val HTTP = "http"
    const val WSS = "wss"
    const val WS = "ws"

    //符号
    const val SYMBOL1 = "://"
    const val SYMBOL2 = "/"
    const val SYMBOL3 = "?"
    const val SYMBOL4 = "="
    const val SYMBOL5 = "&"

    //域名
    const val TEST_BASE_URL = "api.dejiaapp.com"
    const val TEST_CHAT_BASE_URL = "chat.dejiaapp.com"
    const val TEST_CHAT_SOCKET_BASE_URL = "chats.dejiaapp.com"
    const val YUEMEI_DOMAIN_NAME = ".dejiaapp.com"
    const val BASE_URL = "api" + YUEMEI_DOMAIN_NAME
    const val BASE_API_M_URL = "m" + YUEMEI_DOMAIN_NAME
    const val BASE_API_URL = "www" + YUEMEI_DOMAIN_NAME
    const val BASE_SEARCH_URL = "s" + YUEMEI_DOMAIN_NAME
    const val BASE_USER_URL = "user" + YUEMEI_DOMAIN_NAME
    const val BASE_NEWS_URL = "chat" + YUEMEI_DOMAIN_NAME
    const val BASE_SERVICE = "chats" + YUEMEI_DOMAIN_NAME
    const val BASE_EMPTY = "empty" + YUEMEI_DOMAIN_NAME
    const val HTML_CIRCLE = HTTPS + SYMBOL1 + TEST_BASE_URL + "/vue/circleIndex/"
    const val HTML_ABOUT = HTTPS + SYMBOL1 + TEST_BASE_URL + "/vue/about/"

    //服务端地址
    const val API = "api"
    @JvmStatic
    fun configInterface() {
        //首页数据 HomeApi
        instance!!.regist(HTTPS, TEST_BASE_URL, "verificationcode", "getLoginVerificationCode", EnumInterfaceType.POST)
        //激活后一分钟请求
        instance!!.regist(HTTPS, TEST_BASE_URL, "appvisit", "appReceiveOneMinute", EnumInterfaceType.POST)
        //APP 激活记录
        instance!!.regist(HTTPS, TEST_BASE_URL, "appvisit", "appreceive", EnumInterfaceType.POST)
        //极光推送解绑
        instance!!.regist(HTTPS, TEST_BASE_URL, "message", "jPushClose", EnumInterfaceType.POST)
        //极光推送绑定
        instance!!.regist(HTTPS, TEST_BASE_URL, "message", "jPushBind", EnumInterfaceType.POST)
        //消息数
        instance!!.regist(HTTPS, TEST_BASE_URL, "message", "messageCount", EnumInterfaceType.POST)
        //审核是否隐藏按钮
        instance!!.regist(HTTPS, TEST_BASE_URL, "message", "show", EnumInterfaceType.POST)
        //版本控制升级
        instance!!.regist(HTTPS, TEST_BASE_URL, "message", "versions", EnumInterfaceType.POST)
        //我的粉丝（关注我的）
        instance!!.regist(HTTPS, TEST_BASE_URL, "follow", "followingMe", EnumInterfaceType.POST)
        //关注 取消关注
        instance!!.regist(HTTPS, TEST_BASE_URL, "follow", "following", EnumInterfaceType.POST)
        //判断用户是否关注过某数据
        instance!!.regist(HTTPS, TEST_BASE_URL, "follow", "isFollowing", EnumInterfaceType.POST)
        //我的关注
        instance!!.regist(HTTPS, TEST_BASE_URL, "follow", "myFollowing", EnumInterfaceType.POST)
        //修改资料上传照片
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "uploadImg", EnumInterfaceType.UPLOAD)
        //验证码登录
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "verificationCodeLogin", EnumInterfaceType.POST)
        //修改用户资料
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "setUser", EnumInterfaceType.POST)
        //图片上传
        instance!!.regist(HTTPS, TEST_BASE_URL, "ugc", "uploadImage", EnumInterfaceType.UPLOAD)
        //文章提交
        instance!!.regist(HTTPS, TEST_BASE_URL, "ugc", "save", EnumInterfaceType.POST)
        //获取用户信息
        instance!!.regist(HTTPS, TEST_BASE_URL, "tour", "getBuilding", EnumInterfaceType.POST)
        //首页接口
        instance!!.regist(HTTPS, TEST_BASE_URL, "home", "index", EnumInterfaceType.POST)
        //获取城市列表
        instance!!.regist(HTTPS, TEST_BASE_URL, "city", "city", EnumInterfaceType.POST)
        //首页关注接口
        instance!!.regist(HTTPS, TEST_BASE_URL, "home", "follow", EnumInterfaceType.POST)
        //我的文章
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "myArticle", EnumInterfaceType.POST)
        //获取用户信息
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "getUserInfo", EnumInterfaceType.POST)
        //获取用户信息
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "logout", EnumInterfaceType.POST)
        //一键登录
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "phoneLogin", EnumInterfaceType.POST)
        //赞 取消赞
        instance!!.regist(HTTPS, TEST_BASE_URL, "user", "insertAgree", EnumInterfaceType.POST)
        //小区图片大图浏览页面
        instance!!.regist(HTTPS, TEST_BASE_URL, "building", "bigImg", EnumInterfaceType.POST)
        //户型大图浏览接口
        instance!!.regist(HTTPS, TEST_BASE_URL, "building", "houseTypeBigImg", EnumInterfaceType.POST)
        //发帖前弹层
        instance!!.regist(HTTPS, TEST_BASE_URL, "ugc", "addpostalert", EnumInterfaceType.POST)
        //获取分享数据
        instance!!.regist(HTTPS, TEST_BASE_URL, "share", "getShareData", EnumInterfaceType.POST)
        //发评论
        instance!!.regist(HTTPS, TEST_BASE_URL, "ugc", "reply", EnumInterfaceType.POST)

        //私信
        //WebSocket解绑
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "close", EnumInterfaceType.POST)
        //WebSocket绑定
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "bind", EnumInterfaceType.POST)
        //私信列表
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "list", EnumInterfaceType.POST)
        //聊天页
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "index", EnumInterfaceType.POST)
        //发私信
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "send", EnumInterfaceType.POST)
        //聊天页面获取数据
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "getMessage", EnumInterfaceType.POST)
        //私信修改未读标示
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "updateRead", EnumInterfaceType.POST)
        //举报
        instance!!.regist(HTTPS, TEST_CHAT_BASE_URL, "chat", "report", EnumInterfaceType.POST)
    }
}