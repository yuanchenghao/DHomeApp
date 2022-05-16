package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.adapter.ChatAdapter;
import com.dejia.anju.api.ChatDelShieldingApi;
import com.dejia.anju.api.ChatIndexApi;
import com.dejia.anju.api.ChatReportApi;
import com.dejia.anju.api.ChatSendApi;
import com.dejia.anju.api.ChatShieldingApi;
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
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.SoftKeyBoardListener;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.PullLoadMoreRecyclerView;
import com.dejia.anju.webSocket.IMManager;
import com.dejia.anju.webSocket.MessageCallBack;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.CookieStore;

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
import okhttp3.Cookie;
import okhttp3.HttpUrl;

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
    @BindView(R.id.iv_report)
    ImageView iv_report;
    @BindView(R.id.tv_tips_shield)
    TextView tv_tips_shield;
    private CustomPopWindow mPopWindow;
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
    private ChatIndexInfo chatIndexInfo;
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
        setChatCookie();
        initListener();
        IMManager.setMessageCallBack(this);
        //获取页面信息
        getChatIndexInfo();
    }

    private void upDataChatRead() {
        chatUpdateReadApi = new ChatUpdateReadApi();
        Map<String, Object> maps = new HashMap<>(0);
        maps.put("id", mId);
        chatUpdateReadApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                ChatUpdateReadInfo chatUpdateReadInfo = JSONUtil.TransformSingleBean(serverData.data, ChatUpdateReadInfo.class);
                //通知列表页更新未读消息数角标
                NoreadAndChatidInfo noreadAndChatidInfo = new NoreadAndChatidInfo();
                noreadAndChatidInfo.setId(mId);
                noreadAndChatidInfo.setNoread(chatUpdateReadInfo.getNoread());
                EventBus.getDefault().post(new Event<>(2, noreadAndChatidInfo));
            }
        });
    }

    private void initListener() {
        ll_input.setOnTouchListener((v, event) -> {
            if (!isFlag) {
                Util.showKeyBoard(mContext, ll_input);
                isFlag = true;
            }
            return false;
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

        mess_et.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!TextUtils.isEmpty(mess_et.getText().toString().trim())) {
                    sendMessage(mess_et.getText().toString().trim());
                    mess_et.setText("");
                }
                return true;
            }
            return false;
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
        setMultiOnClickListener(ll_back, iv_report);
    }

    //获取页面信息
    private void getChatIndexInfo() {
        Map<String, Object> maps = new HashMap<>(0);
        maps.put("id", mId);
        chatIndexApi = new ChatIndexApi();
        chatIndexApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                AppLog.i("获取页面信息成功");
                chatIndexInfo = JSONUtil.TransformSingleBean(serverData.data, ChatIndexInfo.class);
                tv_name.setText(chatIndexInfo.getTitle());
                tv_type.setText(chatIndexInfo.getSubtitle());
                //获取聊天信息
                getMessageInfo();
                //更新未读消息数
                upDataChatRead();
                upDataShield();
            } else {
                AppLog.i("获取页面信息失败");
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    private void upDataShield() {
        if (chatIndexInfo != null
                && chatIndexInfo.getShielding_data() != null
                && !TextUtils.isEmpty(chatIndexInfo.getShielding_data().getShielding())) {
            if ("0".equals(chatIndexInfo.getShielding_data().getShielding())) {
                tv_tips_shield.setVisibility(View.GONE);
                ll_input.setVisibility(View.VISIBLE);
            } else if ("1".equals(chatIndexInfo.getShielding_data().getShielding())) {
                setSpanString();
                tv_tips_shield.setVisibility(View.VISIBLE);
                ll_input.setVisibility(View.GONE);
            } else {
                setSpanString();
                tv_tips_shield.setVisibility(View.VISIBLE);
                ll_input.setVisibility(View.GONE);
            }
        } else {
            tv_tips_shield.setVisibility(View.GONE);
            ll_input.setVisibility(View.VISIBLE);
        }
    }

    private void setSpanString() {
        if (!TextUtils.isEmpty(chatIndexInfo.getShielding_data().getButton_title())) {
            SpannableString spannableString = new SpannableString(chatIndexInfo.getShielding_data().getDesc() + chatIndexInfo.getShielding_data().getButton_title());
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    DialogUtils.showExitToolDialog(mContext,
                            "取消屏蔽后，你可以继续收到来自此用户新发送的消息",
                            "取消屏蔽",
                            "暂不",
                            new DialogUtils.CallBack2() {
                                @Override
                                public void onYesClick() {
                                    DialogUtils.closeDialog();
                                    postDelShieldInfo();
                                }

                                @Override
                                public void onNoClick() {
                                    DialogUtils.closeDialog();
                                }
                            });
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#0095FF"));
                    ds.setUnderlineText(false); //是否设置下划线
                }
            }, chatIndexInfo.getShielding_data().getDesc().length(), chatIndexInfo.getShielding_data().getDesc().length() + chatIndexInfo.getShielding_data().getButton_title().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_tips_shield.setText(spannableString);
        } else {
            tv_tips_shield.setText(chatIndexInfo.getShielding_data().getDesc());
        }
    }

    //获取私信信息
    private void getMessageInfo() {
        Map<String, Object> maps = new HashMap<>(0);
        maps.put("id", mId);
        maps.put("page", "1");
//        maps.put("msgtime", mDateTime);
        getMessageApi = new GetMessageApi();
        getMessageApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
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
        });
    }

    //发送私信消息
    private void sendMessage(String content) {
        Map<String, Object> maps = new HashMap<>(0);
        maps.put("id", mId);
        //类型 暂时只有文字
        maps.put("class_id", "1");
        maps.put("content", content);
        chatSendApi = new ChatSendApi();
        chatSendApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                WebSocketBean webSocketBean = JSONUtil.TransformSingleBean(serverData.data, WebSocketBean.class);
                Calendar cal = Calendar.getInstance();
                String timeSet = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                dataBean = new MessageBean.DataBean(webSocketBean.getFrom_client_img(), content, 0, ChatAdapter.TO_USER_MSG, Util.getSecondTimestamp() + "", timeSet);
                dataBean.setType(ChatAdapter.TO_USER_MSG);
                dataBean.setContent(content);
                tblist.add(dataBean);
                if (tblist.size() < 2) {
                    runOnUiThread(() -> {
                        content_lv.setLinearLayout(false);
                        if (chatAdapter == null) {
                            chatAdapter = new ChatAdapter(mContext, tblist);
                        }
                    });
                }
                mHandler.sendEmptyMessage(SEND_OK);
            } else {
                dataBean.setMessageStatus(-1);
                if (chatAdapter != null) {
                    chatAdapter.notifyDataSetChanged();
                }
            }
        });
        NetWork.getInstance().setOnErrorCallBack((call, response, e) -> {
            dataBean.setMessageStatus(-1);
            if (chatAdapter != null) {
                chatAdapter.notifyDataSetChanged();
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
            case R.id.iv_report:
                showPopListView();
                break;
        }
    }

    private void showPopListView() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_layout, null);
        if (chatIndexInfo != null
                && chatIndexInfo.getShielding_data() != null
                && !TextUtils.isEmpty(chatIndexInfo.getShielding_data().getShielding())
                && chatIndexInfo.getShielding_data().getShielding().equals("0")) {
            ((TextView) view.findViewById(R.id.tv_shield)).setText("屏蔽此用户");
        } else {
            ((TextView) view.findViewById(R.id.tv_shield)).setText("取消屏蔽");
        }
        View.OnClickListener listener1 = v -> {
            mPopWindow.dissmiss();
            DialogUtils.showExitToolDialog(mContext,
                    "如果您认为此条内容涉及政治、色情、赌博、毒品、人身攻击、隐私泄露等信息，您可以进行举报，我们会在核实后进行处理",
                    "确认举报",
                    "暂不",
                    new DialogUtils.CallBack2() {
                        @Override
                        public void onYesClick() {
                            DialogUtils.closeDialog();
                            postReplyInfo();
                        }

                        @Override
                        public void onNoClick() {
                            DialogUtils.closeDialog();
                        }
                    });
        };
        View.OnClickListener listener2 = v -> {
            if (chatIndexInfo != null
                    && chatIndexInfo.getShielding_data() != null
                    && !TextUtils.isEmpty(chatIndexInfo.getShielding_data().getShielding())
                    && chatIndexInfo.getShielding_data().getShielding().equals("0")) {
                //去屏蔽
                mPopWindow.dissmiss();
                DialogUtils.showExitToolDialog(mContext,
                        "屏蔽此用户后，对方将无法再向你发送消息，直到你主动取消屏蔽",
                        "确认屏蔽",
                        "暂不",
                        new DialogUtils.CallBack2() {
                            @Override
                            public void onYesClick() {
                                DialogUtils.closeDialog();
                                postShieldInfo();
                            }

                            @Override
                            public void onNoClick() {
                                DialogUtils.closeDialog();
                            }
                        });
            } else {
                //取消屏蔽
                mPopWindow.dissmiss();
                DialogUtils.showExitToolDialog(mContext,
                        "取消屏蔽后，你可以继续收到来自此用户新发送的消息",
                        "取消屏蔽",
                        "暂不",
                        new DialogUtils.CallBack2() {
                            @Override
                            public void onYesClick() {
                                DialogUtils.closeDialog();
                                postDelShieldInfo();
                            }

                            @Override
                            public void onNoClick() {
                                DialogUtils.closeDialog();
                            }
                        });
            }
        };
        view.findViewById(R.id.tv_report).setOnClickListener(listener1);
        view.findViewById(R.id.tv_shield).setOnClickListener(listener2);
        mPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .enableOutsideTouchableDissmiss(true)
                .create();
        mPopWindow.showAsDropDown(iv_report, 0, 10);
    }

    private void postReplyInfo() {
        Map<String, Object> maps = new HashMap<>(0);
        maps.put("id", mId);
        new ChatReportApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                ToastUtils.toast(mContext, "感谢您提供的举报，审核预计会在1-3个工作日内完成").show();
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    private void postShieldInfo() {
        Map<String, Object> maps = new HashMap<>(0);
        maps.put("id", mId);
        new ChatShieldingApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                getChatIndexInfo();
                ToastUtils.toast(mContext, "屏蔽成功").show();
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    private void postDelShieldInfo() {
        Map<String, Object> maps = new HashMap<>(0);
        maps.put("id", mId);
        new ChatDelShieldingApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                getChatIndexInfo();
                ToastUtils.toast(mContext, "屏蔽已取消").show();
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    @Override
    public void receiveMessage(MessageBean.DataBean dataBean, String group_id) {
        if (tblist.size() < 2) {
            tblist.add(dataBean);
            runOnUiThread(() -> {
                content_lv.setLinearLayout(false);
                if (chatAdapter == null) {
                    chatAdapter = new ChatAdapter(mContext, tblist);
                }
                content_lv.setAdapter(chatAdapter);
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

    public static void invoke(Context context, String id, String groupId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("groupId", groupId);
        context.startActivity(intent);
    }

    private void downLoad() {
        new Thread(() -> loadRecords()).start();
    }


    //下拉加载历史
    protected void loadRecords() {
        if (pagelist != null) {
            pagelist.clear();
        }
        Map<String, Object> params = new HashMap<>(0);
        params.put("id", mId);
        params.put("page", page);
        if (Integer.parseInt(page) > 1) {
            params.put("msgtime", mDateTime);
        }
        params.put("group_id", mGroupId);
        new GetMessageApi().getCallBack(mContext, params, (BaseCallBackListener<ServerData>) data -> {
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
