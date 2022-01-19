package com.dejia.anju.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.activity.ChatActivity;
import com.dejia.anju.adapter.MessageListAdapter;
import com.dejia.anju.api.GetMessageListApi;
import com.dejia.anju.api.MessageCountApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.event.Event;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.MessageCountInfo;
import com.dejia.anju.model.MessageListData;
import com.dejia.anju.model.NoreadAndChatidInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

/**
 * @author ych
 * 私信列表页
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.ll_notice)
    LinearLayout ll_notice;
    @BindView(R.id.iv_close_notice)
    ImageView iv_close_notice;
    @BindView(R.id.tv_open_notice)
    TextView tv_open_notice;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.ll3)
    LinearLayout ll3;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.item_inform_tv1)
    TextView item_inform_tv1;
    @BindView(R.id.item_inform_tv2)
    TextView item_inform_tv2;
    @BindView(R.id.item_inform_tv3)
    TextView item_inform_tv3;
    @BindView(R.id.item_inform_tv4)
    TextView item_inform_tv4;
    private int page = 1;
    private MessageListAdapter messageListAdapter;
    private YMLinearLayoutManager ymLinearLayoutManager;
    private ArrayList<MessageListData> messageListData;
    private int mPos;
    private HashMap<String, Object> map = new HashMap<>(0);
    private UserInfo userInfo;

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 2:
                String mId = ((NoreadAndChatidInfo) msgEvent.getData()).getId();
                String mNoread = ((NoreadAndChatidInfo) msgEvent.getData()).getNoread();
                boolean refreshMessageList = true;
                if (mContext != null) {
                    if (messageListAdapter == null) {
                        return;
                    }
                    List<MessageListData> mData = messageListAdapter.getData();
                    for (int i = 0; i < mData.size(); i++) {
                        if (mId.equals(mData.get(i).getId())) {
                            refreshMessageList = false;
                            mPos = i;
                            break;
                        }
                    }
                    AppLog.i("mPos === " + mPos);
                    if (mContext != null) {
                        if (refreshMessageList) {
                            this.page = 1;
                            this.messageListAdapter = null;
                            //如果改消息不存在刷新消息列表
                            this.getMessageList();
                        } else {
                            //设置医院消息个数
                            int noread = Integer.parseInt(mNoread);
                            this.messageListAdapter.setNoread(mPos, (noread + 1) + "");
                            this.messageListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMessageNum();
        if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
            ll_notice.setVisibility(View.VISIBLE);
        } else {
            ll_notice.setVisibility(View.GONE);
        }
    }

    @Xml(layouts = "fragment_message")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        setMultiOnClickListener(ll1, ll2, ll3, ll4, iv_close_notice, tv_open_notice);
    }

    @Override
    protected void initData(View view) {
        //自动刷新
        smartRefreshLayout.autoRefresh();
        //刷新监听
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //加载更多
                page++;
                getMessageList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新
                page = 1;
                messageListAdapter = null;
                getMessageList();
                getMessageNum();
            }
        });
    }

    //请求各种消息数
    private void getMessageNum() {
        new MessageCountApi().getCallBack(mContext, new HashMap<>(0), (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                KVUtils.getInstance().encode("message_count", JSONUtil.TransformSingleBean(serverData.data, MessageCountInfo.class));
            }
            MessageCountInfo messageCountInfo = KVUtils.getInstance().decodeParcelable("message_count", MessageCountInfo.class);
            if (messageCountInfo != null && messageCountInfo.getChat_num() > 0) {
                item_inform_tv1.setText(messageCountInfo.getChat_num() + "");
                item_inform_tv1.setVisibility(View.VISIBLE);
            } else {
                item_inform_tv1.setVisibility(View.GONE);
            }
            if (messageCountInfo != null && messageCountInfo.getAt_me_num() > 0) {
                item_inform_tv2.setText(messageCountInfo.getAt_me_num() + "");
                item_inform_tv2.setVisibility(View.VISIBLE);
            } else {
                item_inform_tv2.setVisibility(View.GONE);
            }
            if (messageCountInfo != null && messageCountInfo.getZan_me_num() > 0) {
                item_inform_tv3.setText(messageCountInfo.getZan_me_num() + "");
                item_inform_tv3.setVisibility(View.VISIBLE);
            } else {
                item_inform_tv3.setVisibility(View.GONE);
            }
            if (messageCountInfo != null && messageCountInfo.getNotice_num() > 0) {
                item_inform_tv4.setText(messageCountInfo.getNotice_num() + "");
                item_inform_tv4.setVisibility(View.VISIBLE);
            } else {
                item_inform_tv4.setVisibility(View.GONE);
            }
        });
    }

    //请求消息列表
    private void getMessageList() {
        HashMap<String, Object> map = new HashMap<>(0);
        map.put("page", page);
        new GetMessageListApi().getCallBack(mContext, map, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                if (serverData.data != null) {
                    messageListData = JSONUtil.jsonToArrayList(serverData.data, MessageListData.class);
                    smartRefreshLayout.finishRefresh();
                    if (messageListData.size() == 0) {
                        if (smartRefreshLayout == null) {
                            return;
                        }
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        if (smartRefreshLayout == null) {
                            return;
                        }
                        smartRefreshLayout.finishLoadMore();
                    }
                    setMessageListAdapter();
                } else {
                    smartRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    //设置适配器
    private void setMessageListAdapter() {
        if (null == messageListAdapter) {
            ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            RecyclerView.ItemAnimator itemAnimator = rv.getItemAnimator();
            //取消局部刷新动画效果
            if (null != itemAnimator) {
                ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            }
            rv.setLayoutManager(ymLinearLayoutManager);
            messageListAdapter = new MessageListAdapter(mContext, R.layout.item_message_chat_list, messageListData);
            rv.setAdapter(messageListAdapter);
            messageListAdapter.setOnItemClickListener((adapter, view, position) -> ChatActivity.invoke(mContext
                    , messageListAdapter.getData().get(position).getId()
                    , messageListAdapter.getData().get(position).getGroupUserId()));
        } else {
            //添加
            messageListAdapter.addData(messageListData);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll1:
                //回复我的
                map.clear();
                map.put("id", userInfo.getId());
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("https://www.dejia.com/?webviewType=webview&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/vue/messageReplyMe/")
                            .append("&request_param=")
                            .append(JSONUtil.toJSONString(map));
                    WebUrlJumpManager.getInstance().invoke(mContext, stringBuffer.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll2:
                //@我的
                map.clear();
                map.put("id", userInfo.getId());
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("https://www.dejia.com/?webviewType=webview&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/vue/messageAtMe/")
                            .append("&request_param=")
                            .append(JSONUtil.toJSONString(map));
                    WebUrlJumpManager.getInstance().invoke(mContext, stringBuffer.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll3:
                //收到的赞
                map.clear();
                map.put("id", userInfo.getId());
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("https://www.dejia.com/?webviewType=webview&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/vue/messageAgreeMe/")
                            .append("&request_param=")
                            .append(JSONUtil.toJSONString(map));
                    WebUrlJumpManager.getInstance().invoke(mContext, stringBuffer.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll4:
                //公告通知
                map.clear();
                map.put("id", userInfo.getId());
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("https://www.dejia.com/?webviewType=webview&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/vue/messageNoticeMe/")
                            .append("&request_param=")
                            .append(JSONUtil.toJSONString(map));
                    WebUrlJumpManager.getInstance().invoke(mContext, stringBuffer.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_close_notice:
                ll_notice.setVisibility(View.GONE);
                break;
            case R.id.tv_open_notice:
                try {
                    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                    intent.putExtra(EXTRA_APP_PACKAGE, mContext.getPackageName());
                    intent.putExtra(EXTRA_CHANNEL_ID, mContext.getApplicationInfo().uid);
                    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                    intent.putExtra("app_package", mContext.getPackageName());
                    intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
                    // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"
                    //  if ("MI 6".equals(Build.MODEL)) {
                    //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    //      Uri uri = Uri.fromParts("package", getPackageName(), null);
                    //      intent.setData(uri);
                    //      // intent.setAction("com.android.settings/.SubSettings");
                    //  }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                    Intent intent = new Intent();
                    //下面这种方案是直接跳转到当前应用的设置界面。
                    //https://blog.csdn.net/ysy950803/article/details/71910806
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;
        }
    }
}
