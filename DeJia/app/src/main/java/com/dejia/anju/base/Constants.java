package com.dejia.anju.base;

import com.dejia.anju.net.FinalConstant1;

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
    //用户yuemeiinfo
    public static final String YUEMEIINFO = "yuemeiinfo";

    // 接口地址
    public static final String baseUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_URL + "/";
    public static final String baseUserUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_USER_URL + "/";
    public static final String baseSearchUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_SEARCH_URL + "/";
    public static final String baseNewsUrl = FinalConstant1.HTTPS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_NEWS_URL + "/";
    public static final String baseService = FinalConstant1.WSS + FinalConstant1.SYMBOL1 + FinalConstant1.BASE_SERVICE + "/";
    //版本控制
    public static final String VER = FinalConstant1.YUEMEI_VER;
    //设备标识
    public static final String DEVICE = "device/" + FinalConstant1.YUEMEI_DEVICE + "/";
    public static final String BIND = "chat/bind/";
    // 隐私声明
    public static final String USERAGREMMENT = baseUrl + VER + "/homenew/yssm/";
    //悦美用户使用协议
    public static final String YUEMEIUSERAGREMMENT = baseUrl + VER + "/homenew/secrecypact/";
    // 图形验证码
    public static final String TUXINGCODE = baseUserUrl + "user/getCaptcha/code/9/";
    //京东支付
    public static final String JDPAYURL = baseUserUrl + "alipay/pay/";
    //京东支付成功
    public static final String JDPAYSUCCESSURL = baseUserUrl + "jdpay/appsuccess/id/";
    //京东支付失败
    public static final String JDPAYFAILDURL = baseUserUrl + "jdpay/appfaild/id/";
    //分期支付成功
    public static final String FENQIPAYSUCCESSURL = baseUserUrl + "lebaifen/appsuccess/id/";
    //分期支付失败
    public static final String FENQIPAYFAILDURL = baseUserUrl + "lebaifen/appfaild/id/";
    // 名医更多
    public static final String MINGYI_MORE = baseUrl + VER + "/doctor/mingyi/";
    // 搜索文字显示接口
    public static final String SEARCH_SHOW_DATA = baseSearchUrl + "home/searchk/ver/" + VER + "/";
    // 活动首页
    public static final String HUODONG_WEB_HOME = baseUrl + VER + "/forum/active/";
    //颜值币规则
    public static final String JIFEN_GUIZE_ = "/usernew/scoreexplain/";
    //关于医生
    public static final String ABOUTDOC = baseUrl + VER + "/doctor/userintroduc/";
    //乐百分协议
    public static final String LEBAIFEN = baseUrl + VER + "/service/repayment/";
    // 我的颜值币
    public static final String MY_POINTS_URL = baseUrl + VER + "/usernew/myscore/";
    // 我的经验
    public static final String MY_EXPENCE = baseUrl + VER + "/usernew/myexp/";
    //低价超市web接口
    public static final String CHEAP_SUPER_WEB = "/tao/coopextra/";
    // 保险弹窗web
    public static final String TAO_BAOXIAN = baseUrl + VER + "/service/insurepro/";
    //排行榜规则
    public static final String PAIHANGBANG_GUIZE = baseUrl + VER + "/rank/rule/";
    // 淘整形详细页服务
    public static final String TAO_SERVICE_DETAIL = baseUrl + VER + "/tao/service/";
    // 填码得劵
    public static final String TIANMADEJUAN = baseUrl + VER + "/coupons/changecoupons/";
    // 我的代金劵
    public static final String MY_DAIJINJUAN = baseUrl + VER + "/coupons/mycoupons/";
    // 退款成功页
    public static final String MONEYBACK_SUECC = baseUrl + VER + "/order/applysuccess/";
    // 预约流程
    public static final String YUYUE_LIUCHENG = baseUrl + VER + "/tao/appointment/";
    //推荐
    public static final String TUI_JIAN = baseUrl + VER + "/tao/taoinforecommendtao/";
    // 环境
    public static final String YUYUE_ENVIRONMENT = baseUrl + VER + "/tao/environment/";
    //科普
    public static final String YUYUE_KEPU = baseUrl + VER + "/tao/baike/";
    // 淘整形内整形日记
    public static final String TAO_DELY = baseUrl + VER + "/tao/taopost/";
    // 颜值币详细页
    public static final String POINTS_DETAIL = baseUrl + VER + "/usernew/scorelist/";
    // 赔付依据
    public static final String PEIFU_BASIC = baseUrl + VER + "/tao/detailshow/";
    // 退款说明
    public static final String MONEY_BACK_INFO = baseUrl + VER + "/usernew/refundexplain/";
    // 银行转账说明
    public static final String BANK_TRANSFER = baseUrl + VER + "/order/transfer/";
    // 银行问题说明中心
    public static final String BANK_ZHIFU_HELP = baseUrl + VER + "/usernew/helpcenter/";
    // 百科首页
    public static final String BAIKE_HOME = baseUrl + VER + "/baike/allpart/";
    // 百科四级页面
    public static final String BAIKE_FOR = baseUrl + VER + "/baike/fourpart/" + "id/";
    // 银行限额
    public static final String BANK_ZHIFU_LIMIT = baseUrl + VER + "/usernew/transferexplain/";
    // 经验值明细
    public static final String EXP_DETAIL = baseUrl + VER + "/usernew/explist/";
    // 颜值币说明
    public static final String POINTS_SHENGIMG = baseUrl + VER + "/usernew/scoreexplain/";
    // 经验值说明
    public static final String JINGYAN_EXP = baseUrl + VER + "/usernew/expshuom/";
    //搜索完成后的接口
    public static final String SEARCH_613 = baseSearchUrl + "home/index613/";
    // 我的订单列表
    public static final String MY_ORDER = baseUrl + VER + "/usernew/myordernew/";
    //投票页面
    public static final String FORUM_VOTE = baseUrl + VER + "/forum/vote/";
    //我的日记
    public static final String MY_DIARY = baseUrl + VER + "/usernew/mydiary/";
    //我的足迹
    public static final String MY_BROWSELOG = baseUrl + VER + "/usernew/browselog/";
    // 订单详情
    public static final String ORDER_DETAIL = baseUrl + VER + "/usernew/orderdetail/";
    // 订单追踪
    public static final String ORDER_SEARCH = baseUrl + VER + "/usernew/ordertrack/";
    // 医院详情页
    public static final String HOSPTIPL_DETAIL = baseUrl + VER + "/hospital/index/";
    // 专题分享
    public static final String ZHUANTI_SHARE = baseUserUrl + "share/ztshare/id/";
    //专题详情
    public static final String ZHUANTI_DETAIL = baseUserUrl + "home/taozt/id/";
    // 赔付声明
    public static final String COMPENSTATE = baseUrl + VER + "/tao/compensate/";
    // 退款声明
    public static final String MONEY_BACK_DES = baseUrl + VER + "/usernew/refundexplain/";
    // 医院地图地址
    public static final String HOSPITALMAP = baseUrl + VER + "/hospital/hospitalmap/";
    // 评论列表
    public static final String PL_WEB = baseUrl + VER + "/usernew/myreplypost/";
    // 专家详情页接口
    public static final String DOC_DETAIL = baseUrl + VER + "/doctor/userinfo/";
    // 淘整形详细页
    public static final String TAO_DETAIL = baseUrl + VER + "/tao/taoinfo/";
    // 淘整形预约页order
    public static final String TAO_ORDER = baseUrl + VER + "/tao/order/";
    // 我想变美
    public static final String WANTBEAUTIFUL = baseUrl + VER + "/usernew/beautify/";
    // 应用热门推荐
    public static final String HOTAPP = baseUrl + VER + "/homenew/link/";
    // 改退规则
    public static final String GAITUI = baseUrl + VER + "/usernew/refundexplain/";
    // 关于悦美
    public static final String ABOUTURL = baseUrl + VER + "/homenew/aboutym/";
    //首页弹层
    public static final String HOMENEW_TANCENG = baseUrl + VER + "/homenew/alert/";
    // 消息界面判断的三种类型
    public static final String MESSAGEURL = baseUrl + VER + "/message/messagelist/";
    //用户提问接口
    public static final String QUESTION_MESSAGE = baseUrl + VER + "/taoask/ask/";
    //用户解答接口
    public static final String ANSWER_MESSAGE = baseUrl + VER + "/taoask/answer/";
    //签到页面
    public static final String SIGN_WEB = "/task/checkinpage/";
    public static final String SIGN_WEB2 = "/facevideo/getvideofilter/";
    //任务中心
    public static final String TASK = "/task/taskhub/";
    //Homeperson 专用用户id
    public static final String HOME_PERSON_UID = "home_uid";
    //是否新用户 0是老用户 1是新用户
    public static final String ISSHOW = "isshow";
    // 屏幕的宽
    public static final String WINDOWS_W = "windowsWight";
    // 屏幕的高
    public static final String WINDOWS_H = "windowsHight";
    // 签到提示
    public static final String SWITTCH = "switch1";
    // 用户昵称
    public static final String UNAME = "nickname";
    // 用户省份
    public static final String UPROVINCE = "province";
    // 用户城市
    public static final String UCITY = "city";
    // 用户电话
    public static final String UPHONE = "phone";
    //用户登录电话
    public static final String ULOGINPHONE = "loginphone";
    //用户是否绑定
    public static final String USERISPHONE = "isphone";
    // 淘整形城市
    public static final String TAOCITY = "taocity";
    //定位省
    public static final String DWPROVINCE = "dw_province";
    // 用户头像
    public static final String UHEADIMG = "headimg";
    // 用户性别
    public static final String USEX = "sex";
    //用户生日
    public static final String UBIRTHDAY = "birthday";
    //底部导航
    public static final String START_TABBAR = "start_tabbar";
    //用户group_id
    public static final String GROUP_ID = "group_id";
    public static final String USER_MORE = "user_more";
    //总消息
    public static final String ZONG_ID = "zong_id";
    //私信id
    public static final String SIXIN_ID = "sixin_id";
    //评论
    public static final String PINGLUN_ID = "pinglun_id";
    //赞
    public static final String ZAN_ID = "zan_id";
    //关注
    public static final String GUANZHU_ID = "guanzhu_id";
    //通知
    public static final String NOTICE_ID = "notice_id";
    // 签到开关
    public static final String SIGN_FLAG = "sign_flag";  //1 开启  0 关闭
    //今天是否签到
    public static final String IS_SIGN = "is_sign";  //1 已签  0 未签

    // 签到是否开启本地推送签到后的日期
    public static final String CURRENTDATE = "current_date";

    //搜索页面本地搜索数据数据库
    public static final String YUEMEIWORDS = "yuemeiwords";

    //日记页面咨询提示
    public static final String DIARIES_TIP_TAG = "diaries_tip_tag";

    public static final String ISFIRST2 = "first2";
    //签到谈层保存
    public static final String TANCENG = "tanceng1";
    public static final String TISHI = "tishi";
    // 唯一标识
    public static final String UUID = "uuid";
    public static final String BD_USERID = "baidu_uid";
    public static final String BD_CHID = "baidu_channelId";
    public static final String TIME_ID = "time_id";
    public static final String LOCATING_CITY = "locating_city";
    public static final String MESSAGE_ARR = "message_arr";
    public static final String MY_TIP = "my_tip";
    public static final String GMV = "gmv";
    //搜索医院曝光
    public static final String SEARCH_TAO_BG_DOCTORS = "SEARCH_TAO_BG_DOCTORS";

    public static final String YUEMEI_ITEM = "yuemei_item";

    //定位所在的经纬度
    public static final String DW_LATITUDE = "dw_latitude";
    public static final String DW_LONGITUDE = "dw_longitude";

    //购物车数量
    public static final String CART_NUMBER = "cart_number";

    //消息页面刷新
    public static final String REFRESH_MESSAGE = "com.example.mybroadcast.REFRESH_MESSAGE";
    //我的问答页连接
    public static final String QUESTION_ANSWER_URL = baseUrl + VER + "/taoask/myask/";
    //拼团页连接
    public static final String SPELT_URL = baseUrl + VER + "/tao/group/";
    //会员中心
    public static final String PLUS_VIP_URL = baseUrl + VER + "/member/home/";
    //七牛存储
    public static final String QINIUTOKEN = "qiniutoken";
    //非wifi下是否播放视频
    public static final String NOT_WIFI_PLAY = "not_wifi_play";
    //视频流页面上滑提示显示隐藏
    public static final String VIDEO_LIST_SLIDE_PROMPT = "video_list_slide_prompt";
    //淘整形
    public static final String TAO_TWO = baseUrl + VER + "/homenew/taonew/";
    //变美助手
    public static final String TAO_TOOL = baseUrl + VER + "/homenew/tool/";
    //钱包协议
    public static final String WALLET_HTTP = baseUrl + VER + "/wallet/protocol/flag/1/";

    //颜值币商城
    public static final String YANZHIBI = baseUrl + VER + "/task/yancoinmall/";
    //砍价
    public static final String BARGAINLIST = baseUrl + VER + "/bargaining/channel/";
    //大促浮层是否关闭
    public static final String DACU_FLOAT_CLOAS = "dacu_float_close";
    //新人红包是否关闭
    public static final String NEWUSER_CLOSE = "newuser_close";
    //对比咨询数量
    public static final String MESSAGE_NUM = "message_num";
    public static final String CURRENT_DATE = "current_date";

    //日记本详情右下角医生显示和隐藏
    public static final String DIARIES_POST_DOC_SHOW = "diaries_post_doc_show";

    //首页底部提示
    public static String HOME_BOTOOM_TIP_SHOW = "home_botoom_tip_show";

    //是否是一键登录中的第三方登录
    public static final String IS_AUTO_THIRD = "is_auto_third";

    //登录页面曝光
    public static final String EXPOSURE_LOGIN = "exposure_login";

    //首页样式
    public static final String HOME_STYLE = "home_style";

    //潜客
    public static final String POTENTIAL_CUSTOMER = "potential_customer";

    //无网络状态 1代表无网  0 有网
    public static final String NO_NETWORK = "no_network";

    //私信页bottom提示是否显示
    public static final String CHAT_BOTTOM_ISVISIBLE = "chat_bottom_isvisible";
    //私信页的日期
    public static final String CHAT_DATA = "chat_data";

    //医院是web还是原生
    public static final String IS_WEBDOCTOR = "is_webdoctor";

    //首页缓存
    public static final String HOMEJSON = "homejson";

    //新首页缓存
    public static final String HOME_NEW_JSON = "home_new_json";

    public static final String HOME_PERSON_CALENDER = baseUrl + VER + "/calendar/index/";

    //启动页面视频
    public static final String IS_START_VIDEO = "is_start_video";

    //项目对比详情页
    public static final String TAOPK = baseUrl + VER + "/taopk/taopk/";

    //视频面诊匹配筛选H5页
    public static final String VIDEOFILTER = baseUrl + VER + "/facevideo/getvideofilter/";
    //匹配成功展示弹层H5
    public static final String HITDOCTORSINFO = baseUrl + VER + "/facevideo/hitdoctorsinfo/";
    //面诊结束评分页
    public static final String VIDEOSCORE = baseUrl + VER + "/facevideo/videoscore/";
    //红包使用规则
    public static final String REDENVELOPES = baseUrl + VER + "/coupons/explain/";
    //首页角标
    public static final String HOME_INDEX = "home_index";
    //第二个签角标
    public static final String TAO_INDEX = "tao_index";
    //第3个签角标
    public static final String COMMUNITY_INDEX = "community_index";
    //第4个签角标
    public static final String MESSAGE_INDEX = "message_index";
    //第5个签角标
    public static final String MY_INDEX = "my_index";
    //设置页同步多问 0 未开启，1开启
    public static final String MORE_ASK = "more_ask";
    //设置页定向推送 0 未开启，1开启
    public static final String DIRECTIONAL_PUSH = "directional_push";
    public static final String IS_SHENHE = "isshenhe";//是否是审核状态，1是
    public static final String PHONE_LOGIN_STYLE = "phone_login_style";//一键登陆样式标识 1：底部 ；2：中间
    public static final String HOME_SHOW_LOGIN = "home_show_login";
    public static final String YOUTH_MODEL_SHOW = "youth_model_show";//青少年模式 0 没点过已满18  1 点过
    public static final String MAGIC_FACE_SHOW = "magic_face_show";//0没显示 1显示过
    public static final String VIDEO_BIG_PROMOTION = "video_big_promotion";
    public static final String NEED_SHOW_VIDEO_BIG_PROMOTION = "need_show_video_big_promotion";
    public static final String VIDEO_RED_ENVELOPES = "video_red_envelopes";
    //支付成功页
    public static final String ORDERSUCCESS = baseUrl + VER + "/order/paysuccess/";

    public static final String SHUZI_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJI3JOcYW+4/IubAOWYFI/5fyZPozEUs2JXHKt5rdarcpUOUT0P3dCUXI5jBklqB1DHKL5zLbA8V1tGNbMT+FCsCAwEAAQ==";

    public static final String SHUZILM = "shuzilm";

    public static final String IS_PROMOTION  = "is_promotion";//是否值大促 1是
}
