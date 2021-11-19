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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dejia.anju.activity.ChatActivity;
import com.dejia.anju.adapter.MessageListAdapter;
import com.dejia.anju.model.MessageListData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.dejia.anju.R;
import com.dejia.anju.api.GetMessageListApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.net.ServerData;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

//私信列表页
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

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
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

//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
//        layoutParams.height = statusbarHeight + SizeUtils.dp2px(44);
//        ll_title.setLayoutParams(layoutParams);
//        ll_title.setPadding(0, statusbarHeight, 0, 0);
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
            }
        });
    }

    //请求消息列表
    private void getMessageList() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        new GetMessageListApi().getCallBack(mContext, map, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
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
                }
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
            messageListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    ChatActivity.invoke(mContext, messageListAdapter.getData().get(position).getId());
                }
            });
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
                break;
            case R.id.ll2:
                //@我的
                break;
            case R.id.ll3:
                //收到的赞
                break;
            case R.id.ll4:
                //公告通知
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
