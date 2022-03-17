package com.dejia.anju.webSocket;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.dejia.anju.AppLog;
import com.dejia.anju.activity.ChatActivity;
import com.dejia.anju.brodcast.SendMessageReceiver;
import com.dejia.anju.fragment.MessageFragment;
import com.dejia.anju.fragment.RecommendFragment;
import com.dejia.anju.model.MessageBean;
import com.dejia.anju.model.Pong;
import com.dejia.anju.model.WebSocketBean;
import com.dejia.anju.net.LoggingInterceptor;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjq.gson.factory.GsonFactory;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.WebSocket;

import static com.dejia.anju.net.FinalConstant1.TEST_CHAT_BASE_URL;

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
                AppLog.i("init");
                String mReplace = Util.getYuemeiInfo();
                bindWebSocket(text, mReplace);
                break;
            case "ping":
                AppLog.i("ping");
                webSocket.send(GsonFactory.getSingletonGson().toJson(new Pong("pong")));
                break;
            case "say":
                AppLog.i("say");
                try {
                    WebSocketBean webSocketBean = JSONUtil.TransformSingleBean(text, WebSocketBean.class);
                    Gson gson = new GsonBuilder().registerTypeAdapter(MessageBean.DataBean.class, new MessageBean.DataBean.MessageDeserializer()).create();
                    MessageBean.DataBean dataBean = gson.fromJson(text, MessageBean.DataBean.class);
                    dataBean.setContent(webSocketBean.getAppcontent());
                    dataBean.setUser_avatar(webSocketBean.getFrom_client_img());
                    dataBean.setFromUserId(webSocketBean.getFrom_client_id());
                    dataBean.setFromName(webSocketBean.getFrom_client_name());
                    dataBean.setContent(webSocketBean.getContent());
                    dataBean.handlerMessageTypeAndViewStatus();
                    if (mMessageCallBack != null) {
                        mMessageCallBack.receiveMessage(dataBean, webSocketBean.getGroup_id() + "");
                    }
                    if (sUnReadMessageCallBack != null) {
                        sUnReadMessageCallBack.onUnReadMessage();
                    }
                    if (Util.isBackground(context)) {
                        //如果程序处于后台
                        Intent intent = new Intent(SendMessageReceiver.ACTION);
                        //这个id不确定是不是需要传到聊天页的id
                        intent.putExtra("id", webSocketBean.getClassid());
                        intent.putExtra("groupUserId", webSocketBean.getGroupUserId() + "");
                        context.sendBroadcast(intent);
                    } else {
                        //如果程序处于前台
//                        if (!isChatActivityTop()) {  //如果在前台其他页面（非聊天页ChatActivity）
//                            Intent intent = new Intent(Constants.REFRESH_MESSAGE);
//                            intent.putExtra("message", webSocketBean.getAppcontent());
//                            intent.putExtra("time", webSocketBean.getTime());
//                            intent.putExtra("groupUserId", webSocketBean.getGroupUserId() + "");
//                            context.sendBroadcast(intent);
//                            Activity currentActivity = com.dejia.anju.mannger.ActivityManager.getInstance().getCurrentActivity();
//                            if (currentActivity != null && !currentActivity.isFinishing()) {
//                                String content = webSocketBean.getAppcontent();
//                                int classid = webSocketBean.getClassid();
//                                String hosName = webSocketBean.getHos_name();
//                                int hosId = webSocketBean.getHos_id();
//                                String fromClientImg = webSocketBean.getFrom_client_img();
//                                String fromClientId = webSocketBean.getFrom_client_id();
//                                String pos = webSocketBean.getPos();
//                                TopWindowUtils.show(currentActivity, hosName, content);
//                            }
//                        }
                    }
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
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(ChatActivity.class.getName());
    }

    private void bindWebSocket(String string, String mReplace) {
        String client_id = JSONUtil.resolveJson(string, "client_id");
        Map<String, Object> maps = new HashMap<>(0);
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
        OkGo.post("http://" +TEST_CHAT_BASE_URL+"/chat/bind/")
                .cacheMode(CacheMode.DEFAULT)
                .headers(headers)
                .params(httpParams)
                .addInterceptor(new LoggingInterceptor())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        AppLog.i("bind--------" + s);
                    }
                });
    }
}




