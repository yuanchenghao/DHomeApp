package com.dejia.anju.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.activity.SearchActivity;
import com.dejia.anju.adapter.YmTabLayoutAdapter;
import com.dejia.anju.api.GetCityApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.CityInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.BaseCityPopWindow;
import com.google.android.material.tabs.TabLayout;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * @author ych
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_root)
    View ll_root;
    @BindView(R.id.ll_title)
    RelativeLayout ll_title;
    @BindView(R.id.ll_area)
    LinearLayout ll_area;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.iv_city)
    ImageView iv_city;
    @BindView(R.id.home_tab_layout)
    TabLayout home_tab_layout;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.vp)
    ViewPager vp;
    //viewpager相关
    private ArrayList<BaseFragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private YmTabLayoutAdapter ymTabLayoutAdapter;
    //当前选中
    private int mFragmentSelectedPos = 0;
    private CityInfo cityInfo;
    private GetCityApi getCityApi;
    private BaseCityPopWindow cityPopWindow;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 0:
                //退出
            case 1:
                //登录
                if (ymTabLayoutAdapter != null) {
                    if (ymTabLayoutAdapter.getItem(mFragmentSelectedPos) instanceof RecommendFragment) {
                        if (ymTabLayoutAdapter != null && (RecommendFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos) != null) {
                            ((RecommendFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos)).refresh();
                        }
                    } else {
                        if (ymTabLayoutAdapter != null && (FollowFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos) != null) {
                            ((FollowFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos)).refresh();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Xml(layouts = "fragment_home")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        cityPopWindow = new BaseCityPopWindow(mContext, ll_root, cityInfo);
    }

    @Override
    protected void initData(View view) {
        titleList.add("头条");
        titleList.add("关注");
        fragmentList.add(RecommendFragment.newInstance());
        fragmentList.add(FollowFragment.newInstance());
        ymTabLayoutAdapter = new YmTabLayoutAdapter(getChildFragmentManager(), titleList, fragmentList);
        vp.setAdapter(ymTabLayoutAdapter);
        home_tab_layout.setupWithViewPager(vp);
        setTabmStyle();
        setMultiOnClickListener(ll_area, iv_search);
        getCityList();
        home_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragmentSelectedPos = tab.getPosition();
                if (tab != null) {
                    View view = tab.getCustomView();
                    //文字
                    ((TextView) view.findViewById(R.id.tv_tab)).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.tv_tab)).setText(titleList.get(tab.getPosition()));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(Color.parseColor("#1C2125"));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextSize(20);
                    ((TextView) view.findViewById(R.id.tv_tab)).setTypeface(Typeface.DEFAULT_BOLD);
                }
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab != null) {
                    View view = tab.getCustomView();
                    ((TextView) view.findViewById(R.id.tv_tab)).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.tv_tab)).setText(titleList.get(tab.getPosition()));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(Color.parseColor("#171922"));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextSize(18);
                    ((TextView) view.findViewById(R.id.tv_tab)).setTypeface(Typeface.DEFAULT);
                    tab.setCustomView(view);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabmStyle() {
        //根据Tab数量循环来设置
        for (int i = 0; i < home_tab_layout.getTabCount(); i++) {
            TabLayout.Tab tab = home_tab_layout.getTabAt(i);
            if (tab != null) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null);
                if (i == 0) {
                    ((TextView) view.findViewById(R.id.tv_tab)).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.tv_tab)).setText(titleList.get(tab.getPosition()));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(Color.parseColor("#1C2125"));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextSize(20);
                    ((TextView) view.findViewById(R.id.tv_tab)).setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    ((TextView) view.findViewById(R.id.tv_tab)).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.tv_tab)).setText(titleList.get(tab.getPosition()));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(Color.parseColor("#171922"));
                    ((TextView) view.findViewById(R.id.tv_tab)).setTextSize(18);
                    ((TextView) view.findViewById(R.id.tv_tab)).setTypeface(Typeface.DEFAULT);
                }
                //最后添加view到Tab上面
                tab.setCustomView(view);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_area:
                if (cityInfo != null
                        && cityInfo.getHot_city() != null
                        && cityInfo.getHot_city().size() > 0) {
                    if (cityPopWindow.isShowing()) {
                        cityPopWindow.dismiss();
                    } else {
                        cityPopWindow = new BaseCityPopWindow(mContext, ll_root, cityInfo);
                        cityPopWindow.showPop(statusbarHeight);
                        cityPopWindow.setOnAllClickListener((city, hotCity) -> {
                            if (!TextUtils.isEmpty(city) && hotCity.getIs_start_using() == 1) {
                                cityPopWindow.dismiss();
                                tv_city.setText(city);
                                Util.setCity(city);
                                if (ymTabLayoutAdapter.getItem(mFragmentSelectedPos) instanceof RecommendFragment) {
                                    if (ymTabLayoutAdapter != null && (RecommendFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos) != null) {
                                        ((RecommendFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos)).refresh();
                                    }
                                } else {
                                    if (ymTabLayoutAdapter != null && (FollowFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos) != null) {
                                        ((FollowFragment) ymTabLayoutAdapter.getItem(mFragmentSelectedPos)).refresh();
                                    }
                                }
                            } else {
                                ToastUtils.toast(mContext, "暂未开放").show();
                                cityPopWindow.dismiss();
                            }
                        });
                    }
                } else {
                    getCityList();
                }
                break;
            case R.id.iv_search:
                SearchActivity.invoke(mContext);
                break;
        }
    }

    private void getCityList() {
        getCityApi = new GetCityApi();
        getCityApi.getCallBack(mContext, new HashMap<>(0), (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                cityInfo = JSONUtil.TransformSingleBean(serverData.data, CityInfo.class);
            }
        });
    }
}
