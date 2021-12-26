package com.dejia.anju.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.MainActivity;
import com.dejia.anju.R;
import com.dejia.anju.activity.EditIntroduceActivity;
import com.dejia.anju.activity.EditUserInfoActivity;
import com.dejia.anju.activity.QRCodeActivity;
import com.dejia.anju.adapter.MyArticleAdapter;
import com.dejia.anju.adapter.RenZhengListAdapter;
import com.dejia.anju.api.GetMyArticleApi;
import com.dejia.anju.api.GetUserInfoApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.event.Event;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.MyArticleInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @BindView(R.id.iv_scan_code)
    ImageView iv_scan_code;
    @BindView(R.id.iv_drawer)
    ImageView iv_drawer;
    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.iv_person)
    SimpleDraweeView iv_person;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.edit_info)
    TextView edit_info;
    @BindView(R.id.tv_content_num)
    TextView tv_content_num;
    @BindView(R.id.tv_zan_count)
    TextView tv_zan_count;
    @BindView(R.id.tv_fans)
    TextView tv_fans;
    @BindView(R.id.tv_follow)
    TextView tv_follow;
    @BindView(R.id.ll_introduce)
    LinearLayout ll_introduce;
    @BindView(R.id.iv_write_icon)
    ImageView iv_write_icon;
    @BindView(R.id.tv_introduce)
    TextView tv_introduce;
    @BindView(R.id.tv_my_article)
    TextView tv_my_article;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rv_renzheng)
    RecyclerView rv_renzheng;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.ll_context)
    LinearLayout ll_context;
    @BindView(R.id.ll_zan)
    LinearLayout ll_zan;
    @BindView(R.id.ll_fans)
    LinearLayout ll_fans;
    @BindView(R.id.ll_follow)
    LinearLayout ll_follow;
    private UserInfo userInfo;
    private int page = 1;
    private MyArticleAdapter myArticleAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 3:
                userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
                upDataUi();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Xml(layouts = "fragment_my")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.height = statusbarHeight + SizeUtils.dp2px(50);
        ll_title.setLayoutParams(layoutParams);
        ll_title.setPadding(0, statusbarHeight, 0, 0);
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        rv.setNestedScrollingEnabled(false);
        rv_renzheng.setNestedScrollingEnabled(false);
        upDataUi();
        getUserInfo();
        getMyArticle();
    }

    //获取用户信息 主要是为了拿到认证信息
    private void getUserInfo() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("id", userInfo.getId());
        new GetUserInfoApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    UserInfo user = JSONUtil.TransformSingleBean(serverData.data, UserInfo.class);
                    KVUtils.getInstance().encode("user", user);
                    userInfo = user;
                    upDataUi();
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            }
        });
    }

    //获取我的文章列表
    private void getMyArticle() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("id", userInfo.getId());
        maps.put("page", page);
        new GetMyArticleApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    if(serverData.data != null){
                        List<MyArticleInfo> list = JSONUtil.jsonToArrayList(serverData.data, MyArticleInfo.class);
                        if (list != null) {
                            if (list.size() == 0) {
                                if (refresh_layout == null) {
                                    refresh_layout.finishLoadMoreWithNoMoreData();
                                }
                            } else {
                                if (refresh_layout == null) {
                                    refresh_layout.finishLoadMore();
                                }
                            }
                            setMyArticleList(list);
                        } else {
                            refresh_layout.finishLoadMoreWithNoMoreData();
                        }
                        if (list != null && list.size() > 0 && page == 1) {
                            tv_my_article.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                        }
                    }else{
                        refresh_layout.finishLoadMoreWithNoMoreData();
                        ToastUtils.toast(mContext, serverData.message).show();
                    }
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            }
        });
    }

    //设置我的内容列表
    private void setMyArticleList(List<MyArticleInfo> list) {
        if (null == myArticleAdapter) {
            YMLinearLayoutManager ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            RecyclerView.ItemAnimator itemAnimator = rv.getItemAnimator();
            //取消局部刷新动画效果
            if (null != itemAnimator) {
                ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            }
            rv.setLayoutManager(ymLinearLayoutManager);
            myArticleAdapter = new MyArticleAdapter(mContext, list);
            rv.setAdapter(myArticleAdapter);
            myArticleAdapter.setEventListener(new MyArticleAdapter.EventListener() {
                @Override
                public void onItemListener(View v, MyArticleInfo data, int pos) {
                    WebUrlJumpManager.getInstance().invoke(mContext, list.get(pos).getUrl(), null);
                }
            });
        }else{
            myArticleAdapter.addData(list);
        }
    }

    private void upDataUi() {
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getImg())) {
                iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(userInfo.getImg()).setAutoPlayAnimations(true).build());
            } else {
                iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
            }
            if (!TextUtils.isEmpty(userInfo.getNickname())) {
                tv_name.setText(userInfo.getNickname());
            }
            if (!TextUtils.isEmpty(userInfo.getSex())) {
                if ("1".equals(userInfo.getSex())) {
                    //男
                    tv_sex.setText("男");
                    iv_sex.setImageResource(R.mipmap.boy);
                    ll_sex.setVisibility(View.VISIBLE);
                } else if ("2".equals(userInfo.getSex())) {
                    //女
                    tv_sex.setText("女");
                    iv_sex.setImageResource(R.mipmap.girl);
                    ll_sex.setVisibility(View.VISIBLE);
                } else {
                    ll_sex.setVisibility(View.GONE);
                }
            } else {
                ll_sex.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(userInfo.getPersonal_info())) {
                iv_write_icon.setVisibility(View.VISIBLE);
                tv_introduce.setText("你还没有填写一句话简介");
            } else {
                iv_write_icon.setVisibility(View.GONE);
                tv_introduce.setText("简介：" + userInfo.getPersonal_info());
            }
            if (TextUtils.isEmpty(userInfo.getUgc_num())) {
                tv_content_num.setText("0");
            } else {
                tv_content_num.setText(userInfo.getUgc_num());
            }
            if (TextUtils.isEmpty(userInfo.getAgree_num())) {
                tv_zan_count.setText("0");
            } else {
                tv_zan_count.setText(userInfo.getAgree_num());
            }
            if (TextUtils.isEmpty(userInfo.getFollowing_me_num())) {
                tv_fans.setText("0");
            } else {
                tv_fans.setText(userInfo.getFollowing_me_num());
            }
            if (TextUtils.isEmpty(userInfo.getFollowing_num())) {
                tv_follow.setText("0");
            } else {
                tv_follow.setText(userInfo.getFollowing_num());
            }
            if(userInfo.getAuth() != null && userInfo.getAuth().size() > 0){
                YMLinearLayoutManager ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                RecyclerView.ItemAnimator itemAnimator = rv_renzheng.getItemAnimator();
                //取消局部刷新动画效果
                if (null != itemAnimator) {
                    ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
                }
                rv_renzheng.setLayoutManager(ymLinearLayoutManager);
                RenZhengListAdapter renZhengListAdapter = new RenZhengListAdapter(mContext, R.layout.item_renzhen, userInfo.getAuth());
                rv_renzheng.setAdapter(renZhengListAdapter);
                rv_renzheng.setVisibility(View.VISIBLE);
            }else{
                rv_renzheng.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initData(View view) {
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //加载更多
                page++;
                getMyArticle();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                myArticleAdapter = null;
                getUserInfo();
                getMyArticle();
            }
        });
    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.iv_scan_code, R.id.iv_drawer, R.id.edit_info,R.id.ll_introduce,R.id.ll_context,R.id.ll_zan,R.id.ll_fans,R.id.ll_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_code:
                QRCodeActivity.invoke(mContext);
                break;
            case R.id.iv_drawer:
                if ((MainActivity) getActivity() != null && ((MainActivity) getActivity()).drawerLayout != null) {
                    ((MainActivity) getActivity()).drawerLayout.openDrawer(Gravity.END);
                }
                break;
            case R.id.edit_info:
                //编辑资料
                mContext.startActivity(new Intent(mContext, EditUserInfoActivity.class));
                break;
            case R.id.ll_introduce:
                mContext.startActivity(new Intent(mContext, EditIntroduceActivity.class));
                break;
            case R.id.ll_context:
                ToastUtils.toast(mContext,"内容").show();
                break;
            case R.id.ll_zan:
                ToastUtils.toast(mContext,"获赞").show();
                break;
            case R.id.ll_fans:
                ToastUtils.toast(mContext,"粉丝").show();
                break;
            case R.id.ll_follow:
                ToastUtils.toast(mContext,"关注").show();
                break;
        }
    }

}
