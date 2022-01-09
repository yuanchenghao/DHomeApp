package com.dejia.anju.net;

import com.blankj.utilcode.util.AppUtils;
import com.dejia.anju.DeJiaApp;
import com.dejia.anju.R;

import static com.dejia.anju.net.EnumInterfaceType.POST;
import static com.dejia.anju.net.EnumInterfaceType.UPLOAD;

public class FinalConstant1 {
    // token
    public static final String YUEMEI_APP_KEY = "84ea4af0199361f0b15f1ac1a0d87e57";
    public static final boolean YUEMEI_DEBUG = false;
    //公共参数key
    public static final String CITY = "city";
    public static final String UID = "uid";
    public static final String APPKEY = "appkey";
    public static final String VER = "ver";
    public static final String DEVICE = "device";
    public static final String MARKET = "market";
    public static final String IMEI = "android_imei";
    public static final String ANDROID_OAID = "android_oaid";
    public static final String ANDROID_ID = "android_id";
    public static final String ONLYKEY = "only_key";
    public static final String TIME = "time";
    public static final String APPFROM = "app_from";
    public static final String ISFIRSTACTIVE = "is_first_active";
    //公共参数value
    public static final String YUEMEI_VER = AppUtils.getAppVersionName();                                       //版本号
    public static final String APP_FROM = "1";
    public static final String SESSIONID = "sessionid";
    public static final String YUEMEI_DEVICE = "android";                                                  //android还是ios（设备标识）
    public static final String MYAPP_MARKET = DeJiaApp.getContext().getString(R.string.marketv);          //自己写的渠道
    //    private static final String NETWORK_MARKET = Cfg.loadStr(DeJiaApp.getContext(), SplashActivity.NETWORK_MARKET, "");//接口返回的渠道
//    public static String YUEMEI_MARKET = TextUtils.isEmpty(NETWORK_MARKET) ? (VASDOLLY_MARKET == null ? MYAPP_MARKET : VASDOLLY_MARKET) : NETWORK_MARKET;          //市场渠道
    public static final String SEARCH_HISTORY = "search_history";
    //协议
    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    public static final String WSS = "wss";
    public static final String WS = "ws";
    //符号
    public static final String SYMBOL1 = "://";
    public static final String SYMBOL2 = "/";
    public static final String SYMBOL3 = "?";
    public static final String SYMBOL4 = "=";
    public static final String SYMBOL5 = "&";
    //域名
    public static final String TEST_BASE_URL = "172.16.10.200:8080";
    public static final String TEST_CHAT_BASE_URL = "172.16.10.200:88";
    public static final String TEST_CHAT_SOCKET_BASE_URL = "172.16.10.200:7272";
    public static final String YUEMEI_DOMAIN_NAME = ".dejiaapp.com";
    public static final String BASE_URL = "api" + YUEMEI_DOMAIN_NAME;
    public static final String BASE_API_M_URL = "m" + YUEMEI_DOMAIN_NAME;
    public static final String BASE_API_URL = "www" + YUEMEI_DOMAIN_NAME;
    public static final String BASE_SEARCH_URL = "s" + YUEMEI_DOMAIN_NAME;
    public static final String BASE_USER_URL = "user" + YUEMEI_DOMAIN_NAME;
    public static final String BASE_NEWS_URL = "chat" + YUEMEI_DOMAIN_NAME;
    public static final String BASE_SERVICE = "chats" + YUEMEI_DOMAIN_NAME;
    public static final String BASE_EMPTY = "empty" + YUEMEI_DOMAIN_NAME;
    public static final String HTML_CIRCLE = "http://"+TEST_BASE_URL +  "/vue/circleIndex/";
    //服务端地址
    public static final String API = "api";


    public static void configInterface() {
        //首页数据 HomeApi
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "verificationcode", "getLoginVerificationCode", POST);
        //激活后一分钟请求
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "appvisit", "appReceiveOneMinute", POST);
        //APP 激活记录
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "appvisit", "appreceive", POST);
        //极光推送解绑
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "message", "jPushClose", POST);
        //极光推送绑定
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "message", "jPushBind", POST);
        //消息数
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "message", "messageCount", POST);
        //审核是否隐藏按钮
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "message", "show", POST);
        //版本控制升级
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "message", "versions", POST);
        //我的粉丝（关注我的）
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "follow", "followingMe", POST);
        //关注 取消关注
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "follow", "following", POST);
        //判断用户是否关注过某数据
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "follow", "isFollowing", POST);
        //我的关注
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "follow", "myFollowing", POST);
        //修改资料上传照片
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "uploadImg", UPLOAD);
        //验证码登录
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "verificationCodeLogin", POST);
        //修改用户资料
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "setUser", POST);
        //图片上传
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "ugc", "uploadImage", UPLOAD);
        //文章提交
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "ugc", "save", POST);
        //获取用户信息
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "tour", "getBuilding", POST);
        //首页接口
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "home", "index", POST);
        //获取城市列表
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "city", "city", POST);
        //首页关注接口
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "home", "follow", POST);
        //我的文章
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "myArticle", POST);
        //获取用户信息
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "getUserInfo", POST);
        //获取用户信息
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "logout", POST);
        //一键登录
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "phoneLogin", POST);
        //赞 取消赞
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "user", "insertAgree", POST);
        //小区图片大图浏览页面
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "building", "bigImg", POST);
        //户型大图浏览接口
        NetWork.getInstance().regist(HTTP, TEST_BASE_URL, "building", "houseTypeBigImg", POST);

        //私信
        //WebSocket解绑
        NetWork.getInstance().regist(HTTP, TEST_CHAT_BASE_URL, "chat", "close", POST);
        //WebSocket绑定
        NetWork.getInstance().regist(HTTP, TEST_CHAT_BASE_URL, "chat", "bind", POST);
        //私信列表
        NetWork.getInstance().regist(HTTP, TEST_CHAT_BASE_URL, "chat", "list", POST);
        //聊天页
        NetWork.getInstance().regist(HTTP, TEST_CHAT_BASE_URL, "chat", "index", POST);
        //发私信
        NetWork.getInstance().regist(HTTP, TEST_CHAT_BASE_URL, "chat", "send", POST);
        //聊天页面获取数据
        NetWork.getInstance().regist(HTTP, TEST_CHAT_BASE_URL, "chat", "getMessage", POST);
        //私信修改未读标示
        NetWork.getInstance().regist(HTTP, TEST_CHAT_BASE_URL, "chat", "updateRead", POST);
    }
}
