package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.adapter.ChatAdapter;
import com.dejia.anju.api.ChatIndexApi;
import com.dejia.anju.api.ChatSendApi;
import com.dejia.anju.api.ChatUpdateReadApi;
import com.dejia.anju.api.GetMessageApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.ChatIndexInfo;
import com.dejia.anju.model.ChatUpdateReadInfo;
import com.dejia.anju.model.MessageBean;
import com.dejia.anju.model.NoreadAndChatidInfo;
import com.dejia.anju.model.WebSocketBean;
import com.dejia.anju.net.NetWork;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.SoftKeyBoardListener;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.PullLoadMoreRecyclerView;
import com.dejia.anju.webSocket.IMManager;
import com.dejia.anju.webSocket.MessageCallBack;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.CookieStore;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Response;

//私信页面
public class ChatActivity extends BaseActivity implements View.OnClickListener, MessageCallBack {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.content_lv)
    PullLoadMoreRecyclerView content_lv;
    @BindView(R.id.ll_input)
    LinearLayout ll_input;
    @BindView(R.id.mess_et)
    EditText mess_et;
    @BindView(R.id.fl_root)
    FrameLayout fl_root;
    private boolean isFlag = false;
    //页码
    private String page = "2";
    //聊天对象id
    private String mId;
    private String mGroupId;
    //获取页面信息
    private ChatIndexApi chatIndexApi;
    //获取私信消息
    private GetMessageApi getMessageApi;
    //发私信
    private ChatSendApi chatSendApi;
    //修改私信未读消息数
    private ChatUpdateReadApi chatUpdateReadApi;
    //默认输入框的高度
    private int INPUT_HEIGHT = 51;
    public List<MessageBean.DataBean> tblist = new ArrayList<>();
    public List<MessageBean.DataBean> pagelist = new ArrayList<>();
    private ArrayList<MessageBean.DataBean> mDataList = new ArrayList<>();
    //最后一条消息时间
    private String mDateTime;
    private ChatAdapter chatAdapter;
    public static final int SEND_OK = 0x1110;
    public static final int REFRESH = 0x0011;
    public static final int RECERIVE_OK = 0x1111;
    public static final int PULL_TO_REFRESH_DOWN = 0x0111;
    public int position; //加载滚动刷新位置
    private MessageBean.DataBean dataBean;
    private String domain = "dejiainfo";
    private long expiresAt = 1544493729973L;
    private String name = "";
    private String path = "/";
    private String value = "";
    private Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<ChatActivity> mActivity;

        private MyHandler(ChatActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChatActivity theActivity = mActivity.get();
            if (theActivity != null) {
                switch (msg.what) {
                    case REFRESH:
                        //刷新
                        if (theActivity.chatAdapter != null) {
                            theActivity.chatAdapter.notifyDataSetChanged();
                            int position = theActivity.chatAdapter.getItemCount() - 1 < 0 ? 0 : theActivity.chatAdapter.getItemCount() - 1;
                        }
                        break;
                    case SEND_OK:
                        //发送之后
                        if (theActivity.chatAdapter != null) {
                            theActivity.chatAdapter.notifyItemInserted(theActivity.tblist.size() - 1);
                            theActivity.content_lv.smoothScrollToPosition(theActivity.tblist.size() - 1);
                        }
                        break;
                    case RECERIVE_OK:
                        //收到消息通知
                        if (theActivity.chatAdapter != null) {
                            theActivity.chatAdapter.notifyItemInserted(theActivity.tblist.size() - 1);
                        }
                        break;
                    case PULL_TO_REFRESH_DOWN:
                        //加载历史消息
                        if (theActivity.chatAdapter != null) {
                            List<MessageBean.DataBean> tblist = (List<MessageBean.DataBean>) msg.obj;
                            theActivity.content_lv.setPullLoadMoreCompleted();
                            theActivity.chatAdapter.addMessage(tblist);
                            theActivity.chatAdapter.notifyDataSetChanged();
                            theActivity.content_lv.smoothScrollToPosition(theActivity.position - 1);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    //    @Xml(layouts = "activity_chat")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        content_lv.setPushRefreshEnable(false);
        mId = getIntent().getStringExtra("id");
        mGroupId = getIntent().getStringExtra("groupId");
        if (TextUtils.isEmpty(mId)) {
            finish();
            return;
        }
        //        setChatCookie();
        initListener();
        IMManager.setMessageCallBack(this);
        //获取页面信息
        getChatIndexInfo();
    }

    private void upDataChatRead() {
        chatUpdateReadApi = new ChatUpdateReadApi();
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("id", mId);
        chatUpdateReadApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    ChatUpdateReadInfo chatUpdateReadInfo = JSONUtil.TransformSingleBean(serverData.data, ChatUpdateReadInfo.class);
                    //通知列表页更新未读消息数角标
                    NoreadAndChatidInfo noreadAndChatidInfo = new NoreadAndChatidInfo();
                    noreadAndChatidInfo.setId(mId);
                    noreadAndChatidInfo.setNoread(chatUpdateReadInfo.getNoread());
                    EventBus.getDefault().post(new Event<NoreadAndChatidInfo>(2,noreadAndChatidInfo));
                }
            }
        });
    }

    private void initListener() {
        ll_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isFlag) {
                    Util.showKeyBoard(mContext, ll_input);
                    isFlag = true;
                }
                return false;
            }
        });

        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                setListMargin(false, INPUT_HEIGHT);
            }

            @Override
            public void keyBoardHide(int height) {
            }
        });

        mess_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!TextUtils.isEmpty(mess_et.getText().toString().trim())) {
                        sendMessage(mess_et.getText().toString().trim());
                        mess_et.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
        content_lv.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                if (CollectionUtils.isEmpty(mDataList) || mDataList.size() < 9) {
                    content_lv.setPullLoadMoreCompleted();
                    ToastUtils.toast(mContext, "已加载全部历史消息").show();
                } else {
                    //加载历史消息
                    downLoad();
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back);
    }

    //获取页面信息
    private void getChatIndexInfo() {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("id", mId);
        chatIndexApi = new ChatIndexApi();
        chatIndexApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    AppLog.i("获取页面信息成功");
                    ChatIndexInfo chatIndexInfo = JSONUtil.TransformSingleBean(serverData.data, ChatIndexInfo.class);
                    tv_name.setText(chatIndexInfo.getTitle());
                    tv_type.setText(chatIndexInfo.getSubtitle());
                    //获取聊天信息
                    getMessageInfo();
                    //更新未读消息数
                    upDataChatRead();
                } else {
                    AppLog.i("获取页面信息失败");
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            }
        });
    }

    //获取私信信息
    private void getMessageInfo() {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("id", mId);
        maps.put("page", "1");
//        maps.put("msgtime", mDateTime);
        getMessageApi = new GetMessageApi();
        getMessageApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    mDataList = JSONUtil.jsonToArrayList(serverData.data, MessageBean.DataBean.class);
                    if (CollectionUtils.isNotEmpty(mDataList)) {
                        mDateTime = mDataList.get(0).getDateTime();
                        for (MessageBean.DataBean dataBean : mDataList) {
                            dataBean.handlerMessageTypeAndViewStatus();
                        }
                        tblist = mDataList;
                        if (tblist.size() <= 4) {
                            //列表布局正向显示(从上到下)
                            content_lv.setLinearLayout(false);
                        } else {
                            //列表布局反向显示(从下到上)
                            content_lv.setLinearLayout(true);
                        }
                        chatAdapter = new ChatAdapter(mContext, tblist);
                        content_lv.setAdapter(chatAdapter);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content_lv.setLinearLayout(false);
                                if (chatAdapter == null) {
                                    chatAdapter = new ChatAdapter(mContext, tblist);
                                }
                                content_lv.setAdapter(chatAdapter);
                            }
                        });
                    }
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            }
        });
    }

    //发送私信消息
    private void sendMessage(String content) {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("id", mId);
        //类型 暂时只有文字
        maps.put("class_id", "1");
        maps.put("content", content);
        chatSendApi = new ChatSendApi();
        chatSendApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    WebSocketBean webSocketBean = JSONUtil.TransformSingleBean(serverData.data, WebSocketBean.class);
                    Calendar cal = Calendar.getInstance();
                    String timeSet = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                    dataBean = new MessageBean.DataBean(webSocketBean.getFrom_client_img(), content, 0, ChatAdapter.TO_USER_MSG, Util.getSecondTimestamp() + "", timeSet);
                    dataBean.setType(ChatAdapter.TO_USER_MSG);
                    dataBean.setContent(content);
                    tblist.add(dataBean);
                    if(tblist.size() < 2){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content_lv.setLinearLayout(false);
                                if(chatAdapter == null){
                                    chatAdapter = new ChatAdapter(mContext,tblist);
                                }
                            }
                        });
                    }
                    mHandler.sendEmptyMessage(SEND_OK);
                }else{
                    dataBean.setMessageStatus(-1);
                    if(chatAdapter != null){
                        chatAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        NetWork.getInstance().setOnErrorCallBack(new NetWork.OnErrorCallBack() {
            @Override
            public void onErrorCallBack(Call call, Response response, Exception e) {
                dataBean.setMessageStatus(-1);
                if(chatAdapter != null){
                    chatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void receiveMessage(MessageBean.DataBean dataBean, String group_id) {
        if (tblist.size() < 2) {
            tblist.add(dataBean);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    content_lv.setLinearLayout(false);
                    if (chatAdapter == null) {
                        chatAdapter = new ChatAdapter(mContext, tblist);
                    }
                    content_lv.setAdapter(chatAdapter);
                }
            });
        } else {
//            if (mId.equals(dataBean.getFromUserId())) {
                tblist.add(dataBean);
                mHandler.sendEmptyMessage(SEND_OK);
//            }
        }
    }

    @Override
    public void onFocusCallBack(String txt) {

    }

    /**
     * 设置recyclerview边距
     */
    private void setListMargin(boolean isTopOrBottom, int value) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) content_lv.getLayoutParams();
        if (isTopOrBottom) {
            layoutParams.topMargin = SizeUtils.dp2px(value);
        } else {
            layoutParams.bottomMargin = SizeUtils.dp2px(value);
        }
        content_lv.setLayoutParams(layoutParams);
    }

    public static void invoke(Context context, String id,String groupId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("groupId", groupId);
        context.startActivity(intent);
    }

    private void downLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadRecords();
            }
        }).start();
    }


    //下拉加载历史
    protected void loadRecords() {
        if (pagelist != null) {
            pagelist.clear();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", mId);
        params.put("page", page);
        if (Integer.parseInt(page) > 1) {
            params.put("msgtime", mDateTime);
        }
        params.put("group_id", mGroupId);
        new GetMessageApi().getCallBack(mContext, params, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData data) {
                if ("1".equals(data.code)) {
                    ArrayList<MessageBean.DataBean> dataBeen = JSONUtil.jsonToArrayList(data.data, MessageBean.DataBean.class);
                    if (CollectionUtils.isNotEmpty(dataBeen)) {
                        mDateTime = dataBeen.get(0).getDateTime();
                        for (MessageBean.DataBean dataBean : dataBeen) {
                            dataBean.handlerMessageTypeAndViewStatus();
                        }
                        pagelist = dataBeen;
                        position = pagelist.size();
                        if (pagelist.size() != 0) {
                            tblist.addAll(0, pagelist);
                            Message message = mHandler.obtainMessage();
                            message.obj = tblist;
                            message.what = PULL_TO_REFRESH_DOWN;
                            mHandler.sendMessage(message);
                            page = (Integer.parseInt(page)) + 1 + "";
                        } else {
                            if ("2".equals(page)) {
                                content_lv.setPullLoadMoreCompleted();
                            }
                        }
                    } else {
                        content_lv.setPullLoadMoreCompleted();
                        ToastUtils.toast(mContext, "已加载全部历史消息").show();
                    }
                } else {
                    ToastUtils.toast(mContext, data.message).show();
                }
            }
        });
    }

    /**
     * 为连接设置Cookie
     */
    private void setChatCookie() {
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        HttpUrl httpUrl = new HttpUrl.Builder().scheme("https").host("chat.yuemei.com").build();
        List<Cookie> cookies = cookieStore.loadCookie(httpUrl);
        for (Cookie cookie : cookies) {
            domain = cookie.domain();
            expiresAt = cookie.expiresAt();
            name = cookie.name();
            path = cookie.path();
            value = cookie.value();
        }

        String mYuemeiinfo = Util.getYuemeiInfo();
        cookieStore.removeCookie(httpUrl);
        Cookie yuemeiinfo = new Cookie.Builder().name("dejiainfo").value(mYuemeiinfo).domain(domain).expiresAt(expiresAt).path(path).build();
        cookieStore.saveCookie(httpUrl, yuemeiinfo);
        List<Cookie> cookies222 = cookieStore.loadCookie(httpUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
    }
}
