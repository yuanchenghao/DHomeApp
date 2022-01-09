package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;

import com.dejia.anju.R;
import com.dejia.anju.adapter.HomeFollowAdapter;
import com.dejia.anju.api.HomeFollowApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.model.HomeFollowBean;
import com.dejia.anju.model.HomeFollowListBean;
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
    private List<HomeFollowListBean> mDatas;
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
            smartRefreshLayout.postDelayed(() -> smartRefreshLayout.autoRefresh(), 500);
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

    public void refresh() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.autoRefresh();
        }
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
        homeFollowApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if (smartRefreshLayout != null) {
                smartRefreshLayout.finishRefresh();
            }
            if ("1".equals(serverData.code)) {
                mHasLoadedOnce = true;
                homeFollowBean = JSONUtil.TransformSingleBean(serverData.data, HomeFollowBean.class);
                if (homeFollowBean != null) {
                    mDatas = new ArrayList<>();
                    if (page == 1) {
                        if (homeFollowBean.getNo_follow_creator_list() != null
                                && homeFollowBean.getNo_follow_creator_list().size() > 0) {
                            HomeFollowListBean h = new HomeFollowListBean();
                            h.setNo_follow_creator_list(homeFollowBean.getNo_follow_creator_list());
                            mDatas.add(h);
                        }
                        if (homeFollowBean.getFollow_creator_list() != null
                                && homeFollowBean.getFollow_creator_list().size() > 0) {
                            HomeFollowListBean h = new HomeFollowListBean();
                            h.setFollow_creator_list(homeFollowBean.getFollow_creator_list());
                            mDatas.add(h);
                        }
                        if (homeFollowBean.getBuilds() != null
                                && homeFollowBean.getBuilds().size() > 0) {
                            HomeFollowListBean h = new HomeFollowListBean();
                            h.setBuilds(homeFollowBean.getBuilds());
                            mDatas.add(h);
                        }
                        if (homeFollowBean.getNo_follow_creator_article_list() != null
                                && homeFollowBean.getNo_follow_creator_article_list().size() > 0) {
                            for (int i = 0; i < homeFollowBean.getNo_follow_creator_article_list().size(); i++) {
                                HomeFollowBean.NoFollowCreatorArticleList noFollowCreatorArticleList = new HomeFollowBean.NoFollowCreatorArticleList();
                                noFollowCreatorArticleList.setList(homeFollowBean.getNo_follow_creator_article_list().get(i).getList());
                                noFollowCreatorArticleList.setUser_data(homeFollowBean.getNo_follow_creator_article_list().get(i).getUser_data());
                                HomeFollowListBean h = new HomeFollowListBean();
                                h.setNo_follow_creator_article_list(noFollowCreatorArticleList);
                                mDatas.add(h);
                            }
                        }
                    }
                    if (homeFollowBean.getFollow_creator_article_list() != null
                            && homeFollowBean.getFollow_creator_article_list().size() > 0) {
                        for (int i = 0; i < homeFollowBean.getFollow_creator_article_list().size(); i++) {
                            HomeFollowBean.FollowCreatorArticleList followCreatorArticleList = new HomeFollowBean.FollowCreatorArticleList();
                            followCreatorArticleList.setUser_data(homeFollowBean.getFollow_creator_article_list().get(i).getUser_data());
                            followCreatorArticleList.setTitle(homeFollowBean.getFollow_creator_article_list().get(i).getTitle());
                            followCreatorArticleList.setArticle_type(homeFollowBean.getFollow_creator_article_list().get(i).getArticle_type());
                            followCreatorArticleList.setImg(homeFollowBean.getFollow_creator_article_list().get(i).getImg());
                            followCreatorArticleList.setBuilding(homeFollowBean.getFollow_creator_article_list().get(i).getBuilding());
                            followCreatorArticleList.setTime_set(homeFollowBean.getFollow_creator_article_list().get(i).getTime_set());
                            followCreatorArticleList.setReply_num(homeFollowBean.getFollow_creator_article_list().get(i).getReply_num());
                            followCreatorArticleList.setAgree_num(homeFollowBean.getFollow_creator_article_list().get(i).getAgree_num());
                            followCreatorArticleList.setUrl(homeFollowBean.getFollow_creator_article_list().get(i).getUrl());
                            HomeFollowListBean h = new HomeFollowListBean();
                            h.setFollow_creator_article_list(followCreatorArticleList);
                            mDatas.add(h);
                        }
                    }
                    if (mDatas != null && mDatas.size() == 0) {
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
            rv.setItemViewCacheSize(20);
            homeFollowAdapter = new HomeFollowAdapter(mContext, mDatas);
            homeFollowAdapter.setRecycleviewPool(rv.getRecycledViewPool());
            rv.setAdapter(homeFollowAdapter);
        } else {
            //添加
            homeFollowAdapter.addData(mDatas);
        }
    }
}
