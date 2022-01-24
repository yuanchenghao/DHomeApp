package com.dejia.anju.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.dejia.anju.adapter.MyArticleAdapter;
import com.dejia.anju.adapter.RenZhengListAdapter;
import com.dejia.anju.api.GetMyArticleApi;
import com.dejia.anju.api.GetUserInfoApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.MessageShowInfo;
import com.dejia.anju.model.MyArticleInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class PersonActivity extends BaseActivity {
    @BindView(R.id.iv_scan_code)
    ImageView iv_scan_code;
    @BindView(R.id.iv_share)
    ImageView iv_share;
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
    @BindView(R.id.tv_renzheng_title)
    TextView tv_renzheng_title;
    @BindView(R.id.tv_renzheng_title2)
    TextView tv_renzheng_title2;
    @BindView(R.id.tv_content_title)
    TextView tv_content_title;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.ll_context)
    LinearLayout ll_context;
    @BindView(R.id.ll_zan)
    LinearLayout ll_zan;
    @BindView(R.id.ll_fans)
    LinearLayout ll_fans;
    @BindView(R.id.ll_follow)
    LinearLayout ll_follow;
    @BindView(R.id.ll_renzheng)
    LinearLayout ll_renzheng;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    private UserInfo userInfo;
    private String userId;
    private int page = 1;
    private MyArticleAdapter myArticleAdapter;
    private HashMap<String, Object> map = new HashMap<>(0);

    @Xml(layouts = "activity_person")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void initView() {
        userId = getIntent().getStringExtra("userId");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.height = statusbarHeight + SizeUtils.dp2px(50);
        ll_title.setLayoutParams(layoutParams);
        ll_title.setPadding(0, statusbarHeight, 0, 0);
        rv.setNestedScrollingEnabled(false);
        rv_renzheng.setNestedScrollingEnabled(false);
        getUserInfo();
        getMyArticle();
    }

    @Override
    protected void initData() {
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

    /**
     * 获取我的文章列表
     */
    private void getMyArticle() {
        HashMap<String, Object> maps = new HashMap<>(0);
        maps.put("id", userId);
        maps.put("page", page);
        new GetMyArticleApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if (refresh_layout != null) {
                refresh_layout.finishRefresh();
            }
            if ("1".equals(serverData.code)) {
                if (serverData.data != null) {
                    List<MyArticleInfo> list = JSONUtil.jsonToArrayList(serverData.data, MyArticleInfo.class);
                    if (list != null) {
                        if (list.size() == 0) {
                            if (refresh_layout != null) {
                                refresh_layout.finishLoadMoreWithNoMoreData();
                            }
                        } else {
                            if (refresh_layout != null) {
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
                } else {
                    refresh_layout.finishLoadMoreWithNoMoreData();
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
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
            myArticleAdapter.setEventListener((v, data, pos) -> WebUrlJumpManager.getInstance().invoke(mContext, list.get(pos).getUrl(), null));
        } else {
            myArticleAdapter.addData(list);
        }
    }

    //获取用户信息 主要是为了拿到认证信息
    private void getUserInfo() {
        HashMap<String, Object> maps = new HashMap<>(0);
        maps.put("id", userId);
        new GetUserInfoApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                UserInfo user = JSONUtil.TransformSingleBean(serverData.data, UserInfo.class);
                userInfo = user;
                upDataUi();
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    private void upDataUi() {
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userId) && userId.equals(Util.getUid())) {
                //自己
                iv_share.setVisibility(View.GONE);
                iv_scan_code.setVisibility(View.VISIBLE);
                edit_info.setVisibility(View.VISIBLE);
                tv_renzheng_title.setText("我的认证");
                tv_content_title.setText("我的内容");
                tv_renzheng_title2.setText("申请认证");
            } else {
                //他人
                MessageShowInfo messageShowInfo = KVUtils.getInstance().decodeParcelable("message_show", MessageShowInfo.class);
                if (messageShowInfo != null && !TextUtils.isEmpty(messageShowInfo.getShare()) && !messageShowInfo.getShare().equals("0")) {
                    iv_share.setVisibility(View.VISIBLE);
                } else {
                    iv_share.setVisibility(View.GONE);
                }
                iv_scan_code.setVisibility(View.GONE);
                edit_info.setVisibility(View.GONE);
                tv_renzheng_title.setText("TA的认证");
                tv_content_title.setText("TA的内容");
                tv_renzheng_title2.setText("");
            }
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
                if (!TextUtils.isEmpty(userId) && userId.equals(Util.getUid())) {
                    tv_introduce.setText("你还没有填写一句话简介");
                }else{
                    tv_introduce.setText("TA还没有填写过简介");
                }
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
            if (userInfo.getAuth() != null && userInfo.getAuth().size() > 0) {
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
            } else {
                rv_renzheng.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.iv_scan_code, R.id.edit_info, R.id.ll_introduce, R.id.iv_share, R.id.iv_close, R.id.ll_context, R.id.ll_zan, R.id.ll_fans, R.id.ll_follow, R.id.ll_renzheng, R.id.ll_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_code:
                if (Util.isLogin()) {
                    QrCodeActivity.invoke(mContext);
                }
                break;
            case R.id.edit_info:
                //编辑资料
                mContext.startActivity(new Intent(mContext, EditUserInfoActivity.class));
                break;
            case R.id.ll_introduce:
                if (!TextUtils.isEmpty(userId) && userId.equals(Util.getUid())) {
                    mContext.startActivity(new Intent(mContext, EditIntroduceActivity.class));
                }
                break;
            case R.id.iv_share:
                ToastUtils.toast(mContext, "分享").show();
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.ll_context:
            case R.id.ll_content:
                map.clear();
                map.put("id", userInfo.getId());
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("https://www.dejia.com/?webviewType=webview&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/vue/myPost/")
                            .append("&request_param=")
                            .append(JSONUtil.toJSONString(map));
                    WebUrlJumpManager.getInstance().invoke(mContext, stringBuffer.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_zan:
                if (!TextUtils.isEmpty(userId) && userId.equals(Util.getUid())) {
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
                }
                break;
            case R.id.ll_fans:
                map.clear();
                map.put("type", "1");
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    new StringBuffer("https://www.dejia.com/?webviewType=webview&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/vue/followList/")
                            .append("&request_param=")
                            .append(JSONUtil.toJSONString(map));
                    WebUrlJumpManager.getInstance().invoke(mContext, stringBuffer.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_follow:
                map.clear();
                map.put("type", "2");
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("https://www.dejia.com/?webviewType=webview&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/vue/followList/")
                            .append("&request_param=")
                            .append(JSONUtil.toJSONString(map));
                    WebUrlJumpManager.getInstance().invoke(mContext, stringBuffer.toString(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_renzheng:
                if (!TextUtils.isEmpty(userId) && userId.equals(Util.getUid())) {
                    WebViewData webViewData = new WebViewData.WebDataBuilder()
                            .setWebviewType("webview")
                            .setLinkisJoint("1")
                            .setIsHide("1")
                            .setIsRefresh("1")
                            .setEnableSafeArea("1")
                            .setBounces("1")
                            .setIsRemoveUpper("0")
                            .setEnableBottomSafeArea("0")
                            .setBgColor("#F6F6F6")
                            .setIs_back("0")
                            .setIs_share("0")
                            .setShare_data("0")
                            .setLink("/vue/auth/")
                            .build();
                    WebUrlJumpManager.getInstance().invoke(mContext, "", webViewData);
                }
                break;

        }
    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void invoke(Context context, String userId) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }
}
