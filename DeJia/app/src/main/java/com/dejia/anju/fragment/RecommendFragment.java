package com.dejia.anju.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dejia.anju.R;
import com.dejia.anju.adapter.BannerAdapter;
import com.dejia.anju.adapter.HomeAdapter;
import com.dejia.anju.api.HomeIndexApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
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
import androidx.annotation.Nullable;
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
        smartRefreshLayout.autoRefresh();
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
        banner.destroy();
    }

    //获取首页数据
    private void getHomeList() {
        homeIndexApi = new HomeIndexApi();
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("page", page);
        homeIndexApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                smartRefreshLayout.finishRefresh();
                if ("1".equals(serverData.code)) {
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
            homeAdapter = new HomeAdapter(mContext, homeIndexBean.getList());
            rv.setAdapter(homeAdapter);
            homeAdapter.setOnItemClickListener(new HomeAdapter.onItemClickListener() {
                @Override
                public void onItemListener(View v, HomeIndexBean.HomeList data, int pos) {
                    if (!TextUtils.isEmpty(data.getUrl())) {
                        ToastUtils.toast(mContext, data.getUrl()).show();
                    }
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
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
                HomeIndexBean.FocusPicture FocusPicture = (HomeIndexBean.FocusPicture) data;
                if (data != null && !TextUtils.isEmpty(FocusPicture.getUrl())) {
                    //web跳转还没做
                    ToastUtils.toast(mContext, FocusPicture.getUrl()).show();
                }
            }
        });
    }

    @Override
    protected void initData(View view) {

    }
}
