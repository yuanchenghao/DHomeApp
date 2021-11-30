package com.dejia.anju.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dejia.anju.R;
import com.dejia.anju.adapter.HomeAdapter;
import com.dejia.anju.adapter.HomeFollowAdapter;
import com.dejia.anju.api.HomeFollowApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.model.HomeFollowBean;
import com.dejia.anju.model.HomeIndexBean;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

//首页关注fragment
public class FollowFragment extends BaseFragment {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private int page;
    private HomeFollowApi homeFollowApi;
    private HomeFollowAdapter homeFollowAdapter;
    private YMLinearLayoutManager ymLinearLayoutManager;
    private HomeFollowBean homeFollowBean;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        boolean change = isVisibleToUser != getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed() && change) {
            if (getUserVisibleHint()) {
                onVisible();
            } else {
                onInvisible();
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onInvisible();
        } else {
            onVisible();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && !isHidden()) {
            onVisible();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint() && !isHidden()) {
            onInvisible();
        }
    }

    private void onVisible() {
        if (!mHasLoadedOnce) {
            smartRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    smartRefreshLayout.autoRefresh();
                }
            }, 500);
        }
    }

    private void onInvisible() {

    }


    public static FollowFragment newInstance() {
        Bundle args = new Bundle();
        FollowFragment fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle args = getArguments();
//        if (args != null) {
//
//        }
    }

    @Xml(layouts = "fragment_follow")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow;
    }

    @Override
    protected void initView(View view) {
        //刷新监听
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //加载更多
                page++;
                getFollowList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新
                page = 1;
                homeFollowAdapter = null;
                getFollowList();
            }
        });
    }

    @Override
    protected void initData(View view) {

    }

    private void getFollowList() {
        homeFollowApi = new HomeFollowApi();
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("page", page);
        homeFollowApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            //            42153
            @Override
            public void onSuccess(ServerData serverData) {
                smartRefreshLayout.finishRefresh();
                if ("1".equals(serverData.code)) {
                    mHasLoadedOnce = true;
                    homeFollowBean = JSONUtil.TransformSingleBean(serverData.data, HomeFollowBean.class);
                    if (homeFollowBean != null) {
                        if (homeFollowBean.getBuilds().size() == 0
                                && homeFollowBean.getFollow_creator_article_list().size() == 0
                                && homeFollowBean.getFollow_creator_list().size() == 0
                                && homeFollowBean.getNo_follow_creator_list().size() == 0
                                && homeFollowBean.getNo_follow_creator_article_list().size() == 0) {
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
                        setHomeListAdapter();
                    } else {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            }
        });
    }

    //设置列表数据
    private void setHomeListAdapter() {
        if (null == homeFollowAdapter) {
            ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            RecyclerView.ItemAnimator itemAnimator = rv.getItemAnimator();
            //取消局部刷新动画效果
            if (null != itemAnimator) {
                ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            }
            rv.setLayoutManager(ymLinearLayoutManager);
            homeFollowAdapter = new HomeFollowAdapter(mContext, homeFollowBean);
            rv.setAdapter(homeFollowAdapter);
            homeFollowAdapter.setOnItemClickListener(new HomeFollowAdapter.onItemClickListener() {
                @Override
                public void onItemListener(View v, HomeIndexBean.HomeList data, int pos) {
                    if (!TextUtils.isEmpty(data.getUrl())) {
                        ToastUtils.toast(mContext, data.getUrl()).show();
                    }
                }
            });
        } else {
            //添加
            homeFollowAdapter.addData(homeFollowBean);
        }
    }
}
