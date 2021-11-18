package com.dejia.anju.webSocket;

import android.content.Context;

import com.dejia.anju.model.MessageBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.dejia.anju.model.Pong;
import com.dejia.anju.model.WebSocketBean;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.WebSocket;


/**
 * Created by Administrator on 2018/1/12.
 */

public class IMManager implements ReceiveMessageCallBack, MessageStatus {
    public static final String TAG = "IMManager";
    private static IMManager instance;
    private String domain = "chat.yuemei.com";
    private long expiresAt = 1544493729973L;
    private String name = "";
    private String path = "/";
    private String value = "";
    public static MessageCallBack mMessageCallBack;
    private Context mContext;
    public static UnReadMessageCallBack sUnReadMessageCallBack;

    private IMManager(Context context) {
        mContext = context;
        IMNetWork.getInstance(mContext).setMessageCallBack(this);
        IMNetWork.getInstance(mContext).setMessageStatus(this);
    }

    public static void setMessageCallBack(MessageCallBack messageCallBack) {
        mMessageCallBack = messageCallBack;
    }

    public static void setUnReadMessageCallBack(UnReadMessageCallBack unReadMessageCallBack) {
        sUnReadMessageCallBack = unReadMessageCallBack;
    }

    public static IMManager getInstance(Context context) {
        if (instance == null) {
            synchronized (IMManager.class) {
                if (instance == null) {
                    instance = new IMManager(context);

                }
            }
        }
        return instance;
    }

    public IMNetWork getIMNetInstance() {
        return IMNetWork.getInstance(mContext);
    }

    @Override
    public void messageStatus(Status status) {

    }

    @Override
    public void webSocketCallBack(Context context, WebSocket webSocket, String text) {
        String type = JSONUtil.resolveJson(text, "type");
        switch (type) {
            case "init":
                String mReplace = Util.getYuemeiInfo();
                bindWebSocket(text, mReplace);
                break;
            case "ping":
                webSocket.send(new Gson().toJson(new Pong("pong")));
                break;
            case "say":
                try {
                    WebSocketBean webSocketBean = JSONUtil.TransformSingleBean(text, WebSocketBean.class);
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(MessageBean.DataBean.class, new MessageBean.DataBean.MessageDeserializer())
                            .create();
                    MessageBean.DataBean dataBean = gson.fromJson(text, MessageBean.DataBean.class);
                    dataBean.setContent(webSocketBean.getAppcontent());
                    dataBean.setUser_avatar(webSocketBean.getFrom_client_img());
                    dataBean.setFromUserId(webSocketBean.getFrom_client_id());
                    dataBean.setFromName(webSocketBean.getFrom_client_name());
                    dataBean.handlerMessageTypeAndViewStatus();
//                    Intent messageNumChangeIntent = new Intent(MessageNumChangeReceive.ACTION);
//                    messageNumChangeIntent.putExtra(MessageNumChangeReceive.HOS_ID,webSocketBean.getHos_id()+"");
//                    context.sendBroadcast(messageNumChangeIntent);
//
//                    if ("1".equals(Cfg.loadStr(context,FinalConstant.IS_SHENHE,""))){
//                        return;
//                    }
//                    if (mMessageCallBack != null){
//                        mMessageCallBack.receiveMessage(dataBean,webSocketBean.getGroup_id()+"");
//                    }
//                    if (sUnReadMessageCallBack != null){
//                        sUnReadMessageCallBack.onUnReadMessage();
//                    }
//                    if (Utils.isBackground(context)){ //如果在后台发送广播
//                        Intent intent = new Intent(SendMessageReceiver.ACTION);
//                        intent.putExtra("message", webSocketBean.getAppcontent());
//                        intent.putExtra("groupUserId", webSocketBean.getGroupUserId() +"");
//                        context.sendBroadcast(intent);
//                    }else {
//                        if (!isChatActivityTop()){  //如果在前台其他页面（非聊天页ChatActivity）
//                            Intent intent = new Intent(FinalConstant.REFRESH_MESSAGE);
//                            intent.putExtra("message", webSocketBean.getAppcontent());
//                            intent.putExtra("time", webSocketBean.getTime());
//                            intent.putExtra("groupUserId", webSocketBean.getGroupUserId() + "");
//                            context.sendBroadcast(intent);
//                            Activity currentActivity = MyApplication.getInstance().getCurrentActivity();
//                            if (currentActivity != null && !currentActivity.isFinishing()){
//                                String content = webSocketBean.getAppcontent();
//                                int classid = webSocketBean.getClassid();
//                                String hosName = webSocketBean.getHos_name();
//                                int hosId = webSocketBean.getHos_id();
//                                String fromClientImg = webSocketBean.getFrom_client_img();
//                                String fromClientId = webSocketBean.getFrom_client_id();
//                                String pos = webSocketBean.getPos();
//                                WebSocketBean.CouponsBean coupons = webSocketBean.getCoupons();
//                                if (coupons != null && classid == 8){
//                                    String money = coupons.getMoney();
//                                    if (!EmptyUtils.isEmpty(money)&& Utils.isNumber(money)){
//                                        int moneyInt = Integer.parseInt(money);
//                                        if (moneyInt > 0){
//                                            content = money;
//                                        }
//                                    }
//                                }
//
//                                TopWindowUtils.show(currentActivity, hosName,content, fromClientImg,fromClientId,hosId+"",pos,classid);
//                                HashMap<String, String> hashMap = new HashMap<>();
//                                hashMap.put("hos_id",webSocketBean.getHos_id()+"");
//                                hashMap.put("event_others",webSocketBean.getPos());
//                            }
//
//                        }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "say_self":
                break;
            case "onfocus":
                WebSocketBean webSocketBean = JSONUtil.TransformSingleBean(text, WebSocketBean.class);
                if (mMessageCallBack != null) {
                    mMessageCallBack.onFocusCallBack(webSocketBean.getContent());
                }
                break;
        }
    }


    private boolean isChatActivityTop() {
//        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
//        return name.equals(ChatActivity.class.getName());
        return false;
    }

    private void bindWebSocket(String string, String mReplace) {
        String client_id = JSONUtil.resolveJson(string, "client_id");
        Map<String, Object> maps = new HashMap<>();
        maps.put("client_id", client_id);
        HttpHeaders headers = SignUtils.buildHttpHeaders(maps);
        HttpParams httpParams = SignUtils.buildHttpParam5(maps);
        CookieConfig.getInstance().setCookie("https", "chat.yuemei.com", "chat.yuemei.com");
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("chat.yuemei.com")
                .build();

        List<Cookie> cookies = cookieStore.loadCookie(httpUrl);
        for (Cookie cookie : cookies) {
            domain = cookie.domain();
            expiresAt = cookie.expiresAt();
            name = cookie.name();
            path = cookie.path();
            value = cookie.value();
        }
        cookieStore.removeCookie(httpUrl);
        Cookie yuemeiinfo = new Cookie.Builder()
                .name("yuemeiinfo")
                .value(mReplace)
                .domain(domain)
                .expiresAt(expiresAt)
                .path(path)
                .build();

        cookieStore.saveCookie(httpUrl, yuemeiinfo);
        List<Cookie> cookies1 = cookieStore.loadCookie(httpUrl);
        OkGo.post("https://chat.yuemei.com/chat/bind/")
                .cacheMode(CacheMode.DEFAULT)
                .headers(headers)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                    }
                });
    }
}




