package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseFragment;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.Nullable;

//首页推荐fragment
public class RecommendFragment extends BaseFragment {


    public static RecommendFragment newInstance() {
        Bundle args = new Bundle();
        RecommendFragment fragment = new RecommendFragment();
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

    @Xml(layouts = "fragment_recommend")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(View view) {

    }
}
