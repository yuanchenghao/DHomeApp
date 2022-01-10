package com.dejia.anju.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dejia.anju.R;
import com.dejia.anju.adapter.BannerAdapter;
import com.dejia.anju.adapter.HomeAdapter;
import com.dejia.anju.adapter.HomeFollowAdapter;
import com.dejia.anju.api.HomeIndexApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.HomeIndexBean;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

//首页推荐fragment
public class RecommendFragment extends BaseFragment {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv)
    RecyclerView rv;
    private int page;
    private HomeIndexApi homeIndexApi;
    private HomeAdapter homeAdapter;
    private HomeIndexBean homeIndexBean;
    private YMLinearLayoutManager ymLinearLayoutManager;
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

    public static RecommendFragment newInstance() {
        Bundle args = new Bundle();
        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Xml(layouts = "fragment_recommend")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View view) {
        //刷新监听
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //加载更多
                page++;
                getHomeList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新
                page = 1;
                homeAdapter = null;
                getHomeList();
            }
        });
    }


    public void refresh() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        if (banner != null) {
            banner.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止轮播
        if (banner != null) {
            banner.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁
        if (banner != null) {
            banner.destroy();
        }
    }

    //获取首页数据
    private void getHomeList() {
        homeIndexApi = new HomeIndexApi();
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("page", page);
        homeIndexApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            smartRefreshLayout.finishRefresh();
            if ("1".equals(serverData.code)) {
                mHasLoadedOnce = true;
                homeIndexBean = JSONUtil.TransformSingleBean(serverData.data, HomeIndexBean.class);
                if (homeIndexBean != null
                        && homeIndexBean.getFocus_picture() != null
                        && homeIndexBean.getFocus_picture().size() > 0) {
                    //设置轮播
                    setBanner(homeIndexBean.getFocus_picture());
                    banner.setVisibility(View.VISIBLE);
                } else {
                    if (page == 1) {
                        banner.setVisibility(View.GONE);
                    }
                }
                if (homeIndexBean != null && homeIndexBean.getList() != null) {
                    if (homeIndexBean.getList().size() == 0) {
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
        if (null == homeAdapter) {
            ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            RecyclerView.ItemAnimator itemAnimator = rv.getItemAnimator();
            //取消局部刷新动画效果
            if (null != itemAnimator) {
                ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            }
            rv.setLayoutManager(ymLinearLayoutManager);
            rv.setItemViewCacheSize(20);
            homeAdapter = new HomeAdapter(mContext, homeIndexBean.getList());
            homeAdapter.setRecycleviewPool(rv.getRecycledViewPool());
            rv.setAdapter(homeAdapter);
            homeAdapter.setEventListener((v, data, pos) -> {
                if (!TextUtils.isEmpty(data.getUrl())) {
                    WebUrlJumpManager.getInstance().invoke(mContext, data.getUrl(), null);
                }
            });
        } else {
            //添加
            homeAdapter.addData(homeIndexBean.getList());
        }
    }

    //设置轮播图
    private void setBanner(List<HomeIndexBean.FocusPicture> focus_picture) {
        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(new BannerAdapter(focus_picture));
        banner.start();
        banner.setOnBannerListener((data, position) -> {
            HomeIndexBean.FocusPicture FocusPicture = (HomeIndexBean.FocusPicture) data;
            if (data != null && !TextUtils.isEmpty(FocusPicture.getUrl())) {
                WebUrlJumpManager.getInstance().invoke(mContext, FocusPicture.getUrl(), null);
            }
        });
    }

    @Override
    protected void initData(View view) {

    }
}
