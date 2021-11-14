package com.dejia.anju.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.adapter.YMTabLayoutAdapter;
import com.dejia.anju.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class CircleFragment extends BaseFragment {
    @BindView(R.id.ll_title)
    RelativeLayout ll_title;
    @BindView(R.id.home_tab_layout)
    TabLayout home_tab_layout;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.vp)
    ViewPager vp;
    //viewpager相关
    private ArrayList<BaseFragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private YMTabLayoutAdapter ymTabLayoutAdapter;
    //当前选中的信息流
    private int mFragmentSelectedPos = 0;

    public static CircleFragment newInstance() {
        Bundle args = new Bundle();
        CircleFragment fragment = new CircleFragment();
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

    @Xml(layouts = "fragment_circle")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
    }

    @Override
    protected void initData(View view) {
        titleList.add("北京广场");
        titleList.add("全部板块");
        fragmentList.add(BeijingSquareFragment.newInstance());
        fragmentList.add(AllPlatesFragment.newInstance());
        ymTabLayoutAdapter = new YMTabLayoutAdapter(getChildFragmentManager(), titleList, fragmentList);
        vp.setAdapter(ymTabLayoutAdapter);
        home_tab_layout.setupWithViewPager(vp);
        setTabmStyle();
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
        for (int i = 0; i < home_tab_layout.getTabCount(); i++) {//根据Tab数量循环来设置
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
                tab.setCustomView(view);//最后添加view到Tab上面
            }
        }
    }
}
